package bupt.ygj.datacollector.data;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorkSubmitVO {

	/**
	* @Fields billid : TODO(�������id
)
	*/


	private String billid;

	/**
	* @Fields itemlist : TODO(�ֶ��б�)
	*/


	private List<Item> itemlist;


	public List<Item> getItemlist() {
		return itemlist;
	}

	public void setItemlist(List<Item> itemlist) {
		this.itemlist = itemlist;
	}

	public void setBillid( String billid ) {
		this.billid = billid;
	}

	public String getBillid(){
		return billid;
	}

	

	
	public void setAttributes(Map mapvalue) {
		if(mapvalue != null){
			this.billid = (String) mapvalue.get("billid");
			List<Map<String,String>> list = (List<Map<String, String>>) mapvalue.get("item");
			if(list != null){
				this.itemlist = new ArrayList<Item>();
				for(Map<String,String> map : list){
					Item item = new Item();
					item.setAttributes(map);
					itemlist.add(item);
				}
			}
		}
	}
}
