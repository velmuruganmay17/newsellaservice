package com.sella.cast.util;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.sella.cast.exception.SellaCastException;

public class SellaCastUtil {


	static Logger logger = Logger.getLogger(SellaCastUtil.class);
	
	public static MongoClient getMongoInstance(String mangodb_uri, int port)
	{
		MongoClient client = null;
		client = new com.mongodb.MongoClient(mangodb_uri, port);
		return client;
	}

	public static DBCollection getMongodbTable(String collectionName) throws UnknownHostException {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("resources/SellaCast");
		String mongodb_uri = resourceBundle.getString("mongodb_uri");
		int mongodb_port = Integer.parseInt(resourceBundle.getString("mongodb_port"));
		String mongodb_name = resourceBundle.getString("mongodb_name");
		String mongodb_collection_name = resourceBundle.getString(collectionName);
		
		System.out.println("mongodb_uri : "+mongodb_uri +"\n mongodb_port:"+mongodb_port+"\n mongodb_name:"+mongodb_name);
		
		MongoClient mongoClient = new MongoClient(mongodb_uri, mongodb_port);
		DB db = mongoClient.getDB(mongodb_name);
		DBCollection table = db.getCollection(mongodb_collection_name);
		return table;
	}

	/*public static String[] getCompatibleChannels(String feedType) {
		List<String> channelList = new ArrayList<String>();
		try {
			String channels = CommonDAO.getDBRecords("mongodb_channel_collection_name");
			JSONArray jsonArray = new JSONArray(channels);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject channelJSON = jsonArray.getJSONObject(i);
				JSONArray feedTypeJSONArray = channelJSON.getJSONArray("feedtypesassociated");
				if (feedTypeJSONArray.toString().contains(feedType)) {
					channelList.add(channelJSON.getString("channelname"));
				}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String [] channelArray = new String[channelList.size()];
		channelArray = channelList.toArray(channelArray);
		return channelArray;
	}*/

}
