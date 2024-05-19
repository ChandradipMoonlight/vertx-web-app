package com.moonlight.utils;

import com.moonlight.exception.RoutingError;
import com.moonlight.models.mapper.Response;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public enum ResponseUtils {
	INSTANCE;

	private static final Logger logger = LoggerFactory.getLogger(ResponseUtils.class);

	public void writeJsonResponse(RoutingContext context, Object object) {
		try {
			context.response()
					.putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
					.end(JsonObject.mapFrom(object).put("status", "success").encode());
		} catch (Exception e) {
			logger.error("Exception in writeJsonResponse : {}", e);
		}
	}
	public void writeJsonResponse(io.vertx.rxjava.ext.web.RoutingContext context, Object object) {
		try {
			context.response()
					.putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
					.end(JsonObject.mapFrom(object).put("status", "success").encode());
		} catch (Exception e) {
			logger.error("Exception in writeJsonResponse : {}", e);
		}
	}
	public void writeJsonResponse(RoutingContext context, Object object, String status) {
		try {
			context.response()
					.putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
					.end(JsonObject.mapFrom(object).put("status", status).encode());
		} catch (Exception e) {
			logger.error("Exception in writeJsonResponse : {}", e);
		}
	}
	public void writeJsonResponse(RoutingContext context, Object object, String status, int statusCode) {
		try {
			context.response()
					.putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
					.end(JsonObject.mapFrom(object).put("status", status).encode());
		} catch (Exception e) {
			logger.error("Exception in converting Response in json : {}", e);
		}
	}

	public void writeJsonErrorResponse(RoutingContext context, String status, int statusCode) {
		try {
			context.response()
					.putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
					.setStatusCode(statusCode)
					.end(new JsonObject().put("status", status).put("statusCode", statusCode).encode());
		} catch (Exception e) {
			logger.error("Exception in writeJsonErrorResponse : {}", e);
		}
	}

	public void writeJsonErrorResponse(RoutingContext context, Object data, int statusCode) {
		try {
			context.response()
					.putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
					.setStatusCode(statusCode)
					.end(JsonObject.mapFrom(data).put("status", "fail").encode());
		} catch (Exception e) {
			logger.error("Exception occurred while mapping error response : {}", e);
		}
	}
	public void writeJsonErrorResponse(io.vertx.rxjava.ext.web.RoutingContext context, Object data, int statusCode) {
		logger.error("error Message : {}", data);
		try {
			if (!context.response().closed()) {
				context.response()
						.putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
						.setStatusCode(statusCode)
						.end(JsonObject.mapFrom(data).put("status", "fail").encode());
			}
		} catch (Exception e) {
			logger.error("Exception occurred while mapping error response : {}", e);
		}
	}

	private void handleError(RoutingContext context, String message, int statusCode) {
		Response response = new Response();
		response.setMessage(message);
		response.setErrors(Collections.singletonList(message));
		writeJsonErrorResponse(context, response, statusCode);
	}
	private void handleError(io.vertx.rxjava.ext.web.RoutingContext context, String message, int statusCode) {
		Response response = new Response();
		logger.error("response setMessage : {}", message);
		response.setMessage(message);
		response.getErrors().add(message);
		logger.error("handleError Response : {}",response);
		writeJsonErrorResponse(context, response, statusCode);
	}
	public void handleError(RoutingContext context, Throwable error) {
		handleError(context, error.getMessage(), ((RoutingError) error).getStatusCode());
	}
//	public void handleError(io.vertx.rxjava.ext.web.RoutingContext context, Throwable error) {
//		handleError(context, error.getMessage(), ((RoutingError) error).getStatusCode());
//	}
	public void handleError(io.vertx.rxjava.ext.web.RoutingContext rc, Throwable error){
		logger.error("error : {}", error.getMessage());
		Response response1 = new Response(error.getMessage());
		logger.error("response1 : {}", response1.getErrors());
		try {
			if(notReportable(error)){
				logger.info("inside if condition");
//				handleError(rc, error.getMessage(), ((RoutingError) error).getStatusCode());
				writeJsonErrorResponse(rc, response1, ((RoutingError) error).getStatusCode());
			}else{
//				notify(rc,error);
				handleError(rc, "Technical Error. Please try again later: #"+error.getMessage(), 409);
//                error.printStackTrace();
			}
		}catch (Exception e){
			logger.error("Inside catch block {}", e);
			handleError(rc,error.getMessage(), 409);
//            e.printStackTrace();
		}
	}
	boolean notReportable(Throwable error){
		boolean check = false;
		if(error instanceof RoutingError)
			check = true;
		if(error.getClass().isAssignableFrom(RoutingError.class))
			check = true;
		if(error instanceof NumberFormatException)
			check = true;
		logger.error("Not Reportable : {}", check);
		return check;
	}
	public void handleException(io.vertx.rxjava.ext.web.RoutingContext context, Throwable error) {
		String errorMessage = "Technical Error!";
		int statusCode = 409;
		try {
			if (notReportable(error)) {
				errorMessage = error.getMessage();
				statusCode = ((RoutingError) error).getStatusCode();
			}
			logger.error("Inside Handle Exception : {}, {}", errorMessage, statusCode);
			if (!context.response().closed()) {
				context.response()
						.putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
						.setStatusCode(statusCode)
						.end(JsonObject.mapFrom(new Response(errorMessage)).put("status", "fail").encode());
			}
		} catch (Exception e) {
			handleError(context, e);
		}
	}
}
