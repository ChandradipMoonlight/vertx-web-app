package com.moonlight.v2.external;

import com.moonlight.utils.SubRouter;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.web.Router;

public enum MountExternalRouter implements SubRouter {
	INSTANCE;

	@Override
	public Router router(Vertx vertx) {
		Router router = Router.router(vertx);
		router.get("/gender").handler(PredicateGenderController.INSTANCE::handle);
		return router;
	}
}
