package com.moonlight.admin.user.controller;

import com.moonlight.utils.SubRouter;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.web.Router;

public enum MountUserRouter implements SubRouter {
	INSTANCE;

	@Override
	public Router router(Vertx vertx) {
		Router router = Router.router(vertx);
		router.post("/register").handler(RegisterUser.INSTANCE::handle);
		router.post("/login").handler(LoginController.INSTANCE::handle);
		router.get("/all").handler(FetchAllUsersController.INSTANCE::handle);
		return router;
	}
}
