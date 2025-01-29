package com.moonlight.factory;

import com.moonlight.config.ConfigHelper;
import com.moonlight.models.mongo.CustomFieldMapping;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.BulkOperation;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.streams.ReadStream;
import io.vertx.rxjava.ext.mongo.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Single;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.moonlight.models.mongo.Collections.CUSTOM_FIELD_MAPPING;
import static com.moonlight.models.mongo.Collections.DbFields.*;

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
			System.out.println(client.toString());
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
	public Single<List<HashMap<String, String>>> searchResults(String table, JsonObject object) {
		return findAll(table, object).map(jsonObjects -> {
			return jsonObjects.stream()
					.map(jsonObject -> {
						HashMap<String, String> map = new HashMap<>();
						for (Map.Entry<String, Object> stringObjectEntry : jsonObject) {
							map.put(stringObjectEntry.getKey(), stringObjectEntry.getValue().toString());
						}
						return map;
					}).filter(stringStringHashMap -> !stringStringHashMap.isEmpty()).limit(30).collect(Collectors.toList());
		});
	}

  public Single<List<JsonObject>> searchFieldValues(Long companyId, String formType, String fieldId, String value) {
    JsonObject query = getSearchQueryForCustomFields(companyId, formType, fieldId, value);
    return MongoFactory.INSTANCE.findAllDistinctRecords(CUSTOM_FIELD_MAPPING, query, FIELD_VALUE, 50);
  }

  private static JsonObject getSearchQueryForCustomFields(Long companyId, String formType, String fieldId, String value) {
    JsonObject query = new JsonObject();
    query.put(COMPANY_ID, companyId);
    query.put(FORM_TYPE, formType);
    query.put(FIELD_ID, fieldId);
    JsonObject likeQuery = new JsonObject();
    likeQuery.put("$regex", ".*" + value + ".*").put("$options", "i");
    query.put(FIELD_VALUE, likeQuery);
    System.out.println("Fetch custom form Mongo Query : " + query.encode());
    return query;
  }

  public Single<JsonArray> searchFieldValuesV2(Long companyId, String formType, String fieldId, String value) {
    JsonObject query = getSearchQueryForCustomFields(companyId, formType, fieldId, value);
    return MongoFactory.INSTANCE.findAllDistinctRecords(CUSTOM_FIELD_MAPPING, query, FIELD_VALUE);
  }


  public Single<JsonArray> findAllDistinctRecords(String collection, JsonObject query, String distinctBy) {
    return Single.create(singleSubscriber -> {
      getMongoClient().distinctWithQuery(collection, distinctBy, String.class.getName(), query, event -> {
        if (event.succeeded()) {
          singleSubscriber.onSuccess(event.result());
        } else {
          singleSubscriber.onError(event.cause());
        }
      });
    });
  }

  public Single<List<JsonObject>> findAllDistinctRecords(String collection, JsonObject query, String distinctBy, int batchSize) {
    return Single.create(singleSubscriber -> {
      ReadStream<JsonObject> readStream = getMongoClient()
        .distinctBatchWithQuery(collection, distinctBy, String.class.getName(), query, batchSize);

      List<JsonObject> resultList = new ArrayList<>();

      readStream
        .handler(record -> {
          resultList.add(record);
          if (resultList.size() >= batchSize) {  // Stop fetching after batchSize is reached
            readStream.pause();  // Stop the stream
            singleSubscriber.onSuccess(resultList);  // Return the result
          }
        })
        .fetch(batchSize) // Ensure batch fetching
        .endHandler(v -> {
          if (!singleSubscriber.isUnsubscribed()) {
            singleSubscriber.onSuccess(resultList); // Return results if limit wasn't hit
          }
        })
        .exceptionHandler(singleSubscriber::onError);
    });
  }



  public ReadStream<JsonObject> findAllDistinctRecordsV3(String collection, JsonObject query, String distinctBy, int batchSize) {
    try {
      return getMongoClient()
        .distinctBatchWithQuery(collection, distinctBy, String.class.getName(), query, batchSize)
        .handler(json -> {
          System.out.println("V3 findAllDistinctRecords result :: "+ json.encode());
        });
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public ReadStream<JsonObject> findAllDistinctRecordsV3(Long companyId, String formType, String fieldId, String value) {
    JsonObject query = getSearchQueryForCustomFields(companyId, formType, fieldId, value);
    return findAllDistinctRecordsV3(CUSTOM_FIELD_MAPPING, query, FIELD_VALUE, 30);
  }

}
