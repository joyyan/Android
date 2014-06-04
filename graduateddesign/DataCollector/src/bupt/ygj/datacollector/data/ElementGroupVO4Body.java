package bupt.ygj.datacollector.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class ElementGroupVO4Body extends ElementGroupVO {
	private String childdocid = null;

	public String getChilddocid() {
		return childdocid;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ElementGroupVO4Body loadVO(Map map) {
		ElementGroupVO4Body ret = new ElementGroupVO4Body();
		ret.childdocid = ret.getMapStringValue(map, "childdocid");
		loadVO(ret, map);
		return ret;
	}

	@SuppressWarnings("rawtypes")
	public static List<ElementGroupVO4Body> loadVO(List<Map> list) {
		List<ElementGroupVO4Body> dataList = new ArrayList<ElementGroupVO4Body>();
		for (Map map : list) {
			ElementGroupVO4Body data = loadVO(map);
			dataList.add(data);
		}
		return dataList;
	}
}
