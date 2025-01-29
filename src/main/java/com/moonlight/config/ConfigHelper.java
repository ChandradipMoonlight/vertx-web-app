package com.moonlight.config;

import io.vertx.core.json.JsonObject;

public enum ConfigHelper {
	INSTANCE;

	public JsonObject getMySqlConfig() {
		return ConfigManager.INSTANCE.getMainConfig().getJsonObject("sql");
	}

	public JsonObject getGenderizeConfig() {
		return ConfigManager.INSTANCE.getMainConfig()
				.getJsonObject("external").getJsonObject("genderize");
	}

	public JsonObject getExternalConfig() {
		return ConfigManager.INSTANCE.getMainConfig().getJsonObject("external");
	}

	public JsonObject getAuthConfig() {
		return ConfigManager.INSTANCE.getMainConfig().getJsonObject("auth");
	}

	public JsonObject getMongoConfig() {
		return ConfigManager.INSTANCE.getMainConfig().getJsonObject("mongo1");
	}
}
