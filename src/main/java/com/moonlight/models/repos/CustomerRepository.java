package com.moonlight.models.repos;

import com.moonlight.models.sql.Customer;

import java.util.List;

public enum CustomerRepository {
	INSTANCE;

	private SqlFinder<Customer, Integer> customerFinder = new SqlFinder<>(Customer.class);

	public Customer findById(Integer id) {
		return customerFinder.findById(id);
	}

	public List<Customer> findAllCustomers() {
		return customerFinder.findAll();
	}
}
