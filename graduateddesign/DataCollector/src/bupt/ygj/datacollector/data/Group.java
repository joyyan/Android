package bupt.ygj.datacollector.data;

import java.util.List;
import java.util.Map;

public class Group {

	private String groupname;

	private String groupid;

	private List<RowVO> row;

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public List<RowVO> getRow() {
		return row;
	}

	public void setRow(List<RowVO> row) {
		this.row = row;
	}

	
	public void setAttributes(Map mapvalue) {

	}
}
