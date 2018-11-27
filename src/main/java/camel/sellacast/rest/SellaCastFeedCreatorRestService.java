package camel.sellacast.rest;


import java.net.UnknownHostException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import camel.sellacast.exception.SellaCastException;
import camel.sellacast.json.DeleteUserJSON;
import camel.sellacast.json.FeedJSONObject;
import camel.sellacast.json.KeyJSON;
import camel.sellacast.json.MongoPrimaryKey;
import camel.sellacast.json.SearchJSON;
import camel.sellacast.json.UpdateClapCountJson;
import camel.sellacast.json.UpdateUserStatusJSON;
import camel.sellacast.json.UserJSON;
//import camel.sellacast.json.UpdateClapCountJson;
import camel.sellacast.mongodbDAO.SellaCastFeedDAO;
//import camel.sellacast.mongodbDAO.SellaCastTwitterFeedDAO;

@Path("/sellacastrestservice")
public class SellaCastFeedCreatorRestService {

	static Logger logger = Logger.getLogger(SellaCastFeedCreatorRestService.class);
	
	// services to receive/consume feeds and store in mongo db.
	
	/*@GET 
	@Path("/processRssNormalFeed")
	@Produces(MediaType.TEXT_PLAIN)
	
	public Response processRSSNormalFeedUrl(@QueryParam("url") String url) {

		String output = CamelHttpRssFeedConsumer.processRss403FeedURL(url,"FINTECH");
		return processResponse(output);

	}*/

	/*@GET 
	@Path("/processTechFeed")
	@Produces(MediaType.TEXT_PLAIN)
	
	public Response processTechFeedUrl(@QueryParam("url") String url) {

		String output = CamelHttpRssFeedConsumer.processRss403FeedURL(url,"TECH_FORUM");
		return processResponse(output);

	}*/

	private Response processResponse(String output) {
		if("OK".equals(output)) {
			return Response.status(200).entity(output).build();
		} else {
			return Response.status(404).entity(output).build();
		}
	}

	
	@POST 
	@Path("/insertUser")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertUser(UserJSON userJSON) {
		try {
			String response  = SellaCastFeedDAO.inserNewUser(userJSON);
			return getResponseObject(getSuccessMessage(response),200);
		} catch (UnknownHostException e) {
			logger.debug(e);
			return getResponseObject(getErrorMessage(e.getMessage()), 404);
		}
		
	}
	private String getErrorMessage(final String msg) {
		JSONObject response=new JSONObject();
		response.put("response", "KO");
		if(msg!=null) {
			response.put("message", msg);
		}
		return response.toString();
	}
	private String getSuccessMessage(String msg) {
		StringBuilder response = new StringBuilder("");
		response.append("{\"response\":\"OK\"");
		if(msg!=null) {
			response.append(",\"responseData\":"+msg);
		}
		response.append("}");
		logger.debug("response : "+response);
		return response.toString();
	}
	@GET
	@Path("/getAllUser")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response fetchAlltUsers() {
		try {
			logger.debug("request comes ");
			String users = SellaCastFeedDAO.fetchAlltUsers();
			return getResponseObject(users, 200);
		} catch (UnknownHostException e) {
			logger.debug(e);
			return getResponseObject(getErrorMessage(e.getMessage()), 404);
		}
	}
	
	@POST 
	@Path("/updateUserStatus")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUserStatus(UpdateUserStatusJSON updateUserStatusJSON ) {
		try {
			SellaCastFeedDAO.updateUserStatus(updateUserStatusJSON);
			return getResponseObject(getSuccessMessage(null),200);
		} catch (UnknownHostException e) {
			logger.debug(e);
			return getResponseObject(getErrorMessage(e.getMessage()), 404);
		}
	}
	
	//delete user 
	@POST 
	@Path("/deleteUser")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteUser(DeleteUserJSON deleteUserJSON ) {
		try {
			SellaCastFeedDAO.deleteUser(deleteUserJSON);
			return getResponseObject(getSuccessMessage(null),200);
		} catch (UnknownHostException | SellaCastException e) {
			logger.debug(e);
			return getResponseObject(getErrorMessage(e.getMessage()), 404);
		}
	}
	
	@POST 
	@Path("/deleteFeed")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteFeed(KeyJSON feedKey) {
		try {
			SellaCastFeedDAO.deleteFeed(feedKey);
			return getResponseObject(getSuccessMessage(null),200);
		} catch (UnknownHostException | SellaCastException e) {
			logger.debug(e);
			return getResponseObject(getErrorMessage(e.getMessage()), 404);
		}
	}
	
	// services to get feeds, update feeds for external use
	// to fetch all feeds
	@GET 
	@Path("/fetchAllFeeds")
	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
	public Response fetchAllFeeds() {
		try {
			String allFeeds = SellaCastFeedDAO.getAllFeeds();
			return getResponseObject(allFeeds, 200);
		} catch (UnknownHostException e) {
			logger.debug(e);
			return getResponseObject("KO:"+e.getMessage(), 404);
		}
		
	}
	
	@GET 
	@Path("/fetchAliveFeeds")
	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
	public Response findAliveFeeds() {

		try {
			String liveFeeds = SellaCastFeedDAO.getAliveFeeds(null);
			return getResponseObject(liveFeeds, 200);
		} catch (UnknownHostException e) {
			logger.debug(e);
			return getResponseObject("KO:"+e.getMessage(), 404);
		}
		
	}

	@POST
	@Path("/searchFeeds")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response searchFeeds(SearchJSON searchJSON) {

		try {
			String liveFeeds = null;
			if(searchJSON.isToSearchAllFeeds()) {
				liveFeeds = SellaCastFeedDAO.searchInAllFeeds(searchJSON.getSearchstring());
			} else {
				liveFeeds = SellaCastFeedDAO.searchFeeds(searchJSON.getSearchstring());
			}
			return getResponseObject(liveFeeds, 200);
		} catch (UnknownHostException e) {
			logger.debug(e);
			return getResponseObject("KO:"+e.getMessage(), 404);
		}
		
	}

	@POST 
	@Path("/updateSellaCastFeedResponse")
	@Consumes("application/json")
	@Produces(MediaType.TEXT_PLAIN)
	public Response updateSellaCastFeedResponse(List<UpdateClapCountJson> list) {

		try {
			JSONArray jsonArray = new JSONArray();
			for (UpdateClapCountJson inputJson : list) {
				JSONObject countDetails = SellaCastFeedDAO.updateSellaCastFeedResponse(inputJson.getLikecount(), 
						inputJson.getUnlikecount(), inputJson.get_id().get$oid());
				jsonArray.put(countDetails);
			}
			return getResponseObject(jsonArray.toString(), 200);
		} catch (UnknownHostException e) {
			logger.debug(e);
			return getResponseObject("KO", 404);
		}
		
	}

	@POST 
	@Path("/archivefeeds")
	@Consumes("application/json")
	@Produces(MediaType.TEXT_PLAIN)
	public Response archiveSellaCastFeeds(List<MongoPrimaryKey> list) {

		try {
			SellaCastFeedDAO.archiveSellaCastFeeds(list);
			return getResponseObject("OK", 200);
		} catch (UnknownHostException e) {
			logger.debug(e);
			return getResponseObject("KO", 404);
		}
		
	}

	@POST 
	@Path("/archivebatch")
	@Produces(MediaType.TEXT_PLAIN)
	public Response archiveFeedsBasedOnLikeUnlikeCount() {

		try {
			SellaCastFeedDAO.archiveFeedsWithNegativeFeedback();
			return getResponseObject("OK", 200);
		} catch (UnknownHostException e) {
			logger.debug(e);
			return getResponseObject("KO", 404);
		}
		
	}

	@POST 
	@Path("/updatedCuratedFeed")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response updateCuratedFeed(List<FeedJSONObject> feedList) {

		try {
			SellaCastFeedDAO.updateCuratedStatus(feedList);
			return getResponseObject("OK", 200);
		} catch (UnknownHostException e) {
			logger.debug(e);
			return getResponseObject("KO", 404);
		}
		
	}

	@POST 
	@Path("/insertnewfeed")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response insertNewFeed(FeedJSONObject feedJSONObject) {
		try {
			final String feed =SellaCastFeedDAO.insertFeed(feedJSONObject);
			return getResponseObject(getSuccessMessage(feed),200);
		} catch (UnknownHostException e) {
			logger.debug(e);
			return getResponseObject("KO",500);
		}
		}
	
	@POST 
	@Path("/updatefeed")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateFeed(FeedJSONObject feedJSONObject) {
		try {
			final String feed =SellaCastFeedDAO.updateFeed(feedJSONObject);
			return getResponseObject(getSuccessMessage(feed),200);
		} catch (UnknownHostException e) {
			logger.debug(e);
			return getResponseObject("KO",500);
		}
		}
	
	 

	private Response getResponseObject(String message, int responseCode) {
		return Response.status(responseCode).entity(message)
				/*.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS")
				.header("Access-Control-Allow-Headers", "*")*/
				.build();

	}
}