package com.moonlight.external.controller;

import com.moonlight.controller.CommonController;
import com.moonlight.external.response.Product;
import com.moonlight.external.service.ProductServiceImpl;
import com.moonlight.models.mapper.Response;
import com.moonlight.utils.ResponseUtils;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public enum GetAllProductsController implements CommonController {
	INSTANCE;
	private static final Logger logger = LoggerFactory.getLogger(GetAllProductsController.class);
	@Override
	public void handle(RoutingContext context) {
		Response response = new Response();
		try {
			retrofit2.Response<List<Product>> execute = ProductServiceImpl.INSTANCE.fakeStoreApis()
					.getAllProducts().execute();
			if (execute.isSuccessful()&& execute.body()!=null) {
				execute.body().forEach(product -> logger.info("Response : {}", product));
				response.setData(execute.body());
				response.setMessage("All Products fetched successfully");
				ResponseUtils.INSTANCE.writeJsonResponse(context, response);
			} else {
				response.setErrors(Collections.singletonList("Error in api"));
				response.setMessage("exception occurred");
				ResponseUtils.INSTANCE.writeJsonErrorResponse(context, response, 500);
			}
		} catch (Exception e) {
			logger.error("Error in GetAllProductController : {}", e);
			response.setMessage(e.getMessage());
			response.setErrors(Collections.singletonList(e.getMessage()));
			ResponseUtils.INSTANCE.writeJsonErrorResponse(context, response, 500);
		}
	}
}
