package com.moonlight.external.service;

import com.moonlight.external.response.Product;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface ProductService {

	@GET("/products")
	public Call<List<Product>> getAllProducts();
}
