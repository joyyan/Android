package bupt.ygj.datacollector.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * 自由表单中元素分组的VO
 * bupt.ygj.datacollector.data.ElementGroupVO
 * @author YanGJ create at 2014年6月4日 上午11:10:13
 */
@SuppressWarnings("serial")
public class ElementGroupVO extends ValueObject implements Serializable {

	private List<ElementDataVO> elements = null;

	public List<ElementDataVO> getElements() {
		return elements;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static ElementGroupVO loadVO(ElementGroupVO elementGroupVO, Map map) {
		List<Map> elements = (List<Map>)map.get("itemattribute");
		if (elements != null) {
			elementGroupVO.elements = ElementDataVO.loadVO(elements);
		}
		return elementGroupVO;
	}
	
//	@SuppressWarnings("rawtypes")
//	public static List<ElementGroupVO> loadVO(List<Map> list) {
//		List<ElementGroupVO> dataList = new ArrayList<ElementGroupVO>();
//		for (Map map : list) {
//			ElementGroupVO data = loadVO(map);
//			dataList.add(data);
//		}
//		return dataList;
//	}
}
