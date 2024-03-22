package com.moonlight.models.mapper;

import com.moonlight.models.sql.Address;
import com.moonlight.models.sql.Customer;

public enum CommonMapper {
	INSTANCE;

	public Customer createCustomer(CustomerRequest request) {
		Customer customer =  new Customer();
		customer.setFirstName(request.getFirstName());
		customer.setLastName(request.getLastName());
		customer.setAge(request.getAge());
		if (request.getAddress()!=null) {
			customer.setAddress(createAddress(request.getAddress()));
		}
		return customer;
	}

	public Address createAddress(AddressRequest request) {
		Address address = new Address();
		address.setCity(request.getCity());
		address.setState(request.getState());
		address.setPinCode(request.getPinCode());
		return address;
	}

	public CustomerResponse createCustomerResponse(Customer customer) {
		CustomerResponse response = new CustomerResponse();
		response.setCustomerId(customer.getId());
		response.setFirstName(customer.getFirstName());
		response.setLastName(customer.getLastName());
		response.setAge(customer.getAge());
		response.setCreatedAt(customer.getCreatedAt().getTime());
		response.setUpdatedAt(customer.getUpdatedAt().getTime());
		if (customer.getAddress()!=null) {
			response.setAddressResponse(createAddressResponse(customer.getAddress()));
		}
		return response;
	}

	public AddressResponse createAddressResponse(Address address) {
		AddressResponse response = new AddressResponse();
		response.setAddressId(address.getId());
		response.setCity(address.getCity());
		response.setState(address.getState());
		response.setPinCode(address.getPinCode());
		response.setCreatedAt(address.getCreatedAt().getTime());
		response.setUpdatedAt(address.getUpdatedAt().getTime());
		return response;
	}
}
