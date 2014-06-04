package bupt.ygj.datacollector.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//JsonRootName("forminitdata")
public class FormInitDataVO {

	/**
	* @Fields hinitvaluelist : TODO(表头初始值列表)
	*/
	private List<ItemInitValueVO> hinitvaluelist;

	/**
	* @Fields binfoinitvaluelist : TODO(子表信息初始值列表)
	*/
	private List<BinfoInitValueVO> binfoinitvaluelist;

	public void setHinitvaluelist( List<ItemInitValueVO> hinitvaluelist ) {
		this.hinitvaluelist = hinitvaluelist;
	}

	public List<ItemInitValueVO> getHinitvaluelist(){
		return hinitvaluelist;
	}

	public void setBinfoinitvaluelist( List<BinfoInitValueVO> binfoinitvaluelist ) {
		this.binfoinitvaluelist = binfoinitvaluelist;
	}

	public List<BinfoInitValueVO> getBinfoinitvaluelist(){
		return binfoinitvaluelist;
	}

	
	public void setAttributes(Map mapvalue) {
		if(null != mapvalue) {
			List<Map<String,String>> hinitvaluelist = (List<Map<String, String>>) mapvalue.get("itemvalue");
			if(hinitvaluelist != null){
				this.hinitvaluelist = new ArrayList<ItemInitValueVO>();
				for(Map<String,String> map : hinitvaluelist){
					ItemInitValueVO itemvalue = new ItemInitValueVO();
					itemvalue.setAttributes(map);
					this.hinitvaluelist.add(itemvalue);
				}
			}
			List<Map<String,Object>> binfoinitvaluelist = (List<Map<String, Object>>) mapvalue.get("binfoinitvalue");
			if(binfoinitvaluelist != null){
				this.binfoinitvaluelist = new ArrayList<BinfoInitValueVO>();
				for(Map<String,Object> map : binfoinitvaluelist){
					BinfoInitValueVO binfoinitvalue = new BinfoInitValueVO();
					binfoinitvalue.setAttributes(map);
					this.binfoinitvaluelist.add(binfoinitvalue);
				}
			}
		}
	}
}
