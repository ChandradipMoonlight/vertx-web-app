package com.moonlight.controller;

import io.vertx.ext.web.RoutingContext;

@FunctionalInterface
public interface CommonController {
	void handle(RoutingContext context);

	default void fail(String message)  {
		throw new RuntimeException(message);
	}
}
