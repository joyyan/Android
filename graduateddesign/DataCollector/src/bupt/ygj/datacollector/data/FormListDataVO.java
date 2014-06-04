//package bupt.ygj.datacollector.data;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//
//public class FormListDataVO {
//
//	/**
//	* @Fields billlist : TODO(�����б�)
//	*/
//
//	private List<WorkSubmitVO> billlist;
//
//
//	public void setBilllist( List<WorkSubmitVO> billlist ) {
//		this.billlist = billlist;
//	}
//
//	public List<WorkSubmitVO> getBilllist(){
//		return billlist;
//	}
//
//	
//	public void setAttributes(Map mapvalue) {
//		if(null != mapvalue) {
//			List<Map<String,Object>> billlist = (List<Map<String, Object>>) mapvalue.get("worksubmit");
//			if(billlist != null){
//				this.billlist = new ArrayList<WorkSubmitVO>();			
//				for(Map<String,Object> map : billlist){
//					WorkSubmitVO worksubmit = new WorkSubmitVO();
//					worksubmit.setAttributes(map);
//					this.billlist.add(worksubmit);
//				}
//			}
//		}
//	}
//}
