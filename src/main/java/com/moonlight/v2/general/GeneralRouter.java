package com.moonlight.v2.general;

import com.moonlight.utils.SubRouter;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.web.Router;

public enum GeneralRouter implements SubRouter {
	INSTANCE;

	@Override
	public Router router(Vertx vertx) {
		Router router = Router.router(vertx);
		router.post("/customFields").handler(FetchAllCustomMapping.INSTANCE::handle);
		return router;
	}
}
