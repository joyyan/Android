package bupt.ygj.datacollector.data;

import java.util.Map;

public class ItemInitValueVO {

	private String itemkey;

	private String realvalue;


	public void setItemkey( String itemkey ) {
		this.itemkey = itemkey;
	}

	public String getItemkey(){
		return itemkey;
	}

	public void setRealvalue( String realvalue ) {
		this.realvalue = realvalue;
	}

	public String getRealvalue(){
		return realvalue;
	}

	
	public void setAttributes(Map mapvalue) {
		if(mapvalue != null){
			Map<String,String> map = (Map<String,String>)mapvalue;
			this.itemkey = map.get("itemkey");
			this.realvalue = map.get("realvalue");
		}
	}
}
