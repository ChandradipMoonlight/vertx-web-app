package com.moonlight.v2.general;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import com.moonlight.admin.user.NewCommonController;
import com.moonlight.factory.MongoFactory;
import com.moonlight.models.mongo.Collections;
import com.moonlight.models.mongo.CustomFieldMapping;
import com.moonlight.utils.ResponseUtils;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.web.RoutingContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Single;

import java.util.*;
import java.util.stream.Collectors;

import static com.moonlight.models.mongo.Collections.DbFields.*;

public enum FetchAllCustomMapping implements NewCommonController {
	INSTANCE;

	private static final Logger log = LoggerFactory.getLogger(FetchAllCustomMapping.class);

	@Override
	public void handle(RoutingContext context) {
		Single.just(context)
				.flatMap(this::doNext)
				.subscribe(
						response -> ResponseUtils.INSTANCE.writeJsonResponse(context, response),
						error -> ResponseUtils.INSTANCE.handleError(context, error)
				);
	}

	private Single<Response> doNext(RoutingContext context) {
		JsonObject bodyAsJson = context.getBodyAsJson();
		System.out.println("Json Request : {}"+ bodyAsJson);
		Request request = new Gson().fromJson(bodyAsJson.encode(), Request.class);
		if (request.getData()==null) {
			bodyAsJson.getMap().forEach((k, v) -> {
				request.getData().put(k, (String) v);
			});
		}
		return fetchMappings(request)
				.map(Response::new);
	}

	private Single<List<CustomFieldMapping>> fetchMappings(Request request) {
		log.info("Request : {}", request);
		JsonObject query = query(request);
		return MongoFactory.INSTANCE.findAll(Collections.CUSTOM_FIELD_MAPPING, query)
				.map(jsonObjectList -> {
					log.info("Response : {}", Arrays.toString(jsonObjectList.toArray()));
					return jsonObjectList.stream()
							.map(jsonObject -> jsonObject.mapTo(CustomFieldMapping.class))
							.collect(Collectors.toList());
				}).doOnError(error -> log.error("Error : {}", error));
	}


	private JsonObject query1(Request request) {

		JsonObject query = new JsonObject();
		JsonArray andQuery = new JsonArray();
		andQuery.add(new JsonObject().put(COMPANY_ID, request.companyId));
		andQuery.add(new JsonObject().put(FORM_TYPE, request.formType));
		JsonArray orQuery = new JsonArray();
		request.data.forEach((k, v) -> {
			JsonObject orQueryItems = new JsonObject();
			orQueryItems.put(FIELD_ID, k);
			orQueryItems.put(FIELD_VALUE, v);
			orQuery.add(orQueryItems);
		});
		andQuery.add(new JsonObject().put("$or", orQuery));
		query.put("$and", andQuery);
		System.out.println("Query : "+query.encode());
		return query;
	}

	private JsonObject query(Request request) {
		JsonObject query = new JsonObject();
		query.put(COMPANY_ID, request.companyId);
		return query;
	}

	@NoArgsConstructor
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private static class Request {
		private Long companyId;
		private String formType;
		private Map<String, String> data = new HashMap<>();
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	private static class Response {
		List<CustomFieldMapping> mappings = new ArrayList<>();
	}

}
