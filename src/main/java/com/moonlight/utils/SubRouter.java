package com.moonlight.utils;

import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.web.Router;

public interface SubRouter {

	Router router(Vertx vertx);
}
