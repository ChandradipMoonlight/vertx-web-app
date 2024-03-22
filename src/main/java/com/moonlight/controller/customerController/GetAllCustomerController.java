package com.moonlight.controller.customerController;

import com.moonlight.controller.CommonController;
import com.moonlight.models.mapper.CommonMapper;
import com.moonlight.models.mapper.CustomerResponse;
import com.moonlight.models.mapper.Response;
import com.moonlight.models.repos.CustomerRepository;
import com.moonlight.models.sql.Customer;
import com.moonlight.utils.ResponseUtils;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public enum GetAllCustomerController implements CommonController {
	INSTANCE;

	private static Logger logger = LoggerFactory.getLogger(GetAllCustomerController.class);
	@Override
	public void handle(RoutingContext context) {
		Response response = new Response();
		try {
			List<Customer> customers = CustomerRepository.INSTANCE.findAllCustomers();
			List<CustomerResponse> responses = customers.stream().map(CommonMapper.INSTANCE::createCustomerResponse)
					.collect(Collectors.toList());
			response.setData(responses);
			response.setMessage("All Customer Details fetched successfully!");
			ResponseUtils.INSTANCE.writeJsonResponse(context, response);
		} catch (Exception e) {
			logger.error("Error in GetAllCustomerController : {}", e);
			response.setErrors(Collections.singletonList(e.getMessage()));
			ResponseUtils.INSTANCE.writeJsonErrorResponse(context, response, 500);
		}
	}
}
