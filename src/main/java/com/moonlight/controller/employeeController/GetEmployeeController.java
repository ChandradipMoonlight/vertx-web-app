package com.moonlight.controller.employeeController;

import com.moonlight.controller.CommonController;
import com.moonlight.models.mapper.Response;
import com.moonlight.models.repos.EmployeeRepository;
import com.moonlight.models.sql.Employee;
import com.moonlight.utils.ResponseUtils;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.ext.web.RoutingContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public enum GetEmployeeController implements CommonController {
	INSTANCE;
	@Override
	public void handle(RoutingContext context) {
		Integer employeeId = Integer.valueOf(context.pathParam("employeeId"));
//		Optional<Employee> employee = InMemoryEmployeeRepo.INSTANCE.findById(employeeId);
		Optional<Employee> employee = Optional.ofNullable(EmployeeRepository.INSTANCE.findById(employeeId));
		Response response = new Response();
		if (employee.isPresent()) {
			response.setData(employee.get());
			response.setMessage("Employee Details Fetched Successfully!");
			ResponseUtils.INSTANCE.writeJsonResponse(context, response, "success");
		} else {
			response.setMessage("Employee not found with given employeeId : "+employeeId);
			response.setErrors(Collections.singletonList("Employee not found!"));
			ResponseUtils.INSTANCE.writeJsonErrorResponse(context, response, HttpResponseStatus.NOT_FOUND.code());
		}
	}
}
