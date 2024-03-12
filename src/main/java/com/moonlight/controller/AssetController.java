package com.moonlight.controller;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public enum AssetController implements CommonController{
	INSTANCE;

	@Override
	public void handle(RoutingContext context) {
		context.response()
				.putHeader("content-type", "application/json")
				.end(new JsonArray()
						.add(new JsonObject().put("symbol", "AA"))
						.add(new JsonObject().put("symbol", "AB"))
						.add(new JsonObject().put("symbol", "AC"))
						.encode()
				);
	}
}
