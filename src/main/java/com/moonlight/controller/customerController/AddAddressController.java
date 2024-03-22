package com.moonlight.controller.customerController;

import com.moonlight.controller.CommonController;
import com.moonlight.models.mapper.AddressRequest;
import com.moonlight.models.mapper.AddressResponse;
import com.moonlight.models.mapper.CommonMapper;
import com.moonlight.models.mapper.Response;
import com.moonlight.models.repos.CustomerRepository;
import com.moonlight.models.sql.Address;
import com.moonlight.models.sql.Customer;
import com.moonlight.utils.ResponseUtils;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Optional;

public enum AddAddressController implements CommonController {
	INSTANCE;

	private static Logger logger = LoggerFactory.getLogger(AddAddressController.class);

	@Override
	public void handle(RoutingContext context) {
		Response response = new Response();
		try {
			Integer customerId = Integer.valueOf(context.pathParam("customerId"));
			Optional<Customer> customer = Optional.ofNullable(CustomerRepository.INSTANCE.findById(customerId));
			if (!customer.isPresent()) {
				response.setMessage("Customer not found with given Id : "+customerId);
				response.setErrors(Collections.singletonList("Customer Not Found!"));
				return;
			}
			JsonObject bodyAsJson = context.getBodyAsJson();
			logger.info("Request body as json : {}", bodyAsJson);
			AddressRequest addressRequest = bodyAsJson.mapTo(AddressRequest.class);
			Address address = CommonMapper.INSTANCE.createAddress(addressRequest);
			address.setCustomer(customer.get());
			address.save();
			AddressResponse addressResponse = CommonMapper.INSTANCE.createAddressResponse(address);
			addressResponse.setCustomerId(customerId);
			response.setData(addressResponse);
			response.setMessage("Address Added SuccessFully!");
			ResponseUtils.INSTANCE.writeJsonResponse(context, response);
		} catch (Exception e) {
			logger.error("Error while saving address : {}", e);
			response.setErrors(Collections.singletonList(e.getMessage()));
			ResponseUtils.INSTANCE.writeJsonErrorResponse(context, response, 500);
		}
	}
}
