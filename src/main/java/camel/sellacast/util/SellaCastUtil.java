package camel.sellacast.util;


import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import camel.sellacast.exception.SellaCastException;
 

public class SellaCastUtil {

	static Logger logger = Logger.getLogger(SellaCastUtil.class);
	
	public static MongoClient getMongoInstance(String mangodb_uri, int port) throws SellaCastException {
		MongoClient client = null;
		try {
			client = new MongoClient(mangodb_uri, port);
		} catch (Exception e) {
			logger.error("################# EXCEPTION in creating Mongo instance", e);
			throw new SellaCastException(e.getMessage());
		}
		return client;
	}

	public static DBCollection getMongodbTable(String collectionName) throws UnknownHostException {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("SellaCast");
		String mongodb_uri = resourceBundle.getString("mongodb_uri");
		int mongodb_port = Integer.parseInt(resourceBundle.getString("mongodb_port"));
		String mongodb_name = resourceBundle.getString("mongodb_name");
		String mongodb_collection_name = resourceBundle.getString(collectionName);
		
		MongoClient mongoClient = new MongoClient(mongodb_uri, mongodb_port);
		DB db = mongoClient.getDB(mongodb_name);
		DBCollection table = db.getCollection(mongodb_collection_name);
		return table;
	}



}
