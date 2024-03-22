package com.moonlight.models.mapper;

import lombok.Data;

@Data

public class CustomerRequest {
	private String firstName;
	private String lastName;
	private Integer age;
	private AddressRequest address;
}
