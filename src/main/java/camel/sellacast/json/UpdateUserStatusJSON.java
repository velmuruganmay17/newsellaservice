package camel.sellacast.json;

public class UpdateUserStatusJSON {
	private MongoPrimaryKey _id;
	private boolean active;
	
	public MongoPrimaryKey get_id() {
		return _id;
	}
	public void set_id(MongoPrimaryKey _id) {
		this._id = _id;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
}
