package com.moonlight.factory;

import com.moonlight.config.ConfigHelper;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.BulkOperation;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.mongo.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Single;

import java.util.ArrayList;
import java.util.List;

public enum MongoFactory {
	INSTANCE;

	private static final Logger log = LoggerFactory.getLogger(MongoFactory.class);
	MongoClient client;
	Vertx vertx;

	public void init(Vertx vertx) {
		this.vertx = vertx;
		try {
			client = MongoClient.createShared(vertx, ConfigHelper.INSTANCE.getMongoConfig());
			log.info("Mongo Client initiated");

		} catch (Exception e) {
			log.error("Error in Creating MongoClient : {}", e);
		}
	}

	public MongoClient getMongoClient() {
		if (client==null) {
			client = MongoClient.createShared(vertx, ConfigHelper.INSTANCE.getMongoConfig());
		}
		return client;
	}

	public void createCollection(String collection) {
		client.createCollection(collection,  handler -> {
			if (handler.succeeded()) {
				log.info("Collection is created : {}", handler.result());
			} else {
				log.error("Error in creating collection : {}", handler.cause().getMessage());
			}
		});
	}

	public void insert(String collection, JsonObject jsonObject) {
		client.insert(collection, jsonObject, resultHandler -> {
			if (resultHandler.succeeded()) {
				log.info("Data : {} :: is insert in collection : {}", jsonObject.encodePrettily(), collection);
			} else {
				log.error("Error in insert operation : {}", resultHandler.cause().toString());
			}
		});
	}

	public void insertBulk(String collection, JsonArray jsonArray) {
		List<BulkOperation> operations = new ArrayList<>();
		for (Object o : jsonArray) {
			JsonObject jsonObject = (JsonObject) o;
			operations.add(BulkOperation.createInsert(jsonObject));
		}
		if (operations.isEmpty()) {
			return;
		}
		getMongoClient().bulkWrite(collection, operations, event -> {
			if (event.failed()) {
				log.error("Error in bulkInsert : {}", event.cause().getMessage());
			} else {
				log.info("Bulk data inserted");
			}
		});
	}

	public Single<List<JsonObject>> findAll(String collection, JsonObject query) {
		return Single.create(onSubscribe -> {
			System.out.println("Executing find with Query: " + query.encodePrettily());
			getMongoClient().findWithOptions(collection, query, new FindOptions().setLimit(1000), asyncResult -> {
				if (asyncResult.succeeded()) {
					List<JsonObject> results = asyncResult.result();
					System.out.println("Find succeeded. Result size: " + results.size());
					for (JsonObject result : results) {
						System.out.println("Document: " + result.encodePrettily());
					}
					onSubscribe.onSuccess(results);
				} else {
					System.out.println("Find failed with error: " + asyncResult.cause().getMessage());
					onSubscribe.onError(asyncResult.cause());
				}
			});
		});
	}


	public Single<JsonObject> findById(String collection, String id) {
		return Single.create(singleSubscriber -> {
			getMongoClient().findOne(collection, new JsonObject().put("_id", id), null, asyncResult -> {
				if (asyncResult.succeeded()) {
					singleSubscriber.onSuccess(asyncResult.result());
				} else {
					singleSubscriber.onError(asyncResult.cause());
				}
			});
		});
	}

	public Single<JsonObject> deleteById(String collection, String id) {
		return Single.create(singleSubscriber -> {
			getMongoClient().findOneAndDelete(collection, new JsonObject().put("_id", id), handler -> {
				if (handler.succeeded()) {
					singleSubscriber.onSuccess(handler.result());
				} else {
					singleSubscriber.onError(handler.cause());
				}
			});
		});
	}

	public Single<JsonObject> findOne(String collection, JsonObject query) {
		return Single.create(singleSubscriber -> {
			getMongoClient().findOne(collection, query, null, handler -> {
				if (handler.succeeded()) {
					singleSubscriber.onSuccess(handler.result());
				} else {
					singleSubscriber.onError(handler.cause());
				}
			});
		});
	}

	public Single<JsonObject> deleteOne(String collection, JsonObject query) {
		return Single.create(singleSubscriber -> {
			getMongoClient().findOneAndDelete(collection, query, handler -> {
				if (handler.succeeded()) {
					singleSubscriber.onSuccess(handler.result());
				} else {
					singleSubscriber.onError(handler.cause());
				}
			});
		});
	}
}
