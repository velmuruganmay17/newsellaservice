package camel.sellacast.json;

public class SearchJSON {

	private String searchstring;
	private boolean isToSearchAllFeeds = false;
	
	public String getSearchstring() {
		return searchstring;
	}

	public void setSearchstring(String searchstring) {
		this.searchstring = searchstring;
	}

	public boolean isToSearchAllFeeds() {
		return isToSearchAllFeeds;
	}

	public void setToSearchAllFeeds(boolean isToSearchAllFeeds) {
		this.isToSearchAllFeeds = isToSearchAllFeeds;
	}
	
}
