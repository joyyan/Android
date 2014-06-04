package bupt.ygj.datacollector.util;

import java.util.List;

public class AddressGroupVO {

	private String groupname;
	private List<SortModelVO> list;
	
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public List<SortModelVO> getList() {
		return list;
	}
	public void setList(List<SortModelVO> list) {
		this.list = list;
	}
}
