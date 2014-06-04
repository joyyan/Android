package bupt.ygj.datacollector.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * bupt.ygj.datacollector.data.RelatedValueListVO
 * 
 * @author YanGJ create at 2014年6月4日 上午11:12:51
 */
public class RelatedValueListVO {

	private String itemkey;

	private List<RelatedValueVO> relatedvaluelist;

	public void setItemkey(String itemkey) {
		this.itemkey = itemkey;
	}

	public String getItemkey() {
		return itemkey;
	}

	public void setRelatedvaluelist(List<RelatedValueVO> relatedvaluelist) {
		this.relatedvaluelist = relatedvaluelist;
	}

	public List<RelatedValueVO> getRelatedvaluelist() {
		return relatedvaluelist;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setAttributes(Map mapvalue) {
		this.itemkey = (String) mapvalue.get("itemkey");
		List<Map<String, String>> archivelist = (List<Map<String, String>>) mapvalue
				.get("relatedvalue");
		if (archivelist != null) {
			this.relatedvaluelist = new ArrayList<RelatedValueVO>();
			for (Map<String, String> map : archivelist) {
				RelatedValueVO relatedvalue = new RelatedValueVO();
				relatedvalue.setAttributes(map);
				this.relatedvaluelist.add(relatedvalue);
			}
		}
	}
}
