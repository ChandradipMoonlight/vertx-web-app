package com.moonlight.v2.employee;

import com.moonlight.admin.auth.AccessMiddleware;
import com.moonlight.admin.user.NewCommonController;
import com.moonlight.admin.user.request.UserLoginRequest;
import com.moonlight.exception.RoutingError;
import com.moonlight.external.PredicateGenderResponse;
import com.moonlight.models.mapper.Response;
import com.moonlight.models.repos.EmployeeRepository;
import com.moonlight.models.sql.Employee;
import com.moonlight.utils.ResponseUtils;
import com.moonlight.v2.external.GenderPredicateService;
import io.vertx.rxjava.ext.web.RoutingContext;
import rx.Single;

public enum UpdateEmployeeGender implements NewCommonController {
	INSTANCE;

	@Override
	public void handle(RoutingContext context) {
//		Single.just(context)
//				.map(this::updateGender)
    AccessMiddleware.INSTANCE.authenticateRequest(context)
      .map(this::updateGenderV2)
				.subscribe(
						response -> ResponseUtils.INSTANCE.writeJsonResponse(context, response),
						error -> ResponseUtils.INSTANCE.handleError(context, error)
				);
	}

//	private Response updateGender(RoutingContext context) {
//		Response response = new Response();
//		try {
//			Integer employeeId = Integer.valueOf(context.request().getParam("id"));
//			Integer employeeId2 = Integer.valueOf(context.pathParam("id"));
//			System.out.println("EmployeeId : "+employeeId);
//			System.out.println("employeeId2 : "+employeeId2);
//			Employee employee = EmployeeRepository.INSTANCE.findById(employeeId);
//			if (employee==null) {
//				throw new RoutingError("employee not found");
//			}
//			PredicateGenderResponse genderByName = GenderPredicateService.INSTANCE.getGenderByName(employee.getEmployeeName());
//			employee.setGender(genderByName.getGender());
//			employee.save();
//			response.setMessage("success");
//
//		} catch (Exception e) {
//			System.out.println(e);
//			throw new RoutingError(e.getMessage());
//		}
//		return response;
//	}
  private Response updateGenderV2(UserLoginRequest request) {
    Response response = new Response();
    try {
      Integer employeeId = Integer.valueOf(request.getContext().request().getParam("id"));
      Integer employeeId2 = Integer.valueOf(request.getContext().pathParam("id"));
      System.out.println("EmployeeId : "+employeeId);
      System.out.println("employeeId2 : "+employeeId2);
      Employee employee = EmployeeRepository.INSTANCE.findById(employeeId);
      if (employee==null) {
        throw new RoutingError("employee not found");
      }
      PredicateGenderResponse genderByName = GenderPredicateService.INSTANCE.getGenderByName(employee.getEmployeeName());
      employee.setGender(genderByName.getGender());
      employee.save();
      response.setMessage("success");

    } catch (Exception e) {
      System.out.println(e);
      throw new RoutingError(e.getMessage());
    }
    return response;
  }
}
