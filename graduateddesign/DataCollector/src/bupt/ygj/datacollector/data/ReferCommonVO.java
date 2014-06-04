package bupt.ygj.datacollector.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReferCommonVO extends ValueObject implements Serializable ,Comparable{

	private static final long serialVersionUID = -4045724353204093544L;

	private String id = null;
	private String code = null;
	private String name = null;
	private Boolean isSelected = false;

	public ReferCommonVO() {

	}

	public ReferCommonVO(Map<String, String> map) {
		id = getMapStringValue(map, "id");
		code = getMapStringValue(map, "code");
		name = getMapStringValue(map, "name");
	}

	public String getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}


	public static List<ReferCommonVO> loadVO(List<Map<String, String>> mapList) {
		if(mapList != null && mapList.size() > 0) {
			List<ReferCommonVO> voList = new ArrayList<ReferCommonVO>();
			if (null != mapList) {
	
				for (Map<String, String> map : mapList) {
					ReferCommonVO voItem = new ReferCommonVO(map);
					voList.add(voItem);
				}
	
			}
			return voList;
		} else 
			return null;
	}
	
	@Override
	public boolean equals(Object o) {

		ReferCommonVO person = (ReferCommonVO)o;
		if(this.getId().equals(person.getId()))
			return true;
		else
			return false;
	}

	@Override
	public int compareTo(Object another) {
		ReferCommonVO person = (ReferCommonVO)another;
		if(this.name.compareTo(person.getName()) > 0)
			return 1;
		else if(this.name.compareTo(person.getName()) < 0)
			return -1;
		else
			return 0;
	}
}
