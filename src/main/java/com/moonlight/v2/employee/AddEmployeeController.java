package com.moonlight.v2.employee;

import com.moonlight.admin.auth.AccessMiddleware;
import com.moonlight.admin.user.NewCommonController;
import com.moonlight.admin.user.request.UserLoginRequest;
import com.moonlight.exception.RoutingError;
import com.moonlight.models.mapper.EmployeeMapper;
import com.moonlight.models.mapper.EmployeeResponse;
import com.moonlight.models.mapper.Response;
import com.moonlight.models.mapper.SuccessResponse;
import com.moonlight.models.sql.Employee;
import com.moonlight.utils.ResponseUtils;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.web.RoutingContext;
import lombok.Data;

public enum AddEmployeeController implements NewCommonController {
  INSTANCE;

  @Override
  public void handle(RoutingContext context) {
    AccessMiddleware.INSTANCE.authenticateRequest(context)
      .map(this::doOnNext)
      .subscribe(
        onSuccess -> ResponseUtils.INSTANCE.writeJsonResponse(context, onSuccess),
        onError -> ResponseUtils.INSTANCE.handleError(context, onError)
      );
  }

  private SuccessResponse doOnNext(UserLoginRequest request) {
    try {
      JsonObject employeeJson = request.getContext().getBodyAsJson();
      System.out.println("Employee Request : "+employeeJson.encode());
      Request req = employeeJson.mapTo(Request.class);
      Employee employee = new Employee();
      employee.setEmployeeAge(req.getEmployeeAge());
      employee.setEmployeeName(req.employeeName);
      employee.setEmployeeEmail(req.employeeEmail);
      employee.setEmployeeSalary(req.getEmployeeSalary());
      if (req.gender!=null) {
        employee.setGender(req.gender);
      }
      employee.save();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RoutingError(e.getMessage());
    }
    return  SuccessResponse.generateSuccessResponse();
  }



  @Data
  private static class Request {
    private String employeeName;
    private String employeeEmail;
    private Integer employeeAge;
    private double employeeSalary;
    private String gender;
  }
}
