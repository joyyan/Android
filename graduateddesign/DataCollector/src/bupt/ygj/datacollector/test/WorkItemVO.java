package bupt.ygj.datacollector.test;

import java.util.Map;

import bupt.ygj.datacollector.data.ValueObject;

public class WorkItemVO extends ValueObject{

	/** 工作项目id（itemid） */
	private String itemid = "0005";
	/** 工作项目名称（itemname） */
	private String itemname = "test全字段";
	/** 是否必填标识（isrequired） */
	private boolean isrequired = true;
	/** 是否已录入标识（isentry） */
	private boolean isentry = true;
	/** 工作项目性质（itemtype） */
	private String itemtype = "3";
	/** 功能节点编码（functioncode）） */
	private String functioncode = "code04";
	/** 单据类型pk(pkdoc)） */
	private String pkdoc = "pk01";
	/** winid(winid)） */
	private String winid = "win01";
	/** 交易类型id(transtypeid)） */
	private String transtypeid = "trans01";


	public void setAttributes(Map<String, String> mapvalue) {
		if (mapvalue != null) {
			Map<String, String> map = mapvalue;
			this.itemid = this.getMapStringValue(map, "itemid");
			this.itemname = this.getMapStringValue(map, "itemname");
			this.isrequired = this.getMapStringValue(map, "isrequired").equals("1");
			this.isentry = this.getMapStringValue(map, "isentry").equals("1");
			this.itemtype = this.getMapStringValue(map, "itemtype");
			this.functioncode = this.getMapStringValue(map, "functioncode");
			this.pkdoc = this.getMapStringValue(map, "pkdoc");
			this.winid = this.getMapStringValue(map, "winid");
			this.transtypeid = this.getMapStringValue(map, "transtypeid");
		}
	}
	
	public String getItemid() {
		return itemid;
	}

	public String getItemname() {
		return itemname;
	}

	public boolean getIsrequired() {
		return isrequired;
	}

	public boolean getIsentry() {
		return isentry;
	}

	public String getItemtype() {
		return itemtype;
	}

	public String getFunctioncode() {
		return functioncode;
	}

	public String getPkdoc() {
		return pkdoc;
	}

	public String getWinid() {
		return winid;
	}

	public String getTranstypeid() {
		return transtypeid;
	}

}
