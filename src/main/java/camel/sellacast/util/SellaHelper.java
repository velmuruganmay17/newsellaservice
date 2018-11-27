package camel.sellacast.util;

import org.json.JSONObject;

public class SellaHelper {
	public static  String getErrorMessage(final String msg) {
		JSONObject response=new JSONObject();
		response.put("response", "KO");
		if(msg!=null) {
			response.put("message", msg);
		}
		return response.toString();
	}
	public static  String getSuccessMessage(String msg) {
		StringBuilder response = new StringBuilder("");
		response.append("{\"response\":\"OK\"");
		if(msg!=null) {
			response.append(",\"responseData\":"+msg);
		}
		response.append("}");
		return response.toString();
	}

}
