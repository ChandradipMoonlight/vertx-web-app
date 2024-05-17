package com.moonlight.exception;

import lombok.Data;

@Data
public class RoutingError extends RuntimeException {
	private Integer statusCode;

	public RoutingError(String message) {
		super(message);
	}

	public RoutingError(String message, Integer statusCode) {
		super(message);
		this.statusCode = statusCode;
	}
}
