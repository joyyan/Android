package bupt.ygj.datacollector.data;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模板初始化数据VO
 * 
 * @author xubob
 *
 */
public class InitTemplateDataVO extends ValueObject implements Serializable{ 
	private Map<String, String> headerItemValueList = null;
	
	private Map<String, Map<String, String>> bodyInfoItemValueList = null;
	
	/**
	 * 获取一个用于header的默认值
	 * 
	 * @param key	数据项key
	 * @return	
	 */
	public String getHeaderItemValue(String key) {
		return headerItemValueList == null? null : headerItemValueList.get(key);
	}
	
	/**
	 * 获取一个用于body的默认值
	 * 
	 * @param childdocid 子表ID
	 * @param key	数据项key
	 * @return
	 */
	public String getBodyItemValue(String childdocid, String key) {
		if (bodyInfoItemValueList == null) {
			return null;
		} else {
			Map<String, String> map = bodyInfoItemValueList.get(childdocid);
			return map == null ? null : map.get(key);
		}
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static InitTemplateDataVO loadVO(Map map) {
		InitTemplateDataVO ret = new InitTemplateDataVO();
		
		List<Map<String, String>> headerList = (List<Map<String, String>>)map.get("itemvalue");
		ret.headerItemValueList = list2Map(ret, headerList);
		
		List<Map> bodyList = (List<Map>)map.get("binfoinitvalue");
		ret.bodyInfoItemValueList = new HashMap<String, Map<String, String>>();
		if (bodyList != null) {
			for (Map bodyData : bodyList) {
				String childId = ret.getMapStringValue(bodyData, "childdocid");
				List<Map<String, String>> bodyItems = (List<Map<String, String>> )map.get("itemvalue");
				Map<String, String> bodyItemMap = list2Map(ret, bodyItems);
				ret.bodyInfoItemValueList.put(childId, bodyItemMap);
			}
		}
		return ret;
	}
	
	/**
	 * 将一个List<Map<String, String>的结构转换成Map<String, String>
	 * 
	 * @param obj
	 * @param mapList
	 * @return
	 */
	private static HashMap<String, String> list2Map(ValueObject obj, List<Map<String, String>> mapList) {
		if (mapList == null) {
			return null;
		} else {
			HashMap<String, String> ret = new HashMap<String, String>();
			for (Map<String, String> map: mapList) {
				String itemKey = obj.getMapStringValue(map, "itemkey");
				String itemValue = obj.getMapStringValue(map, "realvalue");
				ret.put(itemKey, itemValue);
			}
			return ret;
		}
	}
}
