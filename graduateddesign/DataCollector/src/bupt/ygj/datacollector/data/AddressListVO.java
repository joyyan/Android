package bupt.ygj.datacollector.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


//@JsonRootName("addresslist")
public class AddressListVO {

	/**
	* @Fields list : TODO(�¼��������б�)
	*/

//	@JsonProperty("address")
	private List<AddressVO> list;


	public void setList( List<AddressVO> list ) {
		this.list = list;
	}

	public List<AddressVO> getList(){
		return list;
	}

	
	public void setAttributes(Map mapvalue) {
		if(null != mapvalue) {
			@SuppressWarnings("unchecked")
			List<Map<String,String>> list = (List<Map<String, String>>) mapvalue.get("address");
			if(list != null){
				this.list = new ArrayList<AddressVO>();
				for(Map<String,String> map : list){
					AddressVO address = new AddressVO();
					address.setAttributes(map);
					this.list.add(address);
				}
				
			}
		}
	}
}
