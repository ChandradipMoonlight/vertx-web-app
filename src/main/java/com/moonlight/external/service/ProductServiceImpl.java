package com.moonlight.external.service;

import com.moonlight.config.ConfigHelper;
import com.moonlight.external.GenderizeService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public enum ProductServiceImpl {
	INSTANCE;

	public ProductService fakeStoreApis() {
		String baseUrl = ConfigHelper.INSTANCE.getExternalConfig()
				.getJsonObject("products").getString("baseUrl");
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
		return retrofit.create(ProductService.class);
	}
}
