package camel.sellacast.util;

import com.mongodb.BasicDBObject;

import camel.sellacast.json.FeedJSONObject;
import camel.sellacast.json.MongoPrimaryKey;

public class DBToFeedJSONObjectTransformer {

	public static FeedJSONObject convertDBObjectToFeedJSONObject(BasicDBObject mongoDbObject) {
		FeedJSONObject fjObject = new FeedJSONObject();
		MongoPrimaryKey key = new MongoPrimaryKey();
		key.set$oid(mongoDbObject.getString("_id"));
		fjObject.set_id(key);
		if (mongoDbObject.getString("audience") != null) {
			fjObject.setAudience(mongoDbObject.getString("audience").split(","));
		}
		fjObject.setCategories(mongoDbObject.getString("categories"));
		fjObject.setHeadline(mongoDbObject.getString("headline"));
		fjObject.setDescription(mongoDbObject.getString("description"));
		fjObject.setEnddate(mongoDbObject.getString("enddate"));
		fjObject.setIdfeed(mongoDbObject.getString("idfeed"));
		fjObject.setImage(mongoDbObject.getString("image"));
		fjObject.setIscurated(mongoDbObject.getBoolean("iscurated"));
		fjObject.setLikecount(mongoDbObject.getString("likecount"));
		fjObject.setLink(mongoDbObject.getString("link"));
		fjObject.setPublishedDate(mongoDbObject.getString("publishedDate"));
		fjObject.setRetentiontime(mongoDbObject.getString("retentiontime"));
		fjObject.setSource(mongoDbObject.getString("source"));
		fjObject.setStartdate(mongoDbObject.getString("startdate"));
		fjObject.setUnlikecount(mongoDbObject.getString("unlikecount"));
		return fjObject;
	}

}
