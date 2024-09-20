package com.moonlight.admin.user.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.moonlight.admin.user.NewCommonController;
import com.moonlight.exception.RoutingError;
import com.moonlight.models.mapper.Response;
import com.moonlight.models.sql.Users;
import com.moonlight.utils.ResponseUtils;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.RxHelper;
import io.vertx.rxjava.ext.web.RoutingContext;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Single;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum RegisterUser implements NewCommonController {
	INSTANCE;

	private static final Logger log = LoggerFactory.getLogger(RegisterUser.class);

	@Override
	public void handle(RoutingContext context) {
		Single.just(context)
				.subscribeOn(RxHelper.blockingScheduler(context.vertx()))
				.map(this::doNext)
				.subscribe(
          response -> ResponseUtils.INSTANCE.writeJsonResponse(context, response),
          error -> ResponseUtils.INSTANCE.handleException(context, error)
				);
	}
	private Response doNext(RoutingContext context) {
		Response response = new Response();
		try {
			JsonObject bodyAsJson = context.getBodyAsJson();
			log.info("Request body : {}", bodyAsJson.toString());
			UserRequest userRequest = bodyAsJson.mapTo(UserRequest.class);
			log.info("UserRequest : {}", userRequest.toString());
			if (userRequest==null) {
				throw new RoutingError("Request Body can not be null", 400);
			}
			validateRequest(userRequest);
			Users users = mapRequestToEntity(userRequest);
			users.save();
			Map<String, Object> data = new HashMap<>();
			data.put("userId", users.getId());
			response.setData(data);
			response.setMessage("success");
			log.info("saved data : {}", JsonObject.mapFrom(response).toString());
		} catch (Exception e) {
			log.error("Exception : {}", e);
			throw new RoutingError(e.getMessage());
		}
		return response;
	}

	@Data
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class UserRequest {
		private String firstName;
		private String lastName;
		private String middleName;
		private String email;
		private String mobileNo;
		private String password;
	}
	private void validateRequest(UserRequest request) {
		if (request.getEmail()==null) {
			throw new RoutingError("Please provide email", 400);
		}
		if (request.getFirstName()==null) {
			throw new RoutingError("Please provide First Name 'fName'", 400);
		}
		if (request.getLastName()==null) {
			throw new RoutingError("Please provide Last Name 'lName'", 400);
		}
		if (request.getPassword()==null) {
			throw new RoutingError("Please provide password", 400);
		}
	}
	private Users mapRequestToEntity(UserRequest request) {
		log.info("inside mapRequestToEntity");
		Users users = new Users();
		users.setFName(request.getFirstName());
		users.setLName(request.getLastName());
		users.setPassword(request.getPassword());
		users.setEmail(request.getEmail());
		if (request.getMobileNo()!=null) {
			users.setMobileNo(request.getMobileNo());
		}
		if (request.getMiddleName()!=null) {
			users.setMName(request.getMiddleName());
		}
		return users;
	}

  private void test(){
    Observable.just("abc").map(s -> s.toUpperCase(Locale.ROOT))
      .subscribe(
//        success -> System.out.println(success),
        System.out::println,
        error -> System.out.println(error.getMessage())
      );
  }
}
