package bupt.ygj.datacollector.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 在CommonFormDataVO中用到，用于表示枚举项的VO
 * bupt.ygj.datacollector.data.EnumValueVO
 * @author YanGJ create at 2014年6月4日 上午11:11:56
 */
@SuppressWarnings("serial")
public class EnumValueVO extends ValueObject implements Serializable {

	private String value = null;
	private String text = null;
	private boolean isselect = false;
	
	public boolean isIsselect() {
		return isselect;
	}

	public void setIsselect(boolean isselect) {
		this.isselect = isselect;
	}

	/**
	 * 获取枚举项的值
	 * 
	 * @return
	 */
	public String getValue(){
		return value;
	}
	
	/**
	 * 获取枚举项的显示内容
	 * 
	 * @return
	 */
	public String getText(){
		return text;
	}
	
	
	/**
	 * 从Map中解析CommonFormEnumValueVO
	 * 
	 * @param map
	 * @return
	 */
	public static EnumValueVO loadVO(Map<String, String> map) {
		EnumValueVO ret = new EnumValueVO();
		ret.value = ret.getMapStringValue(map, "realval"); //   enumrealvalue
		ret.text = ret.getMapStringValue(map, "diplayval");	//enumdiplayvalue
		return ret;
	}
	
	/**
	 * 从List<Map>中解析CommonFormEnumValueVO
	 * 
	 * @param mapList
	 * @return
	 */
	public static List<EnumValueVO> loadVO(List<Map<String, String>> mapList) {
		List<EnumValueVO> ret = new ArrayList<EnumValueVO>();
		for (Map<String, String> map : mapList) {
			EnumValueVO vo = EnumValueVO.loadVO(map);
			ret.add(vo);
		}
		return ret;
		
	}
}
