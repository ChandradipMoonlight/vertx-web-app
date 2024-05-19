package com.moonlight.admin.user.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.moonlight.admin.auth.TokenService;
import com.moonlight.admin.user.NewCommonController;
import com.moonlight.exception.RoutingError;
import com.moonlight.models.mapper.Response;
import com.moonlight.models.repos.UserRepository;
import com.moonlight.models.sql.Users;
import com.moonlight.utils.ResponseUtils;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.RxHelper;
import io.vertx.rxjava.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Single;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum LoginController implements NewCommonController {
	INSTANCE;

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@Override
	public void handle(RoutingContext context) {
		Single.just(context)
				.subscribeOn(RxHelper.blockingScheduler(context.vertx()))
				.map(this::doNext)
				.subscribe(
						response -> ResponseUtils.INSTANCE.writeJsonResponse(context, response),
						error -> ResponseUtils.INSTANCE.handleError(context, error)
				);
	}

	private Response doNext(RoutingContext context) {
		Response response = new Response();
		try {
			JsonObject bodyAsJson = context.getBodyAsJson();
			LoginRequest loginRequest = bodyAsJson.mapTo(LoginRequest.class);
			Users users = UserRepository.INSTANCE.usersFinder.getExpressionList()
					.eq("email", loginRequest.userName)
					.findOneOrEmpty()
					.orElseThrow(() -> new RoutingError("User not found with given userName : " + loginRequest.userName));
			if (!users.getPassword().equals(loginRequest.password)) {
				throw new RoutingError("Invalid Password");
			}
			String token = TokenService.INSTANCE.generateToken(users.getEmail(), users.getId());
			Map<String, String> data = new HashMap<>();
			data.put("token", token);
			response.setMessage("success");
			response.setData(data);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new RoutingError(e.getMessage());
		}
		return response;
	}


	@JsonInclude(JsonInclude.Include.NON_NULL)
	private static class LoginRequest {
		@JsonProperty(value = "userName")
		private String userName;
		@JsonProperty(value = "password")
		private String password;
		public LoginRequest(){}
	}
}
