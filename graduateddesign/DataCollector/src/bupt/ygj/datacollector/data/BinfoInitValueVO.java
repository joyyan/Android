package bupt.ygj.datacollector.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BinfoInitValueVO {

	/**
	* @Fields childdocid : TODO(�ӱ�ID)
	*/

	private String childdocid;

	/**
	* @Fields binitvaluelist : TODO(�����ʼֵ�б�)
	*/

	private List<ItemInitValueVO > binitvaluelist;


	public void setChilddocid( String childdocid ) {
		this.childdocid = childdocid;
	}

	public String getChilddocid(){
		return childdocid;
	}

	public void setBinitvaluelist( List<ItemInitValueVO > binitvaluelist ) {
		this.binitvaluelist = binitvaluelist;
	}

	public List<ItemInitValueVO > getBinitvaluelist(){
		return binitvaluelist;
	}

	public void setAttributes(Map mapvalue) {
		if(mapvalue != null){
			this.childdocid = (String) mapvalue.get("childdocid");
			List<Map<String,String>> binitvaluelist = (List<Map<String, String>>) mapvalue.get("itemvalue");
			if(binitvaluelist != null){
				this.binitvaluelist = new ArrayList<ItemInitValueVO>();
				for(Map<String,String> map : binitvaluelist){
					ItemInitValueVO itemvalue = new ItemInitValueVO();
					itemvalue.setAttributes(map);
					this.binitvaluelist.add(itemvalue);
				}
			}
		}
	}
}
