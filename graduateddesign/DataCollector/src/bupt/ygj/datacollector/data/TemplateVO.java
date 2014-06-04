package bupt.ygj.datacollector.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 自由表单模板的VO bupt.ygj.datacollector.data.TemplateVO
 * 
 * @author YanGJ create at 2014年6月4日 上午11:08:05
 */
@SuppressWarnings("serial")
public class TemplateVO extends ValueObject implements Serializable {
	private String pkorg = null;

	private String nameorg = null;

	private String pkdoc = null;
	private String eventid = null;
	private String visitid = null;
	private String workitemId = null;

	public void setWorkItemAttributes(String pkdoc, String eventid,
			String visitid, String workitemId) {
		this.pkdoc = pkdoc;
		this.eventid = eventid;
		this.visitid = visitid;
		this.workitemId = workitemId;
	}

	public void setOrgAttributes(String pkorg, String nameorg) {
		this.pkorg = pkorg;
		this.nameorg = nameorg;

	}

	public void setPkorg(String pkorg) {
		this.pkorg = pkorg;
	}

	public String getPkorg() {
		return pkorg;
	}

	public String getNameorg() {
		return nameorg;
	}

	public String getPkdoc() {
		return pkdoc;
	}

	public String getEventid() {
		return eventid;
	}

	public String getVisitid() {
		return visitid;
	}

	public String getWorkitemId() {
		return workitemId;
	}

	private String templatePK = null;
	private String ts = null;
	private String style = null;

	private List<ElementGroupVO4Header> headerList = null;
	private List<ElementGroupVO4Body> bodyList = null;

	private InitTemplateDataVO initTemplateVO = null;
	private InitTemplateDataVO initDataVO = null;

	public void setInitDataVO(InitTemplateDataVO initDataVO) {
		this.initDataVO = initDataVO;
	}

	public InitTemplateDataVO getInitDataVO() {
		return initDataVO;
	}

	public String getTemplatePK() {
		return templatePK;
	}

	public String getTs() {
		return ts;
	}

	public String getStyle() {
		return style;
	}

	public List<ElementGroupVO4Header> getHeaderList() {
		return headerList;
	}

	public List<ElementGroupVO4Body> getBodyList() {
		return bodyList;
	}

	public void setInitTemplateVO(InitTemplateDataVO initTemplateVO) {
		this.initTemplateVO = initTemplateVO;
	}

	public InitTemplateDataVO getInitTemplateVO() {
		return initTemplateVO;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static TemplateVO loadVO(Map map) {
		map = (Map) map.get("ftattribute");
		TemplateVO ret = new TemplateVO();
		ret.templatePK = ret.getMapStringValue(map, "templatepk");
		ret.ts = ret.getMapStringValue(map, "ts");
		ret.style = ret.getMapStringValue(map, "style");

		List<Map> headerMapList = (List<Map>) map.get("headattribute");
		if (headerMapList != null) {
			ret.headerList = ElementGroupVO4Header.loadVO(headerMapList);
		}
		List<Map> bodyMapList = (List<Map>) map.get("bodyattribute");
		if (bodyMapList != null) {
			ret.bodyList = ElementGroupVO4Body.loadVO(bodyMapList);
		}
		return ret;
	}

	/**
	 * 判断是否需要缓存模板，true为需要缓存，false为不需要缓存
	 * 
	 * @return
	 */
	public boolean isVaild() {
		if (this.headerList != null && this.headerList.size() > 0) {
			for (ElementGroupVO4Header header : headerList) {
				if (header.getElements() != null
						&& (header.getElements().size() > 0)) {
					return true;
				}
			}
		}

		if (this.bodyList != null && this.bodyList.size() > 0) {
			for (ElementGroupVO4Body body : bodyList) {
				if (body.getElements() != null
						&& (body.getElements().size() > 0)) {
					return true;
				}
			}
		}

		return false;
	}
}
