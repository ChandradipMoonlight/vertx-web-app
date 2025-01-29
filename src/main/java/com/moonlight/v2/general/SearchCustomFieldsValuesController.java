package com.moonlight.v2.general;

import com.moonlight.admin.user.NewCommonController;
import com.moonlight.factory.MongoFactory;
import com.moonlight.utils.ResponseUtils;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.RxHelper;
import io.vertx.rxjava.ext.web.RoutingContext;
import lombok.Data;
import rx.Single;

import java.util.ArrayList;
import java.util.List;

import static com.moonlight.models.mongo.Collections.DbFields.*;


public enum SearchCustomFieldsValuesController implements NewCommonController {
  INSTANCE;

  @Override
  public void handle(RoutingContext context) {
    Single.just(context)
      .subscribeOn(RxHelper.blockingScheduler(context.vertx()))
      .flatMap(this::doNext)
      .subscribe(
        response -> ResponseUtils.INSTANCE.writeJsonResponse(context, response),
        error -> ResponseUtils.INSTANCE.handleError(context, error)
      );
  }

  private Single<Response> doNext(RoutingContext context) {
    String formType = context.request().params().get("formType");
    String searchValue = context.request().params().get("q");
    Long companyId = Long.valueOf(context.request().params().get("companyId"));
    String fieldId = context.request().params().get("fieldId");

    return MongoFactory.INSTANCE.searchFieldValues(companyId, formType, fieldId, searchValue)
      .map(Response::new);
  }

  @Data
  private static class Response {
    private List<JsonObject> data = new ArrayList<>();
    public Response(List<JsonObject> list) {
      this.data = list;
    }
  }



//  @Data
//  private static class Response {
//    List<ResponseData> data = new ArrayList<>();
//
//    public void setData(JsonObject json) {
//      System.out.println("Json Data :: "+json.encode());
//      ResponseData response = new ResponseData();
//      response.setField(json.getString(FIELD_ID));
//      response.setValue(json.getString(FIELD_VALUE));
//      response.setFormType(json.getString(FORM_TYPE));
//      response.setRequestId(json.getString(REQUEST_ID));
//      data.add(response);
//    }
//  }
//
//  @Data
//  private static class ResponseData {
//    private String field;
//    private String value;
//    private String requestId;
//    private String formType;
//  }

}
