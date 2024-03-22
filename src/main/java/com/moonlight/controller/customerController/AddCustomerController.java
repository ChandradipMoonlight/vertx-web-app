package com.moonlight.controller.customerController;

import com.moonlight.controller.CommonController;
import com.moonlight.models.mapper.CommonMapper;
import com.moonlight.models.mapper.CustomerRequest;
import com.moonlight.models.mapper.CustomerResponse;
import com.moonlight.models.mapper.Response;
import com.moonlight.models.sql.Customer;
import com.moonlight.utils.ResponseUtils;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public enum AddCustomerController implements CommonController {
	INSTANCE;

	private static Logger logger = LoggerFactory.getLogger(AddCustomerController.class);

	@Override
	public void handle(RoutingContext context) {
		Response response = new Response();
		try {
			JsonObject bodyAsJson = context.getBodyAsJson();
			logger.info("Request body : {}", bodyAsJson.encodePrettily());
			CustomerRequest customerRequest = bodyAsJson.mapTo(CustomerRequest.class);
			Customer customer = CommonMapper.INSTANCE.createCustomer(customerRequest);
			customer.save();
			CustomerResponse customerResponse = CommonMapper.INSTANCE.createCustomerResponse(customer);
			response.setData(customerResponse);
			response.setMessage("Customer Data Added Successfully!");
			ResponseUtils.INSTANCE.writeJsonResponse(context, response);
		} catch (Exception e) {
			response.setErrors(Arrays.asList(e.getMessage()));
			ResponseUtils.INSTANCE.writeJsonErrorResponse(context, response, 500);
			logger.error("Error : {}", e.getMessage());
		}
	}
}
