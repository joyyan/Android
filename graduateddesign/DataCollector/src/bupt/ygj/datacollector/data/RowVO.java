package bupt.ygj.datacollector.data;
import java.util.List;

public class RowVO {
	
	
	//@JsonProperty("item")
	private List<ItemVO> item;
	
	//@XStreamAlias("child")
	//@JsonAddLevel("child")
	private List<ChildRowVO> child;
	
	//@XStreamAlias("style")
	//@JsonProperty("style")
	//@XStreamAsAttribute
	private String style;
	
	//@XStreamAlias("id")
	//@JsonProperty("id")
	//@XStreamAsAttribute
	private String id;
	
	

	public List<ItemVO> getItem() {
		return item;
	}

	public void setItem(List<ItemVO> item) {
		this.item = item;
	}

	public List<ChildRowVO> getChild() {
		return child;
	}

	public void setChild(List<ChildRowVO> child) {
		this.child = child;
	}

	
	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	
	

}
