package com.moonlight.models.mapper;

import lombok.Data;

import java.util.List;

@Data
public class Response {
	private Object data;
	private String message;
	private List<String> errors;
}
