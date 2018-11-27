package camel.sellacast.json;

public class DeleteUserJSON {
	private MongoPrimaryKey _id;
	private String gbsId;
	public MongoPrimaryKey get_id() {
		return _id;
	}
	public void set_id(MongoPrimaryKey _id) {
		this._id = _id;
	}
	public String getGbsId() {
		return gbsId;
	}
	public void setGbsId(String gbsId) {
		this.gbsId = gbsId;
	}
}
