package bupt.ygj.datacollector.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BatchRelatedValue {

	private List<RelatedValueListVO> list;

	public List<RelatedValueListVO> getList() {
		return list;
	}

	public void setList(List<RelatedValueListVO> list) {
		this.list = list;
	}
	
	public void setAttributes(Map mapvalue) {
		List<Map> archivelist = (List<Map>) mapvalue.get("relatevaluelist");
		if(archivelist != null){
			this.list = new ArrayList<RelatedValueListVO>();
			for(Map map : archivelist){
				RelatedValueListVO relatedvalue = new RelatedValueListVO();
				relatedvalue.setAttributes(map);
				this.list.add(relatedvalue);
			}
		}
	}
}
