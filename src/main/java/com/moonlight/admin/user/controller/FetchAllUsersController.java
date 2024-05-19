package com.moonlight.admin.user.controller;

import com.moonlight.admin.auth.AccessMiddleware;
import com.moonlight.admin.user.NewCommonController;
import com.moonlight.admin.user.request.UserLoginRequest;
import com.moonlight.models.mapper.Response;
import com.moonlight.models.repos.UserRepository;
import com.moonlight.models.sql.Users;
import com.moonlight.utils.ResponseUtils;
import io.vertx.rxjava.ext.web.RoutingContext;

import java.util.List;

public enum FetchAllUsersController implements NewCommonController {
	INSTANCE;

	@Override
	public void handle(RoutingContext context) {
		AccessMiddleware.INSTANCE.authenticateRequest(context)
				.map(this::doNext)
				.subscribe(
						response -> ResponseUtils.INSTANCE.writeJsonResponse(context, response),
						error -> ResponseUtils.INSTANCE.handleError(context, error)
				);
	}

	Response doNext(UserLoginRequest request) {
		Response response = new Response();
		List<Users> users = UserRepository.INSTANCE.usersFinder.getExpressionList()
				.findList();
		response.setData(users);
		response.setMessage("success");
		return response;
	}
}
