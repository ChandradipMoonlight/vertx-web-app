package com.moonlight.admin.auth;

import com.moonlight.admin.user.request.UserLoginRequest;
import com.moonlight.exception.RoutingError;
import com.moonlight.models.repos.UserRepository;
import com.moonlight.models.sql.Users;
import io.vertx.rxjava.core.RxHelper;
import io.vertx.rxjava.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Single;

public enum AccessMiddleware {
	INSTANCE;

	private static final Logger log = LoggerFactory.getLogger(AccessMiddleware.class);

	public Single<UserLoginRequest> authenticateRequest(RoutingContext context) {
		return Single.just(context).subscribeOn(RxHelper.blockingScheduler(context.vertx()))
				.map(this::validateRequest);
	}

	private UserLoginRequest validateRequest(RoutingContext context) {
		String token = context.request().getHeader("Authorization");
		log.info("Token : {}", token);
		if (token==null) {
			throw new RoutingError("Token Not Found in the header", 401);
		}
		if (token.length()<7) {
			throw new RoutingError("Invalid Token", 401);
		}
		String tokenType = token.substring(0, 6);
		log.info("tokenType : {}", tokenType);
		if (!tokenType.equals("Bearer")) {
			throw new RoutingError("Invalid Token Type", 401);
		}
		String accessToken = token.substring(7);
		Integer userId = TokenService.INSTANCE.getIdFromToken(accessToken);
		if (userId==null) {
			throw new RoutingError("Invalid Token", 401);
		}

		Users users = UserRepository.INSTANCE.findUserById(userId)
				.orElseThrow(() -> new RoutingError("User Not Found", 401));
		UserLoginRequest request = new UserLoginRequest();
		request.setContext(context);
		request.setUsers(users);
		return request;
	}

}
