package com.moonlight.models.mapper;

import lombok.Data;

@Data
public class AddressRequest {
	private String city;
	private String state;
	private Integer pinCode;
}
