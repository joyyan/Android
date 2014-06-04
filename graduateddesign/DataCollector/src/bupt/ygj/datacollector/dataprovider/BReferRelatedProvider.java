package bupt.ygj.datacollector.dataprovider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wa.android.common.activity.BaseActivity;
import wa.android.common.network.WAParValueList;
import wa.android.common.network.WAParValueVO;
import wa.android.common.network.WAParameterExt;
import wa.android.common.network.WAReqActionVO;
import wa.android.common.network.WARequestVO;
import wa.android.common.network.WAResActionVO;
import wa.android.common.network.WAResStructVO;
import wa.android.constants.CommonServers;
import android.os.Handler;
import android.os.Message;
import bupt.ygj.datacollector.data.BatchRelatedValue;
import bupt.ygj.datacollector.data.ExceptionEncapsulationVO;
import bupt.ygj.datacollector.data.ReferToItemVO;
import bupt.ygj.datacollector.datarequester.WAVORequester;

public class BReferRelatedProvider extends WAVORequester {

	public static final int MSG_CHANNEL_FAILED = -1;
	// 普通的数据请求
	public final int OK = 0;
	// 部分action请求出错
	public final int PART_ACTION_FAILED = 4;
	public final int FLAG_PART_ACTION_FAILED = 5;

	public BReferRelatedProvider(BaseActivity content, Handler handler,
			int msgId) {
		super(content, handler, msgId);
	}

	public BReferRelatedProvider(BaseActivity content, Handler handler) {
		super(content, handler, 100);
	}

	public void getBatchReferRelatedValues(String itemkey, String referto,String pkvalue,List<String> relatedpathlist) {

		WAReqActionVO actionVO1 = WAReqActionVO.createCommonActionVO("getBatchReferRelatedValues");
		
		WAParValueList relatedpathlistP = new WAParValueList();		//增加List<String>类型的参数
		if(null != relatedpathlist) {
			for (String id : relatedpathlist) {
				relatedpathlistP.addString(id);
			}
		}
		
		WAParValueVO referToItemVO = new WAParValueVO();
		WAParValueVO referToItemDetail = new WAParValueVO();
		WAParValueList referToItemDetailList = new WAParValueList();
		referToItemVO.addField("itemkey", itemkey);
		referToItemVO.addField("referto", referto);
		referToItemVO.addField("pkvalue", pkvalue);
		referToItemVO.addField("relatedpathlist", relatedpathlistP);		//增加List<String>类型的参数
		
		referToItemDetail.addField("refertoitemlist", referToItemVO);
		referToItemDetailList.addItem(referToItemDetail);
		
		WAParameterExt parameter = new WAParameterExt();
		parameter = WAParameterExt.newWAParameterGroup("refertoitemlist",referToItemVO);
		actionVO1.addPar(parameter);

		request(CommonServers.getServerAddress(context) + CommonServers.SERVER_SERVLET_WA,
				"WA00038",actionVO1,this);
	}
	
	
	public void getBatchReferRelatedValues(List<ReferToItemVO> referToItemList) {
		
		WAReqActionVO actionVO1 = WAReqActionVO.createCommonActionVO("getBatchReferRelatedValues");
		
		WAParValueList referToItemDetailList = new WAParValueList();
		for(ReferToItemVO refer : referToItemList) {
			WAParValueList relatedpathlistP = new WAParValueList();		//增加List<String>类型的参数
			if(null != refer) {
				for (String id : refer.getRelatedpathlist()) {
					relatedpathlistP.addString(id);
				}
			}
			
			WAParValueVO referToItemVO = new WAParValueVO();
			referToItemVO.addField("itemkey", refer.getItemkey());
			referToItemVO.addField("referto", refer.getReferto());
			referToItemVO.addField("pkvalue", refer.getPkvalue());
//			referToItemVO.addField("usrid", "");
//			referToItemVO.addField("groupid", "");
			referToItemVO.addField("relatedpathlist", relatedpathlistP);		//增加List<String>类型的参数
			
			referToItemDetailList.addItem(referToItemVO);
		}
		
		WAParameterExt parameter = new WAParameterExt();
		parameter = WAParameterExt.newWAParameterGroup("refertoitemlist",referToItemDetailList);
		
		actionVO1.addPar(parameter);
		
		request(CommonServers.getServerAddress(context) + CommonServers.SERVER_SERVLET_WA,
				"WA00038",actionVO1,this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onRequested(WARequestVO vo) {

		List<WAReqActionVO> listAction = vo.getReqComponentVO("WA00038").actionList;
		Map dataMap = new HashMap();
		ExceptionEncapsulationVO partExceptionvo = new ExceptionEncapsulationVO();
		ExceptionEncapsulationVO flagpartExceptionvo = new ExceptionEncapsulationVO();
		BatchRelatedValue rra = null;
		for (WAReqActionVO action : listAction) {
			WAResActionVO resVO = action.resActionVO;
			String actionType = action.getActiontype();
			if (resVO.flag == 0) {
				try {
					WAResStructVO structVO = resVO.responseList.get(0);
					if (actionType.equals("getBatchReferRelatedValues")) {
						Map<String, Map> data = (Map<String, Map>) structVO.returnValue.get(0);
						rra = new BatchRelatedValue();
						Map dataM = data.get("batchrelatedvalue");
						rra.setAttributes(dataM);
					}
				} catch (Exception e) {
					partExceptionvo.getMessageList().add(actionType + "数据解析错误");
					e.printStackTrace();
				}
			} else {
				String errorDesc = "";
				if (null != resVO) {
					errorDesc = resVO.desc;
				} else
					errorDesc = actionType + "没有返回";
				flagpartExceptionvo.getFlagmessageList().add(errorDesc);
			}
		}
		if (null != rra)
			dataMap.put("batchrelatedvalue", rra);
		if (dataMap.size() == 1) // 1个action完成正确的情况
			handler.sendMessage(makeMessage(OK, dataMap));
		else {
			if (flagpartExceptionvo.getFlagmessageList().size() != 0) {
				dataMap.put("flagexception", flagpartExceptionvo);
				handler.sendMessage(makeMessage(FLAG_PART_ACTION_FAILED,dataMap));
			} else if (partExceptionvo.getMessageList().size() != 0) {
				dataMap.put("exception", partExceptionvo);
				handler.sendMessage(makeMessage(PART_ACTION_FAILED, dataMap));
			}
		}
	}

	@Override
	public void onRequestFailed(int code) {
		Message msg = makeMessage(MSG_REQUEST_FAILED, null);
		msg.arg1 = code;
		handler.sendMessage(msg);
	}
}
