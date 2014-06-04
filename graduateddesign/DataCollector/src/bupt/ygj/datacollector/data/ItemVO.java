package bupt.ygj.datacollector.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemVO implements Serializable{

	public ItemVO(String name, String value) {
		this.name = name;
		this.value = new ArrayList<String>();
		if(null != value){
			this.value.add(value);
		}else{
			this.value.add("");
		}
		
	}
	public ItemVO(String id, List<String> values) {
		this.id = id;
		this.value =values;
	}

	
	//@XStreamAlias("id")
	//@XStreamAsAttribute
	//@JsonProperty("@id")
	public String id = null;
	
	//@XStreamAlias("name")
	//@JsonProperty("@name")
	//@XStreamAsAttribute
	public String name = null;
	
	//@XStreamAlias("valuedesc")
	//@JsonProperty("valuedesc")
	//@XStreamAsAttribute
	private String valuedesc;
	
	//@XStreamAlias("mode")
	//@JsonProperty("mode")
	//@XStreamAsAttribute
	private String mode;
	
	//@XStreamAlias("referid")
	//@JsonProperty("referid")
	//@XStreamAsAttribute
	private String referid;
	
	//@XStreamAlias("checkstatus")
	//@JsonProperty("checkstatus")
	//@XStreamAsAttribute
	private String checkstatus;
	
	//@XStreamAlias("refertype")
	//@JsonProperty("refertype")
	//@XStreamAsAttribute
	private String refertype;
	
	//@XStreamAlias("readonly")
	//@JsonProperty("readonly")
	//@XStreamAsAttribute
	private String readonly;
	
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	//@XStreamImplicit(itemFieldName="value")
	//@JacksonArraySingleElement
	//@JsonProperty("value")
	public List<String> value = null;
	
	//@XStreamImplicit(itemFieldName="item")
	//@JsonProperty("item")
	private List<ItemVO> item = null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getValue() {
		return value;
	}

	public void setValue(List<String> value) {
		this.value = value;
	}
	public String getValuedesc() {
		return valuedesc;
	}
	public void setValuedesc(String valuedesc) {
		this.valuedesc = valuedesc;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getReferid() {
		return referid;
	}
	public void setReferid(String referid) {
		this.referid = referid;
	}
	public String getCheckstatus() {
		return checkstatus;
	}
	public void setCheckstatus(String checkstatus) {
		this.checkstatus = checkstatus;
	}
	public String getRefertype() {
		return refertype;
	}
	public void setRefertype(String refertype) {
		this.refertype = refertype;
	}
	public String getReadonly() {
		return readonly;
	}
	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}

	
}
