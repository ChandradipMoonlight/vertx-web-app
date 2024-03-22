package com.moonlight.models.mapper;

import lombok.Builder;
import lombok.Data;

@Data
public class CustomerResponse {
	private Integer customerId;
	private String firstName;
	private String lastName;
	private Integer age;
	private AddressResponse addressResponse;
	private Long createdAt;
	private Long updatedAt;
}
