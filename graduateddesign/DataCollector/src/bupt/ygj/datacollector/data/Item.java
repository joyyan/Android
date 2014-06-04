package bupt.ygj.datacollector.data;

import java.util.Map;

public class Item {

	private String name;
	private String type;
	private String displayvalue;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDisplayvalue() {
		return displayvalue;
	}

	public void setDisplayvalue(String displayvalue) {
		this.displayvalue = displayvalue;
	}

	public void setAttributes(Map mapvalue) {
		if(mapvalue != null){
			this.name = (String) mapvalue.get("name");
			this.type = (String) mapvalue.get("type");
			this.displayvalue = (String) mapvalue.get("value");
		}
	}
}
