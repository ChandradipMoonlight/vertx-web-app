package com.moonlight.models.mapper;

import lombok.Data;


@Data
public class AddressResponse {
	private Integer addressId;
	private String city;
	private String state;
	private Integer pinCode;
	private Long createdAt;
	private Long updatedAt;
	private Integer customerId;
}
