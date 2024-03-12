package com.moonlight;

import com.moonlight.config.ConfigManager;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class AssetControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(AssetControllerTest.class);

	@Test
	public void returnAllAsset(Vertx vertx, VertxTestContext context) {
		WebClient webClient = WebClient.create(vertx, new WebClientOptions().setDefaultPort(ConfigManager.INSTANCE.getMainConfig().getInteger("port")));
		webClient.get("/asset")
				.send(context.succeeding(httpResponse -> {
					String res = httpResponse.bodyAsJsonArray().encode();
					logger.info("Response : {}", res);
					assertEquals(200, httpResponse.statusCode());
					assertEquals("[{\"symbol\":\"AA\"},{\"symbol\":\"AB\"},{\"symbol\":\"AC\"}]", res);
					context.completeNow();

				}));
	}
}
