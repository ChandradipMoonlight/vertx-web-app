package com.moonlight.controller.employeeController;

import com.moonlight.controller.CommonController;
import com.moonlight.models.mapper.EmployeeRequest;
import com.moonlight.models.mapper.Response;
import com.moonlight.models.repos.EmployeeRepository;
import com.moonlight.models.sql.Employee;
import com.moonlight.utils.ResponseUtils;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Optional;

public enum UpdateEmployeeController implements CommonController {
	INSTANCE;

	private static final Logger logger = LoggerFactory.getLogger(UpdateEmployeeController.class);

	@Override
	public void handle(RoutingContext context) {
		Response response = new Response();
		try {
			Integer employeeId = Integer.valueOf(context.pathParam("employeeId"));
			Optional<Employee> employee = Optional.ofNullable(EmployeeRepository.INSTANCE.findById(employeeId));
			if (employee.isPresent()) {
				JsonObject jsonEmployeeRequest = context.getBodyAsJson();
				logger.info("Request received : {}", jsonEmployeeRequest.encodePrettily());
				EmployeeRequest employeeRequest = jsonEmployeeRequest.mapTo(EmployeeRequest.class);
				Employee updatEmployee = employee.get();
				updateEmployee(updatEmployee, employeeRequest);
				response.setData(updatEmployee);
				response.setMessage("Employee data updated successfully!");
				ResponseUtils.INSTANCE.writeJsonResponse(context, response);
			} else {
				String message = "Employee not found with given employeeId : "+employeeId;
				logger.error("Error : {}", message);
				response.setMessage(message);
				response.setErrors(Collections.singletonList("Employee Not Found"));
			}
		} catch (Exception e) {
			logger.error("Error occurred while updating Employee : {}", e);
			response.setMessage(e.getMessage());
			response.setErrors(Collections.singletonList(e.getMessage()));
			ResponseUtils.INSTANCE.writeJsonErrorResponse(context, response, 500);
		}
	}
	private void updateEmployee(Employee employee, EmployeeRequest request) {
		if (request.getEmployeeAge()!=null) {
			employee.setEmployeeAge(request.getEmployeeAge());
		}
		if (request.getEmployeeName()!=null) {
			employee.setEmployeeAge(request.getEmployeeAge());
		}
		if (request.getEmployeeSalary()>0) {
			employee.setEmployeeSalary(request.getEmployeeSalary());
		}
		if (request.getEmployeeEmail()!=null) {
			employee.setEmployeeEmail(request.getEmployeeEmail());
		}
		employee.save();
	}
}
