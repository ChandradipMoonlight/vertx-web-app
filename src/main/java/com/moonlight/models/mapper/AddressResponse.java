package com.moonlight.models.mapper;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

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
