package camel.sellacast.json;


public class FeedJSONObject {

	private String image;
	private String[] audience;
	private String idfeed;
	private String link;
	private String description;
	private String source;
	private String startdate;
	private String likecount;
	private String enddate;
	private String unlikecount;
	private MongoPrimaryKey _id;
	private String publishedDate;
	private String categories;
	private String headline;
	private String retentiontime;
	private boolean iscurated;
	
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getIdfeed() {
		return idfeed;
	}
	public void setIdfeed(String idfeed) {
		this.idfeed = idfeed;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getPublishedDate() {
		return publishedDate;
	}
	public void setPublishedDate(String publishedDate) {
		this.publishedDate = publishedDate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getCategories() {
		return categories;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}
	public String getHeadline() {
		return headline;
	}
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	public String getRetentiontime() {
		return retentiontime;
	}
	public void setRetentiontime(String retentiontime) {
		this.retentiontime = retentiontime;
	}
	public String[] getAudience() {
		return audience;
	}
	public void setAudience(String[] audience) {
		this.audience = audience;
	}
	public MongoPrimaryKey get_id() {
		return _id;
	}
	public void set_id(MongoPrimaryKey _id) {
		this._id = _id;
	}
	public String getLikecount() {
		return likecount;
	}
	public void setLikecount(String likecount) {
		this.likecount = likecount;
	}
	public String getUnlikecount() {
		return unlikecount;
	}
	public void setUnlikecount(String unlikecount) {
		this.unlikecount = unlikecount;
	}
	public boolean isIscurated() {
		return iscurated;
	}
	public void setIscurated(boolean iscurated) {
		this.iscurated = iscurated;
	}
	
}
