package com.moonlight;

import com.moonlight.admin.user.controller.MountUserRouter;
import com.moonlight.config.ConfigManager;
import com.moonlight.v2.employee.MountEmployeeRouter;
import com.moonlight.v2.external.MountExternalRouter;
import com.moonlight.v2.general.GeneralRouter;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RxHttpRouter extends AbstractVerticle {

	private static final Logger log = LoggerFactory.getLogger(RxHttpRouter.class);

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		HttpServerOptions serverOptions = new HttpServerOptions();
		serverOptions.setCompressionSupported(true);
		vertx.createHttpServer(serverOptions)
				.requestHandler(router)
				.listen(ConfigManager.INSTANCE.getMainConfig().getInteger("port"), httpListenHandler -> {
					if (httpListenHandler.succeeded()) {
						log.info("RxHttp server started on port : {}", ConfigManager.INSTANCE.getMainConfig().getInteger("port"));
						try {
							super.start(startFuture);

						} catch (Exception e) {
							log.error("Error in RxHttp Server : {}", e);
						}
					} else {
						log.info("RxHttp server failed to start : {}", httpListenHandler.cause());
						try {
							throw startFuture.cause();
						} catch (Throwable e) {
							log.error("Error in RxHttp Serer : {}", e);
						}
					}
				});
		try {
			createRouter(router);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	private void createRouter(Router router) {
		router.mountSubRouter("/users", MountUserRouter.INSTANCE.router(vertx));
		router.mountSubRouter("/external", MountExternalRouter.INSTANCE.router(vertx));
		router.mountSubRouter("/employee", MountEmployeeRouter.INSTANCE.router(vertx));
		router.mountSubRouter("/general", GeneralRouter.INSTANCE.router(vertx));
	}
}
