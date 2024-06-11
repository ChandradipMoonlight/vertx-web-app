package com.moonlight.v2.external;

import com.moonlight.config.ConfigHelper;
import com.moonlight.exception.RoutingError;
import com.moonlight.external.ApiClientManager;
import com.moonlight.external.GenderizeService;
import com.moonlight.external.PredicateGenderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;

public enum GenderPredicateService {
	INSTANCE;

	private static final Logger log = LoggerFactory.getLogger(GenderPredicateService.class);

	public  PredicateGenderResponse getGenderByName(String name) {
		PredicateGenderResponse response = null;
		try {
			String baseUrl = ConfigHelper.INSTANCE.getGenderizeConfig().getString("baseUrl");
			Response<PredicateGenderResponse> jsonResponse = ApiClientManager.INSTANCE.getApiClient(baseUrl, GenderizeService.class)
					.guessGenderFromName(name)
					.execute();
			if (jsonResponse.isSuccessful()) {
				response = jsonResponse.body();
			}
		} catch (Exception e) {
			System.out.println(e);
			throw new RoutingError(e.getMessage());
		}
		return response;
	}

}
