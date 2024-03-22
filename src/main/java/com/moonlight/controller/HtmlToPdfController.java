package com.moonlight.controller;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.file.Files;
import java.util.UUID;

public enum HtmlToPdfController implements CommonController {

	INSTANCE;

	private static final Logger logger = LoggerFactory.getLogger(HtmlToPdfController.class);

	@Override
	public void handle(RoutingContext context) {
		try {
			File pdfFile = convertHtmlToPdf();
			if (pdfFile != null && pdfFile.exists()) {
				Buffer buffer = convertPdfFileToBuffer(pdfFile);
				sendResponse(context, buffer, pdfFile);
			} else {
				context.response().setStatusCode(500).end("Error converting HTML to PDF");
			}
		} catch (Exception e) {
			logger.error("Error handling HTML to PDF conversion: {}", e);
			context.response().setStatusCode(500).end("Error handling HTML to PDF conversion");
		}
	}

	private File convertHtmlToPdf() {
		String filePath = "src/main/resources/templates/test.html";
		File inputHtml = new File(filePath);
		Document document = null;
		File pdfFile = null;
		try {
			document = Jsoup.parse(inputHtml, "UTF-8", "");
			document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
			pdfFile = File.createTempFile(UUID.randomUUID().toString().replaceAll("-", ""), ".pdf");
			try (OutputStream outputStream = Files.newOutputStream(pdfFile.toPath())) {
				ITextRenderer renderer = new ITextRenderer();
				SharedContext sharedContext = renderer.getSharedContext();
				sharedContext.setPrint(true);
				sharedContext.setInteractive(false);
				renderer.setDocumentFromString(inputHtml.toURI().toURL().toString());
				renderer.layout();
				renderer.createPDF(outputStream);
			} catch (Exception e) {
				logger.error("Error while converting HTML to PDF: {}", e);
			}
		} catch (IOException e) {
			logger.error("Exception in fileInput: {}", e);
		}
		return pdfFile;
	}

	private Buffer convertPdfFileToBuffer(File pdfFile) {
		Buffer buffer = Buffer.buffer();
		try (FileInputStream fileInputStream = new FileInputStream(pdfFile)) {
			byte[] bytes = new byte[1024];
			int bytesRead;
			while ((bytesRead = fileInputStream.read(bytes)) != -1) {
				buffer.appendBytes(bytes, 0, bytesRead);
			}
		} catch (IOException e) {
			logger.error("Error converting PDF file to buffer: {}", e);
		}
		return buffer;
	}

	private void sendResponse(RoutingContext context, Buffer buffer, File pdfFile) {
		context.response()
				.putHeader(HttpHeaders.CONTENT_TYPE, "application/pdf")
				.putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(pdfFile.length()))
				.putHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"downloaded_pdf.pdf\"")
				.write(buffer)
				.end();

		// Delete the temporary PDF file
		if (pdfFile != null && pdfFile.exists()) {
			pdfFile.delete();
		}
	}
}
