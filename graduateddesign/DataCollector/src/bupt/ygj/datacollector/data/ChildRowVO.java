package bupt.ygj.datacollector.data;

import java.io.Serializable;
import java.util.List;

public class ChildRowVO implements Serializable{	
	
	private List<ItemVO> item;

	public List<ItemVO> getItem() {
		return item;
	}

	public void setItem(List<ItemVO> item) {
		this.item = item;
	}

	
}
