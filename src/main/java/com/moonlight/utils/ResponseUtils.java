package com.moonlight.utils;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ResponseUtils {
	INSTANCE;

	private static final Logger logger = LoggerFactory.getLogger(ResponseUtils.class);

	public void writeJsonResponse(RoutingContext context, Object object) {
		try {
			context.response()
					.putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
					.end(JsonObject.mapFrom(object).encode());
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
		}
	}
	public void writeJsonResponse(RoutingContext context, Object object, String message) {
		try {
			context.response()
					.putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
					.end(JsonObject.mapFrom(object).put("message", message).encode());
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
		}
	}
	public void writeJsonResponse(RoutingContext context, Object object, String message, int statusCode) {
		try {
			context.response()
					.putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
					.setStatusCode(statusCode)
					.end(JsonObject.mapFrom(object).put("message", message).encode());
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
		}
	}

	public void writeJsonErrorResponse(RoutingContext context, String message, int statusCode) {
		try {
			context.response()
					.putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
					.setStatusCode(statusCode)
					.end(new JsonObject().put("message", message).encode());
		} catch (Exception e) {
			logger.info("Error : {}", e.getMessage());
		}
	}
}
