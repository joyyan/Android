package bupt.ygj.datacollector.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 自由表单带出项的VO
 * bupt.ygj.datacollector.data.RelatedItemVO
 * @author YanGJ create at 2014年6月4日 上午11:11:15
 */
@SuppressWarnings("serial")
public class RelatedItemVO extends ValueObject implements Serializable{
	
	private String relatedItemKey = null;
	private String relatedPathString = null;
	
	
	public String getRelatedItemKey() {
		return relatedItemKey;
	}


	public String getRelatedPathString() {
		return relatedPathString;
	}


	public static RelatedItemVO loadVO(Map<String, String> map) {
		RelatedItemVO ret = new RelatedItemVO();
		ret.relatedItemKey = ret.getMapStringValue(map, "relateditemkey");
		ret.relatedPathString = ret.getMapStringValue(map, "relatedpathstring");
		return ret;
	}
	
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<RelatedItemVO> loadVO(List<Map<String, String>> mapList) {
		List<RelatedItemVO> retList = new ArrayList<RelatedItemVO>();
		for(Map map: mapList) {
			RelatedItemVO item = loadVO(map);
			retList.add(item);
		}
		return retList;
	}
	
	@SuppressWarnings("rawtypes")
	public void setAttributes(Map mapvalue) {
		if(null != mapvalue) {
			this.relatedItemKey = (String)mapvalue.get("key");
			this.relatedPathString = (String)mapvalue.get("path");
		}
	}
}
