package camel.sellacast.json;

public class UpdateClapCountJson {

	private String likecount;
	private String unlikecount;
	private MongoPrimaryKey _id;
	
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
}
