package com.moonlight.v2.employee;

import com.moonlight.utils.SubRouter;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.web.Router;

public enum MountEmployeeRouter implements SubRouter {
	INSTANCE;

	@Override
	public Router router(Vertx vertx) {
		Router router = Router.router(vertx);
		router.get("/:id/updateGender").handler(UpdateEmployeeGender.INSTANCE::handle);
		return router;
	}
}
