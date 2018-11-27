package camel.sellacast.json;

public class FeedTypeJSON {

	private String feedtypename;
	private String feedtypedesc;
	private int retentiontime;
	private int feedlifeindays;
	
	private MongoPrimaryKey _id;
	
	public String getFeedtypename() {
		return feedtypename;
	}
	public void setFeedtypename(String feedtypename) {
		this.feedtypename = feedtypename;
	}
	public String getFeedtypedesc() {
		return feedtypedesc;
	}
	public void setFeedtypedesc(String feedtypedesc) {
		this.feedtypedesc = feedtypedesc;
	}
	public int getRetentiontime() {
		return retentiontime;
	}
	public void setRetentiontime(int retentiontime) {
		this.retentiontime = retentiontime;
	}
	public MongoPrimaryKey get_id() {
		return _id;
	}
	public void set_id(MongoPrimaryKey _id) {
		this._id = _id;
	}
	public int getFeedlifeindays() {
		return feedlifeindays;
	}
	public void setFeedlifeindays(int feedlifeindays) {
		this.feedlifeindays = feedlifeindays;
	}
}
