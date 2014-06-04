package bupt.ygj.datacollector.data;

import java.util.Map;

public class SaveCFVOSuc {
	
	private String visitid;

	public String getVisitid() {
		return visitid;
	}

	public void setVisitid(String visitid) {
		this.visitid = visitid;
	}
	
	public void setAttributes(Map mapvalue) {

		if(null != mapvalue) {
			this.visitid = (String)mapvalue.get("visitid");
		}
	
	}
}
