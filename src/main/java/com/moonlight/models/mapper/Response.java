package com.moonlight.models.mapper;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Response {
	private Object data;
	private String message;
	private List<String> errors;
	public List<String> getErrors() {
		if (errors==null) {
			errors=new ArrayList<>();
		}
		return errors;
	}
	public Response() {}
	public Response(String message) {
		this.getErrors().add(message);
		this.message = message;
	}
}
