package com.moonlight.admin.user.request;

import com.moonlight.models.sql.Users;
import io.vertx.rxjava.ext.web.RoutingContext;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class UserLoginRequest {
	private RoutingContext context;
	private Users users;
//	private Map<String, Object> request;
//	public UserLoginRequest() {
//		request = new HashMap<>();
//	}
//	private Map<String, Object> getRequest() {
//		if (request==null) {
//			request = new HashMap<>();
//		}
//		return request;
//	}
}
