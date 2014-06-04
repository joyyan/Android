package bupt.ygj.datacollector.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 自由表单中表示单个数据项的数据VO类 bupt.ygj.datacollector.data.ElementDataVO
 * 
 * @author YanGJ create at 2014年6月4日 上午11:10:33
 */
@SuppressWarnings("serial")
public class ElementDataVO extends ValueObject implements Serializable {
	private String itemKey = null;

	private String controlId = null;
	private boolean isPrimaryKey = false;
	private boolean isForeignKey = false;
	private String itemOrder = null;
	private boolean isRelated = false;

	private String itemName = null;
	private String itemType = null;
	private boolean visible = false;
	private boolean allowEmpty = false;
	private boolean editable = false;
	private boolean enable = false;
	private String editFormula = null;
	private String referTo = null;
	private int length = -1;
	private String defaultValue = null;
	private String defaultReferPK = null;
	private String precision = null;
	private List<String> pathStringList = null;
	private List<RelatedItemVO> relatedItemList = null;

	private boolean isMultiselected = false;
	private List<EnumValueVO> enumItemList = null;

	public boolean isRelated() {
		return isRelated;
	}

	public void setRelated(boolean isRelated) {
		this.isRelated = isRelated;
	}

	public List<RelatedItemVO> getRelatedItemList() {
		return relatedItemList;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setDefaultReferPK(String defaultReferPK) {
		this.defaultReferPK = defaultReferPK;
	}

	public String getItemKey() {
		return itemKey;
	}

	public String getControlId() {
		return controlId;
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	public String getItemOrder() {
		return itemOrder;
	}

	public String getItemName() {
		return itemName;
	}

	public String getItemType() {
		return itemType;
	}

	public boolean isVisible() {
		return visible;
	}

	public boolean isAllowEmpty() {
		return allowEmpty;
	}

	public boolean isEditable() {
		return editable && enable;
	}

	public boolean isEnable() {
		return enable;
	}

	public String getEditFormula() {
		return editFormula;
	}

	public String getReferTo() {
		return referTo;
	}

	public int getLength() {
		return length;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public String getDefaultReferPK() {
		return defaultReferPK;
	}

	public String getPrecision() {
		return precision;
	}

	public List<String> getPathStringList() {
		return pathStringList;
	}

	public boolean isMultiselected() {
		return isMultiselected;
	}

	public List<EnumValueVO> getEnumItemList() {
		return enumItemList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ElementDataVO loadVO(Map map) {
		ElementDataVO ret = new ElementDataVO();
		ret.itemKey = ret.getMapStringValue(map, "itemkey");
		ret.controlId = ret.getMapStringValue(map, "ctrlid");
		ret.isPrimaryKey = "Y".equals(ret
				.getMapStringValue(map, "isprimarykey"));
		// isforeignkey
		ret.itemOrder = ret.getMapStringValue(map, "order");
		// isrelated
		ret.itemName = ret.getMapStringValue(map, "name");
		ret.itemType = ret.getMapStringValue(map, "type");
		ret.visible = "Y".equals(ret.getMapStringValue(map, "isvisible"));
		ret.allowEmpty = "Y".equals(ret.getMapStringValue(map, "isempty"));
		ret.editable = "Y".equals(ret.getMapStringValue(map, "isedit"));
		ret.isRelated = "Y".equals(ret.getMapStringValue(map, "isrelated"));
		ret.enable = "Y".equals(ret.getMapStringValue(map, "enabled"));
		ret.editFormula = ret.getMapStringValue(map, "editformula");
		ret.referTo = ret.getMapStringValue(map, "referto");
		try {
			ret.length = Integer.parseInt(ret.getMapStringValue(map, "length"));
		} catch (Exception e) {
			ret.length = -1;
		}
		ret.defaultValue = ret.getMapStringValue(map, "defvalue");
		ret.defaultReferPK = ret.getMapStringValue(map, "defaultrefpk");
		ret.precision = ret.getMapStringValue(map, "precision");

		List<Map<String, String>> relatedItemList = (List<Map<String, String>>) map
				.get("relateditem");

		if (relatedItemList != null) {
			ret.relatedItemList = new ArrayList<RelatedItemVO>();
			for (Map mapTemp : relatedItemList) {
				RelatedItemVO relatedItem = new RelatedItemVO();
				relatedItem.setAttributes(mapTemp);
				ret.relatedItemList.add(relatedItem);
			}
			// ret.relatedItemList = RelatedItemVO.loadVO(relatedItemList);
		}

		ret.isMultiselected = "Y".equals(ret.getMapStringValue(map,
				"ismultisel"));

		// ret.pathStringList = (ArrayList<String>)map.get("pathstringlist");

		ArrayList<Map<String, String>> mapList = (ArrayList<Map<String, String>>) map
				.get("enumvalue");
		if (mapList != null) {
			ret.enumItemList = EnumValueVO.loadVO(mapList);
		}
		return ret;
	}

	@SuppressWarnings("rawtypes")
	public static List<ElementDataVO> loadVO(List<Map> mapList) {
		List<ElementDataVO> ret = new ArrayList<ElementDataVO>();
		for (Map map : mapList) {
			ElementDataVO dataVO = loadVO(map);
			ret.add(dataVO);
		}
		return ret;
	}

	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setAllowEmpty(boolean allowEmpty) {
		this.allowEmpty = allowEmpty;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
}
