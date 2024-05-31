package com.moonlight.v2.external;

import com.moonlight.admin.user.NewCommonController;
import com.moonlight.exception.RoutingError;
import com.moonlight.external.PredicateGenderResponse;
import com.moonlight.models.mapper.Response;
import com.moonlight.utils.ResponseUtils;
import io.vertx.rxjava.ext.web.RoutingContext;
import rx.Single;

public enum PredicateGenderController implements NewCommonController {
	INSTANCE;

	@Override
	public void handle(RoutingContext context) {
		Single.just(context)
				.map(this::doOnNext)
				.subscribe(
						response -> ResponseUtils.INSTANCE.writeJsonResponse(context, response),
						error -> ResponseUtils.INSTANCE.handleError(context, error)
				);
	}

	private Response doOnNext(RoutingContext context) {
		Response response = new Response();
		try {
			String name = context.request().getParam("name");
			PredicateGenderResponse genderByName = GenderPredicateService.INSTANCE.getGenderByName(name);
			response.setMessage("Success");
			response.setData(genderByName);
		} catch (Exception e) {
			System.out.println(e);
			throw new RoutingError(e.getMessage());
		}
		return response;
	}
}
