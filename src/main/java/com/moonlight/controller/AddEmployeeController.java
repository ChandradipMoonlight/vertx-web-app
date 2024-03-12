package com.moonlight.controller;

import com.moonlight.models.repos.InMemoryEmployeeRepo;
import com.moonlight.models.sql.Employee;
import com.moonlight.utils.ResponseUtils;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public enum AddEmployeeController implements CommonController{
	INSTANCE;

	private static final Logger logger = LoggerFactory.getLogger(AddEmployeeController.class);
	@Override
	public void handle(RoutingContext context) {

		try {
			logger.info("request : {}", context.getBody());
			JsonObject employeeJson = context.getBodyAsJson();
			logger.info("Employee Json Request : {}", employeeJson.encodePrettily());
			Employee employee = employeeJson.mapTo(Employee.class);
//			Employee save = InMemoryEmployeeRepo.INSTANCE.save(employee);
			employee.save();
			Employee save = employee;

			ResponseUtils.INSTANCE.writeJsonResponse(context, save, "success");
			logger.info("Response : {}", JsonObject.mapFrom(save).encodePrettily());
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			e.printStackTrace();
		}
	}
}
