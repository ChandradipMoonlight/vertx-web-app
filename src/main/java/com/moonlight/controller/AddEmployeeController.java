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
			JsonObject employeeJson = context.getBodyAsJson();
			logger.info("Employee Json Request : {}", employeeJson.encodePrettily());
			Employee employee = employeeJson.mapTo(Employee.class);
//			Employee save = InMemoryEmployeeRepo.INSTANCE.save(employee);
			employee.save();
			ResponseUtils.INSTANCE.writeJsonResponse(context, employee, "success");
			logger.info("Response : {}", JsonObject.mapFrom(employee).encodePrettily());
		} catch (Exception e) {
			ResponseUtils.INSTANCE.writeJsonErrorResponse(context, e.getMessage(), 500);
			logger.error("Error : {}", e.getMessage());
			e.printStackTrace();
		}
	}
}
