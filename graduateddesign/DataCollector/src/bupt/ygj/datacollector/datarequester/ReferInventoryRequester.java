package bupt.ygj.datacollector.datarequester;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wa.android.common.activity.BaseActivity;
import wa.android.common.network.WAReqActionVO;
import wa.android.common.network.WARequestVO;
import wa.android.common.network.WAResActionVO;
import wa.android.common.network.WAResStructVO;
import wa.android.constants.CommonServers;
import android.os.Handler;
import android.os.Message;
import bupt.ygj.datacollector.constants.ActionType;
import bupt.ygj.datacollector.data.ExceptionEncapsulationVO;
import bupt.ygj.datacollector.data.ReferCommonVO;

public class ReferInventoryRequester extends ReferCommonRequester {
	
	public static final int MSG_CHANNEL_FAILED = -1;
	// 普通的数据请求
	public final int OK = 1;
	// 部分action请求出错
	public final int PART_ACTION_FAILED = 4;
	public final int FLAG_PART_ACTION_FAILED = 5;

	public ReferInventoryRequester(BaseActivity context, Handler handler, int msgId) {
		super(context, handler, msgId);
	}
	public ReferInventoryRequester(BaseActivity context, Handler handler) {
		super(context, handler, 100);
	}
	
	@Override
	public void request(String pkOrg, String referTo, String condition, int pageIndex) {
		WAReqActionVO actionVO = WAReqActionVO.createCommonActionVO(getDefedActionType());
		actionVO.addPar("condition", condition);
		if(pkOrg == null)
			pkOrg = "";
		actionVO.addPar("pk_org", pkOrg);
		actionVO.addPar("startline", String.valueOf(pageIndex * 25 + 1));
		actionVO.addPar("count", "25");
		request(CommonServers.getServerAddress(context) + CommonServers.SERVER_SERVLET_WA, 
				ActionType.CID_QDBS, actionVO, this);
	}

	public void getReferInventoryValues(String pkOrg, String condition, String pageIndex) {
		WAReqActionVO actionVO = WAReqActionVO.createCommonActionVO(getDefedActionType());
		actionVO.addPar("condition", condition);
		if(pkOrg == null)
			pkOrg = "";
		actionVO.addPar("pk_org", pkOrg);
		actionVO.addPar("startline", pageIndex);
		actionVO.addPar("count", "25");
		request(CommonServers.getServerAddress(context) + CommonServers.SERVER_SERVLET_WA, 
				ActionType.CID_QDBS, actionVO, this);
	}
	
	public void getReferInventoryClassValues(String pkOrg, String condition, String pageIndex) {
		WAReqActionVO actionVO = WAReqActionVO.createCommonActionVO("getReferInventoryClassValues");
		actionVO.addPar("condition", condition);
		if(pkOrg == null)
			pkOrg = "";
		actionVO.addPar("pk_org", pkOrg);
		actionVO.addPar("startline", pageIndex);
		actionVO.addPar("count", "25");
		request(CommonServers.getServerAddress(context) + CommonServers.SERVER_SERVLET_WA, 
				ActionType.CID_QDBS, actionVO, this);
	}
	
	@Override
	public void onRequested(WARequestVO vo) {

		List<WAReqActionVO> listAction = vo.getReqComponentVO("WA00038").actionList;
		Map dataMap = new HashMap();
		ExceptionEncapsulationVO partExceptionvo = new ExceptionEncapsulationVO();
		ExceptionEncapsulationVO flagpartExceptionvo = new ExceptionEncapsulationVO();
		List<ReferCommonVO> voList = null;
		for (WAReqActionVO action : listAction) {
			WAResActionVO resVO = action.resActionVO;
			String actionType = action.getActiontype();
			if (resVO.flag == 0) {
				try {
					WAResStructVO structVO = resVO.responseList.get(0);
					Map<String, List<Map<String, String>>> mapTemp = (Map<String, List<Map<String, String>>>)structVO.returnValue.get(0);
					if(action.getActiontype().equals("getReferInventoryValues")) {
						Map map = (Map)mapTemp.get("inventorylist");
						List<Map<String, String>> mapList = (List<Map<String, String>>)map.get("inventory");
						voList = ReferCommonVO.loadVO(mapList);
					} else {
						Map map = (Map)mapTemp.get("classlist");
						List<Map<String, String>> mapList = (List<Map<String, String>>)map.get("inventoryclass");
						voList = ReferCommonVO.loadVO(mapList);
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
		if (null != voList)
			dataMap.put("inventory", voList);
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
	
	
	protected String getDefedActionType(){
		return "getReferInventoryValues";
	}
	
	@Override
	protected String getDefedKeyOfDataList() {
		return "inventorylist";
	}
}
