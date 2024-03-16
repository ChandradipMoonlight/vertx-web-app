package com.moonlight.external;

import com.moonlight.config.ConfigHelper;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public enum PublicApiClient {
	INSTANCE;

	public GenderizeService genderizeApiService() {
		String baseUrl = ConfigHelper.INSTANCE.getGenderizeConfig().getString("baseUrl");
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.addInterceptor(logging)
				.build();
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(baseUrl)
				.addConverterFactory(GsonConverterFactory.create())
				.client(okHttpClient)
				.build();
		return retrofit.create(GenderizeService.class);
	}
}
