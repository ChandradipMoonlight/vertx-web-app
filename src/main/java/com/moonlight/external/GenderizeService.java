package com.moonlight.external;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GenderizeService {

	@GET("/")
	Call<PredicateGenderResponse> guessGenderFromName(@Query("name") String name);
}
