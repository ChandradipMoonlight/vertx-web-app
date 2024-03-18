package com.moonlight.external;

import com.moonlight.controller.CommonController;
import com.moonlight.utils.ResponseUtils;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;

import java.util.Collections;
import java.util.List;

public enum PredicateGenderController implements CommonController {
	INSTANCE;

	private static final Logger logger = LoggerFactory.getLogger(PredicateGenderController.class);

	@Override
	public void handle(RoutingContext context) {
		com.moonlight.models.mapper.Response stdResponse = new com.moonlight.models.mapper.Response();
		try {
			List<String> name = context.queryParam("name");
			Response<PredicateGenderResponse> jsonResponse = PublicApiClient.INSTANCE.genderizeApiService()
					.guessGenderFromName(name.get(0))
					.execute();
			if (jsonResponse.isSuccessful()) {
				logger.info("Api Response form client : {}", jsonResponse.body());
				 stdResponse.setMessage("Data fetched successfully");
				 stdResponse.setData(jsonResponse.body());
				 ResponseUtils.INSTANCE.writeJsonResponse(context, stdResponse);
			} else {
				assert jsonResponse.errorBody() != null;
				stdResponse.setErrors(Collections.singletonList(jsonResponse.errorBody().string()));
				ResponseUtils.INSTANCE.writeJsonErrorResponse(context, stdResponse, jsonResponse.code());
			}
		} catch (Exception e) {
			stdResponse.setErrors(Collections.singletonList(e.getMessage()));
			logger.error("Error in API response : {}", e);
			ResponseUtils.INSTANCE.writeJsonErrorResponse(context, stdResponse, 500);
		}

	}
}
