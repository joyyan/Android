package bupt.ygj.datacollector.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class ElementGroupVO4Header extends ElementGroupVO {
	private String groupName = null;
	private String groupid = null;
	private List<String> relatedlist = null;

	public List<String> getRelatedlist() {
		return relatedlist;
	}

	public void setRelatedlist(List<String> relatedlist) {
		this.relatedlist = relatedlist;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getGroupid() {
		return groupid;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ElementGroupVO4Header loadVO(Map map) {
		ElementGroupVO4Header ret = new ElementGroupVO4Header();
		ret.groupid = ret.getMapStringValue(map, "groupid", null);
		ret.groupName = ret.getMapStringValue(map, "groupname", null);
		loadVO(ret, map);
		return ret;
	}

	@SuppressWarnings("rawtypes")
	public static List<ElementGroupVO4Header> loadVO(List<Map> list) {
		List<ElementGroupVO4Header> dataList = new ArrayList<ElementGroupVO4Header>();
		for (Map map : list) {
			ElementGroupVO4Header data = loadVO(map);
			dataList.add(data);
		}
		return dataList;
	}
}
