package com.moonlight.controller;

import com.moonlight.models.repos.EmployeeRepository;
import com.moonlight.models.repos.InMemoryEmployeeRepo;
import com.moonlight.models.sql.Employee;
import com.moonlight.utils.ResponseUtils;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public enum GetAllEmployeeController implements CommonController{
	INSTANCE;

	private static final Logger logger = LoggerFactory.getLogger(GetAllEmployeeController.class);

	@Override
	public void handle(RoutingContext context) {
//		List<Employee> allEmployee = InMemoryEmployeeRepo.INSTANCE.findAll();
		List<Employee> allEmployee = EmployeeRepository.INSTANCE.findAll();
		logger.info("All Employee : {}", JsonObject.mapFrom(allEmployee));
		if (allEmployee.isEmpty()) {
			ResponseUtils.INSTANCE.writeJsonErrorResponse(context, "fail", HttpResponseStatus.NOT_FOUND.code());
		}
		ResponseUtils.INSTANCE.writeJsonResponse(context, allEmployee, "success", 200);
	}
}
