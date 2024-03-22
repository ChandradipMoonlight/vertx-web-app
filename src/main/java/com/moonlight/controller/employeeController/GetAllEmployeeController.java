package com.moonlight.controller.employeeController;

import com.moonlight.controller.CommonController;
import com.moonlight.models.mapper.EmployeeMapper;
import com.moonlight.models.mapper.EmployeeResponse;
import com.moonlight.models.mapper.Response;
import com.moonlight.models.repos.EmployeeRepository;
import com.moonlight.models.sql.Employee;
import com.moonlight.utils.ResponseUtils;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum GetAllEmployeeController implements CommonController {
	INSTANCE;

	private static final Logger logger = LoggerFactory.getLogger(GetAllEmployeeController.class);

	@Override
	public void handle(RoutingContext context) {
		Response response = new Response();
		try {
			List<Employee> employees = EmployeeRepository.INSTANCE.findAll();
			if (employees==null||employees.isEmpty()) {
				ResponseUtils.INSTANCE.writeJsonErrorResponse(context, response, HttpResponseStatus.NOT_FOUND.code());
			}else {
				List<EmployeeResponse> employeeResponses = employees.stream()
						.map(EmployeeMapper.INSTANCE::createEmployeeResponse)
						.collect(Collectors.toList());
				//Create Response
				response.setData(employeeResponses);
				response.setMessage("All Employee Details Fetched!");
				logger.info("Response, Employees : {}", JsonObject.mapFrom(response).encodePrettily());
				ResponseUtils.INSTANCE.writeJsonResponse(context, response, "success");
			}
		} catch (Exception e) {
			logger.error("Error in Fetching All Employee : {}", e);
			response.setErrors(Arrays.asList(e.getMessage()));
			ResponseUtils.INSTANCE.writeJsonErrorResponse(context, response, 500);
		}

	}
}
