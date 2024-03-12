package com.moonlight.factory;

import com.moonlight.config.ConfigHelper;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum SqlBeanFactory {
	INSTANCE;

	private static final Logger logger = LoggerFactory.getLogger(SqlBeanFactory.class);

	private DataSourceConfig getDataSourceConfig() {
		JsonObject jsonObject = ConfigHelper.INSTANCE.getMySqlConfig();
		logger.info("SqlConfig : {}", jsonObject.encodePrettily());
		DataSourceConfig config = new DataSourceConfig();
		config.setDriver(jsonObject.getString("driver"));
		config.setUrl(jsonObject.getString("url"));
		config.setUsername(jsonObject.getString("username"));
		config.setPassword(jsonObject.getString("password"));
		logger.info("DataSourceConfig : {}", config);
		return config;
	}

	public void init() {
		dbConnection();
	}
	public void stop() {
		dbConnection().shutdown();
	}

	public Database dbConnection() {
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setDataSourceConfig(getDataSourceConfig());
		dbConfig.setDdlGenerate(true);

//		dbConfig.setDefaultServer(true);
//		dbConfig.setDdlCreateOnly(true);
//		dbConfig.setDdlRun(true);
		dbConfig.addPackage("models.sql");
		return DatabaseFactory.create(dbConfig);
	}

}
