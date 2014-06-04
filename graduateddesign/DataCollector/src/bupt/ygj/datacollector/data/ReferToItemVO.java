package bupt.ygj.datacollector.data;

import java.util.List;

/**
 * 参照关联项请求参数vo
 * @author cuihd
 *
 */
public class ReferToItemVO {

	private String itemkey;
	private String referto;
	private String pkvalue;
	private List<String> relatedpathlist;
	
	public String getItemkey() {
		return itemkey;
	}
	public void setItemkey(String itemkey) {
		this.itemkey = itemkey;
	}
	public String getReferto() {
		return referto;
	}
	public void setReferto(String referto) {
		this.referto = referto;
	}
	public String getPkvalue() {
		return pkvalue;
	}
	public void setPkvalue(String pkvalue) {
		this.pkvalue = pkvalue;
	}
	public List<String> getRelatedpathlist() {
		return relatedpathlist;
	}
	public void setRelatedpathlist(List<String> relatedpathlist) {
		this.relatedpathlist = relatedpathlist;
	}
}
