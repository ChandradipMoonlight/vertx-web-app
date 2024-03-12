package com.moonlight.config;

import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.Setter;


public enum ConfigManager {
	INSTANCE;

	private JsonObject mainConfig;

	public void setMainConfig(JsonObject config){
		this.mainConfig = config;
	}

	public JsonObject getMainConfig(){
		return this.mainConfig;
	}

}
