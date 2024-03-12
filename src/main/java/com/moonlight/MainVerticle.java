package com.moonlight;

import com.moonlight.config.ConfigManager;
import com.moonlight.controller.AddEmployeeController;
import com.moonlight.controller.AssetController;
import com.moonlight.factory.SqlBeanFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

  private static final Logger log = LoggerFactory.getLogger(MainVerticle.class);
  public static final int PORT = 8888;

//  public static void main(String[] args) {
//    final Vertx vertx = Vertx.vertx();
//    vertx.exceptionHandler(error -> log.error("error : {}", error.getMessage()));
//    vertx.deployVerticle(MainVerticle.class.getName(), ar -> {
//      if (ar.failed()) {
//        log.info("Failed to deploy : {}", ar.cause());
//        return;
//      }
//      log.info("Deployed {}", MainVerticle.class.getSimpleName());
//    });
//  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    ConfigManager.INSTANCE.setMainConfig(config());
    log.info("config : {}", config().encodePrettily());
      vertx.deployVerticle(HttpRoutes.class.getName(),
              new DeploymentOptions().setInstances(getProcessors()),
              completionHandler -> {
                if (completionHandler.succeeded()) {
                  log.info("Deployed {} with Id {}", HttpRoutes.class.getSimpleName(), completionHandler);
                  startPromise.complete();
                } else {
                  log.error("Error :: {}", completionHandler.cause());
                }
              });
    SqlBeanFactory.INSTANCE.init();
  }

  private int getProcessors() {
    return Math.max(1, Runtime.getRuntime().availableProcessors());
  }

  @Override
  public void stop() throws Exception {
    SqlBeanFactory.INSTANCE.stop();
  }
}
