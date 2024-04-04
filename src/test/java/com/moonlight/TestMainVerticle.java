package com.moonlight;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.moonlight.models.mapper.Person;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class TestMainVerticle {

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void test_javaToJsonObject() {
    JsonObject jsonObject = JsonObject.mapFrom(new Person(101, "Chandradip"));
    System.out.println(jsonObject.encode());
  }

  void test_gson() {
    JsonArray jsonArray = new JsonArray();

    JsonElement jsonElement = jsonArray.get(0);

    Person person = new Gson().fromJson(jsonElement, Person.class);
  }
}
