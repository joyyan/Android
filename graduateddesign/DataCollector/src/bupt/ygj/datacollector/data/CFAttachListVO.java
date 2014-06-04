package bupt.ygj.datacollector.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CFAttachListVO {

	private String attachitemkey;

	private List<AttachmentVO> cfattachlist;

	public void setAttachitemkey( String attachitemkey ) {
		this.attachitemkey = attachitemkey;
	}

	public String getAttachitemkey(){
		return attachitemkey;
	}

	public void setCfattachlist( List<AttachmentVO> cfattachlist ) {
		this.cfattachlist = cfattachlist;
	}

	public List<AttachmentVO> getCfattachlist(){
		return cfattachlist;
	}

	public void setAttributes(Map mapvalue) {
		if(null != mapvalue) {
			this.attachitemkey = (String) mapvalue.get("attachitemkey");
			List<Map<String,String>> cfattachlist = (List<Map<String, String>>) mapvalue.get("attachment");
			if(cfattachlist != null){
				this.cfattachlist = new ArrayList<AttachmentVO>();
				for(Map<String,String> map : cfattachlist){
					AttachmentVO attachment = new AttachmentVO();
					attachment.setAttributes(map);
					this.cfattachlist.add(attachment);
				}
			}
		}
	}
}
