package camel.sellacast.util;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

public class DBTest {

	public static void main(String[] args) {

	    try {

		/**** Connect to MongoDB ****/
		// Since 2.10.0, uses MongoClient
		MongoClient mongo = new MongoClient("10.21.137.48", 27017);

		/**** Get database ****/
		// if database doesn't exists, MongoDB will create it for you
		DB db = mongo.getDB("SELLACAST_DB");

		/**** Get collection / table from 'testdb' ****/
		// if collection doesn't exists, MongoDB will create it for you
		DBCollection table = db.getCollection("mongodb_collection_name");

		/**** Insert ****/
		// create a document to store key and value
		/*BasicDBObject document = new BasicDBObject();
		document.put("name", "mkyong");
		document.put("age", 30);
		document.put("createdDate", new Date());
		table.insert(document);
		ObjectId id = (ObjectId)document.get( "_id" );
		System.out.println("id >> "+id);*/
		/**** Find and display ****/
		
		DBCollection dbCollection = SellaCastUtil.getMongodbTable("mongodb_collection_name");
		BasicDBObject newFeed = new BasicDBObject();
		newFeed.put("image", "");
		newFeed.put("idfeed", "7");
		newFeed.put("link", "https://www.finextra.com/blogposting/16143/new-technologies-create-new-opportunities-in-trade-finance-and-working-capital?utm_medium=rss&utm_source=finextrafeed");
		newFeed.put("description", "At Sibos 2014 in Boston blockchain was the talk of the show and the movement towards adoption of new..");
		newFeed.put("source", "https://www.finextra.com/rss/headlines.aspx");
		newFeed.put("startdate", "20181016");
		newFeed.put("likecount", "0");
		newFeed.put("unlikecount", "0");
		newFeed.put("enddate", "20191018");
		newFeed.put("publishedDate", "Tue, 16 Oct 2018 04:19:56 GMT");
		newFeed.put("categories", "BUSINESS");
		newFeed.put("headline", "New Technologies create new opportunities in trade finance and working capita");
		newFeed.put("retentiontime", "30");
		newFeed.put("audience", "TEAM1");
		newFeed.put("iscurated", false);
		/*dbCollection.insert(newFeed);
		ObjectId id =  (ObjectId) newFeed.get( "_id" );
		System.out.println("id >> feed "+id);*/
		
		String todayStr = DateUtil.formateDateToString(new Date(), "yyyyMMdd");
		System.out.println("today>> "+todayStr);
		BasicDBObject query = new BasicDBObject();
		
		
//		ObjectId ida = new ObjectId("5bf2a79c0489f32e58290c57");
//		System.out.println("id >> "+id);
//		query.put("idfeed", "7");
		query.put("categories", "BUSINESS");
		// 5bf2a79c0489f32e58290c57
		
		BasicDBObject updateQuery = new BasicDBObject();
		updateQuery.append("$set", 
			new BasicDBObject().append("enddate", "20191018"));
		
		
		/*BasicDBObject updateObj= new BasicDBObject();
		updateObj.put("enddate", "20191018");*/
		dbCollection.updateMulti(query, updateQuery);
		
		// query.put("startdate", new BasicDBObject("$lte", todayStr));
//		query.put("categories", "BUSINESS");
		
		
		//query.put("enddate", new BasicDBObject("$gte", todayStr));
		 
		DBCursor dbCursor = dbCollection.find(query);
		System.out.println(dbCursor.toArray());
		/*List<DBObject> results = dbCursor.toArray();
		Collection<JSONObject> jsonColl = new ArrayList<JSONObject>();
		System.out.println("###################### MONGODB USER Collection RECORD SIZE :" + results.size());
		for (int i = 0; i < results.size(); i++) {
			DBObject dbObject = (DBObject) results.get(i);
			JSONObject jsonObject = new JSONObject(dbObject.toString());
			jsonColl.add(jsonObject);
		}
		*/
//		DBCursor cursor = table.find(searchQuery);
/*System.out.println("response ?>>> ");
		while (dbCursor.hasNext()) {
			System.out.println("cursor >> "+dbCursor.next());
		}*/

		/**** Update ****/
		// search document where name="mkyong" and update it with new values
		/*BasicDBObject query = new BasicDBObject();
		query.put("name", "mkyong");

		BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("name", "mkyong-updated");

		BasicDBObject updateObj = new BasicDBObject();
		updateObj.put("$set", newDocument);

		table.update(query, updateObj);

		*//**** Find and display ****//*
		BasicDBObject searchQuery2 
		    = new BasicDBObject().append("name", "mkyong-updated");

		DBCursor cursor2 = table.find(searchQuery2);

		while (cursor2.hasNext()) {
			System.out.println(cursor2.next());
		}*/

		/**** Done ****/
		System.out.println("Done");

	    }  catch ( Exception e) {
		e.printStackTrace();
	    }

	  }
}
