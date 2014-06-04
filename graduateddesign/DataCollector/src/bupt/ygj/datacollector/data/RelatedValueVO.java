package bupt.ygj.datacollector.data;

import java.util.Map;

public class RelatedValueVO {

	/**
	* @Fields relatedpath : TODO(�������ֶ�·��
)
	*/


	private String relatedpath;

	/**
	* @Fields relatedvalue : TODO(�������ֶ�ֵ
)
	*/


	private String relatedvalue;


	public void setRelatedpath( String relatedpath ) {
		this.relatedpath = relatedpath;
	}

	public String getRelatedpath(){
		return relatedpath;
	}

	public void setRelatedvalue( String relatedvalue ) {
		this.relatedvalue = relatedvalue;
	}

	public String getRelatedvalue(){
		return relatedvalue;
	}

	
	public void setAttributes(Map mapvalue) {
		if(mapvalue != null){
			Map<String,String> map = mapvalue;
			this.relatedpath = map.get("relatedpath");
			this.relatedvalue = map.get("relatedvalue");			
		}
	}
}
