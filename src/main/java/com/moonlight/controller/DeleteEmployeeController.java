package com.moonlight.controller;

import com.moonlight.models.mapper.Response;
import com.moonlight.models.repos.EmployeeRepository;
import com.moonlight.models.sql.Employee;
import com.moonlight.utils.ResponseUtils;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Optional;

public enum DeleteEmployeeController implements CommonController{
	INSTANCE;
	private static final Logger logger = LoggerFactory.getLogger(DeleteEmployeeController.class);

	@Override
	public void handle(RoutingContext context) {
		Response response = new Response();
		try {
			Integer employeeId = Integer.valueOf(context.pathParam("employeeId"));
			Optional<Employee> employee = Optional.ofNullable(EmployeeRepository.INSTANCE.findById(employeeId));
			if (employee.isPresent()) {
				employee.get().delete();
				response.setMessage("Employee Deleted Successfully!");
				ResponseUtils.INSTANCE.writeJsonResponse(context, response);
			} else {
				response.setErrors(Collections.singletonList("Employee is not found!"));
				ResponseUtils.INSTANCE.writeJsonErrorResponse(context, response, HttpResponseStatus.NOT_FOUND.code());
			}
		} catch (Exception e) {
			logger.error("Error occurred : {}", e);
			response.setErrors(Collections.singletonList(e.getMessage()));
			ResponseUtils.INSTANCE.writeJsonErrorResponse(context, response, 500);
		}
	}
}
