package com.moonlight.controller;

import com.moonlight.models.mapper.EmployeeMapper;
import com.moonlight.models.mapper.EmployeeResponse;
import com.moonlight.models.mapper.Response;
import com.moonlight.models.repos.InMemoryEmployeeRepo;
import com.moonlight.models.sql.Employee;
import com.moonlight.utils.ResponseUtils;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;

public enum AddEmployeeController implements CommonController{
	INSTANCE;

	private static final Logger logger = LoggerFactory.getLogger(AddEmployeeController.class);
	@Override
	public void handle(RoutingContext context) {
		Response response = new Response();
		try {
			JsonObject employeeJson = context.getBodyAsJson();
			logger.info("Employee Json Request : {}", employeeJson.encodePrettily());
			Employee employee = employeeJson.mapTo(Employee.class);
//			Employee save = InMemoryEmployeeRepo.INSTANCE.save(employee);
			employee.save();
			EmployeeResponse employeeResponse = EmployeeMapper.INSTANCE.createEmployeeResponse(employee);
			response.setData(employeeResponse);
			response.setMessage("Employee Data Saved Successfully!");
			ResponseUtils.INSTANCE.writeJsonResponse(context, response, "success");
			logger.info("EmployeeResponse : {}", JsonObject.mapFrom(response).encodePrettily());
		} catch (Exception e) {
			response.setErrors(Arrays.asList(e.getMessage()));
			ResponseUtils.INSTANCE.writeJsonErrorResponse(context, response, 500);
			logger.error("Error : {}", e.getMessage());
		}
	}

}
