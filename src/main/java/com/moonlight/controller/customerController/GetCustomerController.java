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
import java.util.Optional;

public enum GetCustomerController implements CommonController {
	INSTANCE;

	private static Logger logger = LoggerFactory.getLogger(GetCustomerController.class);

	@Override
	public void handle(RoutingContext context) {
		Response response = new Response();
		try {
			Integer customerId = Integer.valueOf(context.pathParam("customerId"));
			Optional<Customer> customer = Optional.ofNullable(CustomerRepository.INSTANCE.findById(customerId));
			if (!customer.isPresent()) {
				response.setMessage("Customer data is not present with given Id : "+ customerId);
				ResponseUtils.INSTANCE.writeJsonErrorResponse(context, response, 404);
				return;
			}
			CustomerResponse customerResponse = CommonMapper.INSTANCE.createCustomerResponse(customer.get());
			response.setMessage("Customer Data Fetched SuccessFully!");
			response.setData(customerResponse);
			ResponseUtils.INSTANCE.writeJsonResponse(context, response);

		} catch (Exception e) {
			logger.error("Error in GetCustomerController : {}", e);
			response.setErrors(Collections.singletonList(e.getMessage()));
			ResponseUtils.INSTANCE.writeJsonErrorResponse(context, response, 500);
		}
	}
}
