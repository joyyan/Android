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

public class ReferValuesRequester extends WAVORequester {
	
	public static final int MSG_CHANNEL_FAILED = -1;
	// 普通的数据请求
	public final int OK = 0;
	// 部分action请求出错
	public final int PART_ACTION_FAILED = 4;
	public final int FLAG_PART_ACTION_FAILED = 5;

	public ReferValuesRequester(BaseActivity context, Handler handler, int msgId) {
		super(context, handler, msgId);
	}
	public ReferValuesRequester(BaseActivity context, Handler handler) {
		super(context, handler, 100);
	}
	


	public void getReferInventoryValues(String pkOrg, String referTo, String condition, String pageIndex) {
		WAReqActionVO actionVO = WAReqActionVO.createCommonActionVO("getReferValues");
		actionVO.addPar("condition", condition);
		if(pkOrg == null)
			pkOrg = "";
		actionVO.addPar("pk_org", pkOrg);
		actionVO.addPar("referto", referTo);
		actionVO.addPar("startline", pageIndex);
		actionVO.addPar("ispaging", "Y");
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
					Map map = (Map)mapTemp.get("archivelist");
					List<Map<String, String>> mapList = (List<Map<String, String>>)map.get("archive");
					voList = ReferCommonVO.loadVO(mapList);
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
	
	protected String getDefedKeyOfDataList() {
		return "inventorylist";
	}
}
