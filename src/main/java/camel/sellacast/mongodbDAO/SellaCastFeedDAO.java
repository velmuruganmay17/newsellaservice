package camel.sellacast.mongodbDAO;


import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import camel.sellacast.exception.SellaCastException;
import camel.sellacast.json.DeleteUserJSON;
import camel.sellacast.json.FeedJSONObject;
import camel.sellacast.json.KeyJSON;
import camel.sellacast.json.MongoPrimaryKey;
import camel.sellacast.json.UpdateUserStatusJSON;
import camel.sellacast.json.UserJSON;
import camel.sellacast.util.DBToFeedJSONObjectTransformer;
import camel.sellacast.util.DateUtil;
import camel.sellacast.util.SellaCastUtil;

public class SellaCastFeedDAO {

	static Logger logger = Logger.getLogger(SellaCastFeedDAO.class);

	public static String getMongoDBRecordId(String URL, String title, String idFeed) throws UnknownHostException {
		String recordId = null;

		DBCollection table = SellaCastUtil.getMongodbTable("mongodb_collection_name");

		BasicDBObject query = new BasicDBObject();
		if (idFeed != null) {
			query.put("idfeed", idFeed);
		} else {
			query.put("headline", title);
			query.put("link", URL);
		}

		DBCursor dbCursor = table.find(query);
		if (dbCursor.hasNext()) {
			DBObject dbObject = dbCursor.next();
			recordId = dbObject.get("_id").toString();
		}

		return recordId;
	}

	public static String getAliveFeeds(String feedType) throws UnknownHostException {
		DBCollection table = SellaCastUtil.getMongodbTable("mongodb_collection_name");
		String todayStr = DateUtil.formateDateToString(new Date(), "yyyyMMdd");
		BasicDBObject query = new BasicDBObject();
		//query.put("startdate", new BasicDBObject("$lte", todayStr));
		query.put("publishedDate", new BasicDBObject("$eq", todayStr));
		if (feedType != null) {
			query.put("categories", feedType);
		}

		DBCursor dbCursor = table.find(query);
		List<DBObject> results = dbCursor.toArray();
		Collection<JSONObject> jsonColl = new ArrayList<JSONObject>();
		logger.debug("###################### MONGODB RECORD SIZE :" + results.size());
		for (int i = 0; i < results.size(); i++) {
			DBObject dbObject = (DBObject) results.get(i);
			JSONObject jsonObject = new JSONObject(dbObject.toString());
			jsonColl.add(jsonObject);
		}
		return jsonColl.toString();
	}
	
	public static String getAllFeeds() throws UnknownHostException {
		DBCollection table = SellaCastUtil.getMongodbTable("mongodb_collection_name");
		DBCursor dbCursor = table.find();
		List<DBObject> results = dbCursor.toArray();
		Collection<JSONObject> jsonColl = new ArrayList<JSONObject>();
		logger.debug("###################### MONGODB RECORD SIZE :" + results.size());
		for (int i = 0; i < results.size(); i++) {
			DBObject dbObject = (DBObject) results.get(i);
			JSONObject jsonObject = new JSONObject(dbObject.toString());
			jsonColl.add(jsonObject);
		}
		return jsonColl.toString();
	}

	public static String searchFeeds(String searchString) throws UnknownHostException {

		BasicDBObject query = new BasicDBObject();

		BasicDBObject stringQuery = new BasicDBObject();
		List<BasicDBObject> stringSearchConditions = new ArrayList<BasicDBObject>();
		BasicDBObject headLineInput = new BasicDBObject();
		headLineInput.put("headline", new BasicDBObject("$regex", searchString).append("$options", "i"));
		stringSearchConditions.add(headLineInput);
		BasicDBObject descInput = new BasicDBObject();
		descInput.put("description", new BasicDBObject("$regex", searchString).append("$options", "i"));
		stringSearchConditions.add(descInput);
		stringQuery.put("$or", stringSearchConditions);

		List<BasicDBObject> allConditions = new ArrayList<BasicDBObject>();
		BasicDBObject dateQuery = new BasicDBObject();
		String todayStr = DateUtil.formateDateToString(new Date(), "yyyyMMdd");
		dateQuery.put("startdate", new BasicDBObject("$lte", todayStr));
		dateQuery.put("enddate", new BasicDBObject("$gte", todayStr));

		allConditions.add(stringQuery);
		allConditions.add(dateQuery);

		query.put("$and", allConditions);

		DBCollection mongodbTable = SellaCastUtil.getMongodbTable("mongodb_collection_name");
		DBCursor dbCursor = mongodbTable.find(query);
		List<DBObject> results = dbCursor.toArray();
		Collection<JSONObject> jsonColl = new ArrayList<JSONObject>();
		for (int i = 0; i < results.size(); i++) {
			DBObject dbObject = (DBObject) results.get(i);
			JSONObject jsonObject = new JSONObject(dbObject.toString());
			jsonColl.add(jsonObject);
		}
		return jsonColl.toString();
	}

	public static String searchInAllFeeds(String searchString) throws UnknownHostException {

		BasicDBObject query = new BasicDBObject();

		List<BasicDBObject> stringSearchConditions = new ArrayList<BasicDBObject>();
		BasicDBObject headLineInput = new BasicDBObject();
		headLineInput.put("headline", new BasicDBObject("$regex", searchString).append("$options", "i"));
		stringSearchConditions.add(headLineInput);
		BasicDBObject descInput = new BasicDBObject();
		descInput.put("description", new BasicDBObject("$regex", searchString).append("$options", "i"));
		stringSearchConditions.add(descInput);
		query.put("$or", stringSearchConditions);

		DBCollection mongodbTable = SellaCastUtil.getMongodbTable("mongodb_collection_name");
		DBCursor dbCursor = mongodbTable.find(query);
		List<DBObject> results = dbCursor.toArray();
		Collection<JSONObject> jsonColl = new ArrayList<JSONObject>();
		for (int i = 0; i < results.size(); i++) {
			DBObject dbObject = (DBObject) results.get(i);
			JSONObject jsonObject = new JSONObject(dbObject.toString());
			jsonColl.add(jsonObject);
		}
		return jsonColl.toString();
	}

	public static void updateCuratedStatus(List<FeedJSONObject> feedList) throws UnknownHostException {
		DBCollection table = SellaCastUtil.getMongodbTable("mongodb_collection_name");

		for (FeedJSONObject feedObject : feedList) {
			BasicDBObject update = new BasicDBObject();
			update.append("$set", new BasicDBObject().append("description", feedObject.getDescription())
					.append("headline", feedObject.getHeadline()));

			BasicDBObject query = new BasicDBObject();
			query.put("_id", new ObjectId(feedObject.get_id().get$oid()));
			table.update(query, update);
		}

	}

	public static JSONObject updateSellaCastFeedResponse(String strlikeCount, String strunlikeCount, String idRecord) throws UnknownHostException {
		
		int likeCountToAdd = strlikeCount != null && strlikeCount.trim().length() > 0 ? Integer.parseInt(strlikeCount) : 0;
		int unLikeCountToAdd = strunlikeCount != null && strunlikeCount.trim().length() > 0 ? Integer.parseInt(strunlikeCount) : 0;
		DBCollection table = SellaCastUtil.getMongodbTable("mongodb_collection_name");

		int newLikeCount = 0;
		int newUnLikeCount = 0;
		
		BasicDBObject query = new BasicDBObject();
		query.put("_id",new ObjectId(idRecord));
		DBCursor result = table.find(query);
		
		if(result.hasNext()) {
			DBObject dbObject = result.next();
			int existingLikeCount = Integer.parseInt(dbObject.get("likecount").toString());
			int existingUnLikeCount = Integer.parseInt(dbObject.get("unlikecount").toString());
			
			BasicDBObject update = new BasicDBObject();
			if(likeCountToAdd > 0 && unLikeCountToAdd > 0) {
				newLikeCount = existingLikeCount+likeCountToAdd;
				newUnLikeCount = existingUnLikeCount + unLikeCountToAdd;
				update.append("$set", new BasicDBObject().append("likecount", String.valueOf(newLikeCount))
						.append("unlikecount", String.valueOf(newUnLikeCount))
						);
				table.update(query, update);
			} else if(likeCountToAdd > 0) {
				newLikeCount = existingLikeCount+likeCountToAdd;
				newUnLikeCount = existingUnLikeCount;
				update.append("$set", new BasicDBObject().append("likecount", String.valueOf(newLikeCount)));
				table.update(query, update);
			} else if(unLikeCountToAdd > 0) {
				newUnLikeCount = existingUnLikeCount + unLikeCountToAdd;
				newLikeCount = existingLikeCount;
				update.append("$set", new BasicDBObject().append("unlikecount", String.valueOf(newUnLikeCount)));
				table.update(query, update);
			}
		}
		
		JSONObject countDetailsJson = new JSONObject();
		countDetailsJson.put("likecount", String.valueOf(newLikeCount));
		countDetailsJson.put("unlikecount", String.valueOf(newUnLikeCount));
		return countDetailsJson;
	}

	public static void archiveSellaCastFeeds(List<MongoPrimaryKey> list) throws UnknownHostException {
		DBCollection table = SellaCastUtil.getMongodbTable("mongodb_collection_name");
		BasicDBObject query = new BasicDBObject();

		BasicDBObject idQuery = new BasicDBObject();
		List<ObjectId> idList = new ArrayList<ObjectId>();
		for (MongoPrimaryKey primaryKey : list) {
			idList.add(new ObjectId(primaryKey.get$oid()));
		}
		idQuery.put("$in", idList);

		query.put("_id", idQuery);

		DBCursor dbCursor = table.find(query);
		List<DBObject> results = dbCursor.toArray();
		for (int i = 0; i < results.size(); i++) {
			DBObject dbObject = (DBObject) results.get(i);
			JSONObject jsonObject = new JSONObject(dbObject.toString());
			insertArchiveFeed(jsonObject);
			table.remove(dbObject);
		}

	}

	public static void archiveFeedsWithNegativeFeedback() throws UnknownHostException {
		String feeds = getAliveFeeds(null);
		JSONArray feedArray = new JSONArray(feeds);
		ResourceBundle resourceBundle = ResourceBundle.getBundle("SellaCast");
		List<MongoPrimaryKey> primaryKeys = new ArrayList<MongoPrimaryKey>();
		for (int i = 0; i < feedArray.length(); i++) {
			JSONObject feedObject = feedArray.getJSONObject(i);
			/*
			 * int likeCount =
			 * Integer.parseInt(feedObject.getString("likecount")); int
			 * likeCountLimit =
			 * Integer.parseInt(resourceBundle.getString("likefeedbacklimit"));
			 */ int unlikeCount = Integer.parseInt(feedObject.getString("unlikecount"));
			int unlikeCountLimit = Integer.parseInt(resourceBundle.getString("unlikefeedbacklimit"));

			// if(likeCount < likeCountLimit || unlikeCount >= unlikeCountLimit)
			// {
			if (unlikeCount >= unlikeCountLimit) {
				MongoPrimaryKey key = new MongoPrimaryKey();
				key.set$oid(feedObject.getJSONObject("_id").getString("$oid"));
				primaryKeys.add(key);
			}
		}
		if (primaryKeys.size() > 0) {
			archiveSellaCastFeeds(primaryKeys);
		}
	}
	
	public static List<FeedJSONObject> getFeedsBasedOnAttribute(String attribute, String value) throws UnknownHostException {
		BasicDBObject query = new BasicDBObject();

//		List<BasicDBObject> stringSearchConditions = new ArrayList<BasicDBObject>();
		query.put(attribute, new BasicDBObject("$regex", value).append("$options", "i"));
//		stringSearchConditions.add(query);

		DBCollection mongodbTable = SellaCastUtil.getMongodbTable("mongodb_collection_name");
		DBCursor dbCursor = mongodbTable.find(query);
		// Getting the iterator
		Iterator<DBObject> it = dbCursor.iterator();
		List<FeedJSONObject> feedList = new ArrayList<>();
		while (it.hasNext()) {
			feedList.add(DBToFeedJSONObjectTransformer.convertDBObjectToFeedJSONObject((BasicDBObject) it.next()));
		}
		return feedList;
	}

	private static void insertArchiveFeed(JSONObject jsonObject) throws UnknownHostException {
		DBCollection archiveTable = SellaCastUtil.getMongodbTable("mongodb_archive_collection_name");
		BasicDBObject newFeed = new BasicDBObject();
		newFeed.put("image", jsonObject.getString("image"));
		newFeed.put("idfeed", jsonObject.getString("idfeed"));
		newFeed.put("link", jsonObject.getString("link"));
		newFeed.put("description", jsonObject.getString("description"));
		newFeed.put("source", jsonObject.getString("source"));
		newFeed.put("startdate", jsonObject.getString("startdate"));
		newFeed.put("likecount", jsonObject.getString("likecount"));
		newFeed.put("unlikecount", jsonObject.getString("unlikecount"));
		newFeed.put("enddate", jsonObject.getString("enddate"));
		newFeed.put("publishedDate", jsonObject.getString("publishedDate"));
		newFeed.put("categories", jsonObject.getString("categories"));
		newFeed.put("headline", jsonObject.getString("headline"));
		newFeed.put("retentiontime", jsonObject.getString("retentiontime"));
		newFeed.put("iscurated", jsonObject.getBoolean("iscurated"));

		JSONArray jsonArray = jsonObject.getJSONArray("audience");

		String[] audienceArray = new String[jsonArray.length()];
		for (int i = 0; i < jsonArray.length(); i++) {
			audienceArray[i] = jsonArray.getString(i);
		}
		newFeed.put("audience", audienceArray);
		archiveTable.insert(newFeed);

	}

	public static String insertFeed(FeedJSONObject feedJSONObject) throws UnknownHostException {
		DBCollection dbCollection = SellaCastUtil.getMongodbTable("mongodb_collection_name");
		BasicDBObject newFeed = new BasicDBObject();
		newFeed.put("image", feedJSONObject.getImage());
		newFeed.put("idfeed", feedJSONObject.getIdfeed());
		newFeed.put("link", feedJSONObject.getLink());
		newFeed.put("description", feedJSONObject.getDescription());
		newFeed.put("source", feedJSONObject.getSource());
		newFeed.put("startdate", feedJSONObject.getStartdate());
		newFeed.put("likecount", feedJSONObject.getLikecount());
		newFeed.put("unlikecount", feedJSONObject.getUnlikecount());
		newFeed.put("enddate", feedJSONObject.getEnddate());
		newFeed.put("publishedDate", feedJSONObject.getPublishedDate());
		newFeed.put("categories", feedJSONObject.getCategories());
		newFeed.put("headline", feedJSONObject.getHeadline());
		newFeed.put("retentiontime", feedJSONObject.getRetentiontime());
		newFeed.put("audience", feedJSONObject.getAudience());
		newFeed.put("iscurated", feedJSONObject.isIscurated());
		dbCollection.insert(newFeed);
		return getFeed((ObjectId) newFeed.get("_id"));
	}
	
	public static String updateFeed(FeedJSONObject feedJSONObject) throws UnknownHostException {
		DBCollection dbCollection = SellaCastUtil.getMongodbTable("mongodb_collection_name");
		logger.debug("updateFeed"  );
		BasicDBObject query = new BasicDBObject();
		ObjectId id= new ObjectId(feedJSONObject.get_id().get$oid());
		query.put("_id", id);
		BasicDBObject updateFeed = new BasicDBObject();
		if(feedJSONObject.getImage()!=null) {
			updateFeed.put("image", feedJSONObject.getImage());
		}
		if(feedJSONObject.getIdfeed()!=null) {
		updateFeed.put("idfeed", feedJSONObject.getIdfeed());
		}
		if(feedJSONObject.getLink()!=null) {
		updateFeed.put("link", feedJSONObject.getLink()); }
		if(feedJSONObject.getDescription()!=null) {
		updateFeed.put("description", feedJSONObject.getDescription());
		}
		if(feedJSONObject.getSource()!=null) {
		updateFeed.put("source", feedJSONObject.getSource());
		}
		if(feedJSONObject.getStartdate()!=null) {
		updateFeed.put("startdate", feedJSONObject.getStartdate());
		}
		if(feedJSONObject.getLikecount()!=null) {
		updateFeed.put("likecount", feedJSONObject.getLikecount());
		}
		if(feedJSONObject.getUnlikecount()!=null) {
		updateFeed.put("unlikecount", feedJSONObject.getUnlikecount());
		}
		if(feedJSONObject.getEnddate()!=null) {
		updateFeed.put("enddate", feedJSONObject.getEnddate());
		}
		if(feedJSONObject.getPublishedDate()!=null) {
		updateFeed.put("publishedDate", feedJSONObject.getPublishedDate());
		}
		if(feedJSONObject.getCategories()!=null) {
		updateFeed.put("categories", feedJSONObject.getCategories());
		}
		if(feedJSONObject.getHeadline()!=null) {
		updateFeed.put("headline", feedJSONObject.getHeadline());
		}
		if(feedJSONObject.getRetentiontime()!=null) {
		updateFeed.put("retentiontime", feedJSONObject.getRetentiontime());
		}
		if(feedJSONObject.getAudience()!=null) {
		updateFeed.put("audience", feedJSONObject.getAudience());
		}
		if(feedJSONObject.isIscurated()) {
			updateFeed.put("iscurated", feedJSONObject.isIscurated());
		}
		dbCollection.update(query, updateFeed);
		return getFeed(id);
	}
	
	public static String getFeed(final ObjectId _id) throws UnknownHostException {
		DBCollection table = SellaCastUtil.getMongodbTable("mongodb_collection_name"); 
		BasicDBObject query = new BasicDBObject();
		query.put("_id", _id);  
		DBCursor dbCursor = table.find(query);
		List<DBObject> results = dbCursor.toArray();
		Collection<JSONObject> jsonColl = new ArrayList<JSONObject>();
		logger.debug("###################### MONGODB RECORD SIZE :" + results.size());
		for (int i = 0; i < results.size(); i++) {
			DBObject dbObject = (DBObject) results.get(i);
			JSONObject jsonObject = new JSONObject(dbObject.toString());
			jsonColl.add(jsonObject);
		}
		return jsonColl.toString();
	}
	
	public static String inserNewUser(UserJSON userJSON) throws UnknownHostException {
		DBCollection dbCollection = SellaCastUtil.getMongodbTable("mongodb_user_collection");
		String todayStr = DateUtil.formateDateToString(new Date(), "yyyyMMdd");
		BasicDBObject newFeed = new BasicDBObject();
		newFeed.put("gbsId", userJSON.getGbsId());
		newFeed.put("name", userJSON.getName());
		newFeed.put("email", userJSON.getEmail());
		newFeed.put("phone", userJSON.getPhone());
		newFeed.put("password", userJSON.getPassword());
		newFeed.put("role", userJSON.getRole());
		newFeed.put("active", userJSON.isActive());  
		newFeed.put("creationDate", todayStr);   
		dbCollection.insert(newFeed);
		ObjectId id =  (ObjectId) newFeed.get( "_id" );
		return getUser(id);
	}
	
	
	public static String getUser(ObjectId _id) throws UnknownHostException {
		DBCollection table = SellaCastUtil.getMongodbTable("mongodb_user_collection"); 
		BasicDBObject query = new BasicDBObject();
		query.put("_id", _id);  
		DBCursor dbCursor = table.find(query);
		List<DBObject> results = dbCursor.toArray();
		Collection<JSONObject> jsonColl = new ArrayList<JSONObject>();
		logger.debug("###################### MONGODB RECORD SIZE :" + results.size());
		for (int i = 0; i < results.size(); i++) {
			DBObject dbObject = (DBObject) results.get(i);
			JSONObject jsonObject = new JSONObject(dbObject.toString());
			jsonColl.add(jsonObject);
		}
		return jsonColl.toString();
	}
	
	public static String fetchAlltUsers() throws UnknownHostException {

		DBCollection table = SellaCastUtil.getMongodbTable("mongodb_user_collection");

		String todayStr = DateUtil.formateDateToString(new Date(), "yyyyMMdd");
		BasicDBObject query = new BasicDBObject();
		query.put("creationDate", new BasicDBObject("$lte", todayStr)); 
		 
		DBCursor dbCursor = table.find(query);
		List<DBObject> results = dbCursor.toArray();
		Collection<JSONObject> jsonColl = new ArrayList<JSONObject>();
		logger.debug("###################### MONGODB USER Collection RECORD SIZE :" + results.size());
		for (int i = 0; i < results.size(); i++) {
			DBObject dbObject = (DBObject) results.get(i);
			JSONObject jsonObject = new JSONObject(dbObject.toString());
			jsonColl.add(jsonObject);
		}
		return jsonColl.toString();
	}
	
	public static void updateUserStatus(UpdateUserStatusJSON updateUserStatusJSON) throws UnknownHostException { 
		String todayStr = DateUtil.formateDateToString(new Date(), "yyyyMMdd");
		DBCollection table = SellaCastUtil.getMongodbTable("mongodb_user_collection");
		BasicDBObject update = new BasicDBObject();
		update.append("$set", new BasicDBObject().append("active", updateUserStatusJSON.isActive())
				.append("finalDate", ((updateUserStatusJSON.isActive())?todayStr:"")) );

		BasicDBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(updateUserStatusJSON.get_id().get$oid()));
		table.update(query, update);
	}
	
	public static void deleteUser(final DeleteUserJSON deleteUserJSON ) throws UnknownHostException,SellaCastException { 
		DBCollection table = SellaCastUtil.getMongodbTable("mongodb_user_collection");
		BasicDBObject query = new BasicDBObject();
		if(deleteUserJSON!=null && deleteUserJSON.get_id()!=null ){
			logger.debug("delete by id");
			query.put("_id", new ObjectId(deleteUserJSON.get_id().get$oid()));
		}
		if(deleteUserJSON!=null && deleteUserJSON.getGbsId()!=null ){
			logger.debug("delete by gbsid");
			query.put("gbsId", deleteUserJSON.getGbsId());
		}
		DBCursor dbCursor = table.find(query);
		List<DBObject> results = dbCursor.toArray();
		if(results!=null && !results.isEmpty()) {
			table.remove(query);
		} else {
			throw new SellaCastException("user not found");
		}
		
	}
	
	public static void deleteFeed(final KeyJSON feedJSON ) throws UnknownHostException,SellaCastException { 
		DBCollection table = SellaCastUtil.getMongodbTable("mongodb_collection_name");
		BasicDBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(feedJSON.get_id().get$oid()));
		DBCursor dbCursor = table.find(query);
		List<DBObject> results = dbCursor.toArray();
		if(results!=null && !results.isEmpty()) {
			table.remove(query);
		} else {
			throw new SellaCastException("feed not found");
		}
	}
	
	
	public static void main(String[] args) throws UnknownHostException {
		/*
		 * String jsonFeeds =
		 * "[{\"image\":\"\",\"audience\":[\"BUSINESS\",\"TECHNOLOGY\"],\"idfeed\":\"0\",\"link\":\"http://www.fintech.finance/01-news/benelux-organizations-sign-"+
		 * "memorandum-to-strengthen-further-collaboration-in-blockchain-within-the-region/\",\"description\":\"Three organizations &#8211;Â B-Hive, theÂ Luxembourg "
		 * +
		 * "House of Financial TechnologyÂ (LHoFT) and theÂ Dutch Blockchain CoalitionÂ &#8211; today signed a Memorandum of Understanding (MoU) that will allow "
		 * +
		 * "them to leverage their significant collaboration potential for mutual benefit around the topic of distributed ledger technology, otherwise known as "
		 * +
		 * "blockchain, and allows them to recognize each other\u2019s contribution towards this collaboration.Â The MoU [&#8230;]\",\"source\":\"http://www.fintech."
		 * +
		 * "finance/feed\",\"startdate\":\"20180221\",\"clapcount\":\"0\",\"enddate\":\"20180223\",\"_id\":{\"$oid\":\"5a8e649be4b09f411c761709\"},"+
		 * "\"publishedDate\":\"Wed, 21 Feb 2018 14:41:56 +0000\",\"categories\":\"FINTECH_FEED\",\"headline\":\"Benelux organizations sign memorandum to strengthen "
		 * +
		 * "further collaboration in blockchain within the region\",\"retentiontime\":\"30\"}, {\"image\":\"\",\"audience\":[\"BUSINESS\"],\"idfeed\":\"0\",\"link\":"
		 * +
		 * "\"http://www.fintech.finance/01-news/internet-and-tech-giants-not-banks-are-the-darlings-of-fintech-experts-says-globaldata/\",\"description\":\"In Q4, "
		 * +
		 * "2017, Google has emerged as the most frequently mentioned company amongst key FinTech influencers in all but one key category, according toÂ GlobalData, "
		 * +
		 * "a leading data and analytics company. &#160; According toÂ GlobalData\u2019s FinTech Influencer platform, which tracks and categorises all content shared "
		 * +
		 * "by the most influential FinTech thought leaders each day on Twitter, Google [&#8230;]\",\"source\":\"http://www.fintech.finance/feed\",\"startdate\":\"201"
		 * +
		 * "80221\",\"clapcount\":\"0\",\"enddate\":\"20180223\",\"_id\":{\"$oid\":\"5a8e649be4b09f411c76170a\"},\"publishedDate\":\"Wed, 21 Feb 2018 13:07:32 +00"
		 * +
		 * "00\",\"categories\":\"FINTECH_FEED\",\"headline\":\"Internet and tech giants, not banks, are the darlings of FinTech experts, says GlobalData\",\"reten"
		 * + "tiontime\":\"30\"}]";
		 */ 
	}



	
}