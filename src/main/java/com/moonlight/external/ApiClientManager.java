package com.moonlight.external;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public enum ApiClientManager {
	INSTANCE;

	private static final Logger log = LoggerFactory.getLogger(ApiClientManager.class);

	public <T> T getApiClient(String baseUrl, Class<T> tClass) {
		Retrofit retrofit = null;
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);
		try {
			retrofit = new Retrofit.Builder()
					.baseUrl(baseUrl)
					.client(new OkHttpClient().newBuilder()
							.addInterceptor(logging)
							.build())
					.addConverterFactory(GsonConverterFactory.create())
					.build();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return retrofit.create(tClass);
	}
}
