package com.moonlight.controller;

import com.moonlight.models.repos.InMemoryEmployeeRepo;
import com.moonlight.models.sql.Employee;
import com.moonlight.utils.ResponseUtils;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.ext.web.RoutingContext;

import java.util.Optional;

public enum GetEmployeeController implements CommonController {
	INSTANCE;
	@Override
	public void handle(RoutingContext context) {
		Integer employeeId = Integer.valueOf(context.pathParam("employeeId"));
		Optional<Employee> employee = InMemoryEmployeeRepo.INSTANCE.findById(employeeId);
		if (employee.isPresent()) {
			ResponseUtils.INSTANCE.writeJsonResponse(context, employee.get(), "success");
		} else {
			ResponseUtils.INSTANCE.writeJsonErrorResponse(context, "fail", HttpResponseStatus.NOT_FOUND.code());
		}
	}
}
