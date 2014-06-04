package bupt.ygj.datacollector.data;

import java.util.List;

import wa.android.common.network.WAParValueList;
import wa.android.common.network.WAParValueVO;
import wa.android.common.network.WAParameterExt;
import wa.android.common.network.WAReqActionVO;

public class CFSaveDataVO{
	private String pkDoc = null;
	private String eventId = null;
	private String visitId = null;
	private String workItemId = null;

	//接口中要求将以下两项包成VO放入List中上传，该List中仅包含一个
	//表头信息
	private List<AbsFieldValue> hSaveItemList = null;
	//表体信息
	private CFSaveBodyDataVO bSaveItemList = new CFSaveBodyDataVO();
	
	private GPSInfo gpsinfo = new GPSInfo();
	
	public CFSaveDataVO(String pkDoc, String eventId, String visitId, String workItemId){
		this.pkDoc = pkDoc;
		this.eventId = eventId;
		this.visitId = visitId;
		this.workItemId = workItemId;
	}

	public WAReqActionVO toWAParameter(){
		WAReqActionVO ret = WAReqActionVO.createCommonActionVO("saveCFData");
		ret.addPar("pkdoc", pkDoc);
		ret.addPar("eventid", eventId);
		ret.addPar("visitid", visitId);
		ret.addPar("workitemid", workItemId);
		
		//表单信息
		//savecflist = List<SaveCFVO>
		WAParValueList saveCFList = new WAParValueList();
		//VO:SaveCFVO
		WAParValueVO saveCFVO = new WAParValueVO();
		
//		//表头信息
//		WAParValueList headItemList = new WAParValueList();
//		if(null != hSaveItemList) {
//			for (AbsFieldValue field : hSaveItemList) {
//				//VO:ItemValueVO
//				if(field != null) {	//field我们在代码中控制了，可以为null
//					WAParValueVO fieldPar = field.toWAParameter();
//					headItemList.addItem(fieldPar);
//				}
//			}
//		}
		
		WAParValueVO headItemListVO = new WAParValueVO();
		if(null != hSaveItemList) {
			for (AbsFieldValue field : hSaveItemList) {
				//VO:ItemValueVO
				if(field != null) {	//field我们在代码中控制了，可以为null
					WAParValueVO fieldPar = field.toWAParameter();
					headItemListVO.addField(field.key, fieldPar);
				}
			}
		}
		saveCFVO.addField("hsaveitemlist", headItemListVO);

		//表体信息
		WAParValueList bodyList = new WAParValueList();
		if(null != bSaveItemList) {
			WAParValueVO bodyCFData = bSaveItemList.toWAParameter();
			bodyList.addItem(bodyCFData);
			saveCFVO.addField("bsavelist", bodyCFData);
		}
		
		//地理位置信息
		WAParValueVO gpsinfoP = new WAParValueVO();
		gpsinfoP.addField("jlongitude", gpsinfo.getJlongitude());
		gpsinfoP.addField("wlatitude", gpsinfo.getWlatitude());
		gpsinfoP.addField("helevation", gpsinfo.getHelevation());
		saveCFVO.addField("gpsinfo", gpsinfoP);

		saveCFList.addItem(saveCFVO);
		WAParameterExt cfList = WAParameterExt.newWAParameterGroup("savecflist", saveCFList);		
		ret.addPar(cfList);
		return ret;
	}
	
	public CFSaveBodyDataVO getbSaveItemList() {
		return bSaveItemList;
	}

	public void setbSaveItemList(CFSaveBodyDataVO bSaveItemList) {
		this.bSaveItemList = bSaveItemList;
	}

	public void setHeaderItemList(List<AbsFieldValue> hSaveItemList) {
		this.hSaveItemList = hSaveItemList;
	}
	
	public void addBodyRow(List<AbsFieldValue> bodyRow,String childid){
		bSaveItemList.addBodyRow(bodyRow,childid);
	}
	
	public GPSInfo getGpsinfo() {
		return gpsinfo;
	}

	public void setGpsinfo(GPSInfo gpsinfo) {
		this.gpsinfo = gpsinfo;
	}
}
