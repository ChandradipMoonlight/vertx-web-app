package com.moonlight.models.repos;

import com.moonlight.models.sql.Address;


public enum AddressRepository {
	INSTANCE;

	private SqlFinder<Address, Integer> addressFinder = new SqlFinder<>(Address.class);

	public Address findById(Integer id) {
		return addressFinder.findById(id);
	}
}
