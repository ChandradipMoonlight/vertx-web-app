package com.moonlight;

import com.moonlight.config.ConfigManager;
import com.moonlight.controller.AddEmployeeController;
import com.moonlight.controller.AssetController;
import com.moonlight.controller.GetAllEmployeeController;
import com.moonlight.controller.GetEmployeeController;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRoutes extends AbstractVerticle {

	private static final Logger logger = LoggerFactory.getLogger(HttpRoutes.class);

	@Override
	public void start(Promise<Void> startPromise) throws Exception {
//		super.start(startPromise);
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		HttpServerOptions serverOptions = new HttpServerOptions();
		serverOptions.setCompressionSupported(true);
		vertx
//				.createHttpServer()
				.createHttpServer(serverOptions)
				.requestHandler(router)
				.listen(ConfigManager.INSTANCE.getMainConfig().getInteger("port"), httpListenHandler-> {
					if (httpListenHandler.succeeded()) {
						logger.info("HTTP server started on port : {}", ConfigManager.INSTANCE.getMainConfig().getInteger("port"));
						startPromise.complete();
					} else {
						logger.info("HTTP server failed to start : {}", httpListenHandler.cause());
						startPromise.fail(httpListenHandler.cause().getMessage());
					}
				});
		try {
			createRoutes(router);
		} catch (Exception e ) {
			logger.error("Exception : {}", e);
		}
	}

	public void createRoutes(final Router router) {
		router.get("/asset").handler(AssetController.INSTANCE::handle);
		router.post("/employee/save").handler(AddEmployeeController.INSTANCE::handle);
		router.get("/employee/:employeeId").handler(GetEmployeeController.INSTANCE::handle);
		router.get("/employee/all").handler(GetAllEmployeeController.INSTANCE::handle);
	}
}
