package com.moonlight.v2.employee;

import com.moonlight.admin.user.NewCommonController;
import com.moonlight.exception.RoutingError;
import com.moonlight.models.repos.EmployeeRepository;
import com.moonlight.models.sql.BaseModel;
import com.moonlight.utils.ResponseUtils;
import io.ebean.ExpressionList;
import io.vertx.rxjava.core.RxHelper;
import io.vertx.rxjava.ext.web.RoutingContext;
import lombok.Data;
import lombok.NoArgsConstructor;
import rx.Single;

public enum EmployeeStatisticController implements NewCommonController {
	INSTANCE;

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
			ExpressionList expressionList = EmployeeRepository.INSTANCE.findAllEmployee();
			boolean checkoutInstance = checkoutInstance(expressionList);
			if (checkoutInstance) {
				response.setSuccess(true);
				response.setInstanceOfBaseModel(true);
			}
		} catch (Exception e) {
			System.out.println(e);
			throw new RoutingError(e.getMessage());
		}
		return response;
	}

	private boolean checkoutInstance(ExpressionList expressionList) {
		Object object = expressionList.setMaxRows(1).findOne();
		boolean check = false;
		if (object instanceof BaseModel) {
			check =  true;
		}
		System.out.println("Check Instance : "+check);
		return check;

	}

	@NoArgsConstructor
	@Data
	private static class Response {
		private boolean success = false;
		private boolean instanceOfBaseModel =  false;
	}
}
