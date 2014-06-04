package bupt.ygj.datacollector.dataprovider;

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
import bupt.ygj.datacollector.data.AddressListVO;
import bupt.ygj.datacollector.data.ExceptionEncapsulationVO;
import bupt.ygj.datacollector.datarequester.WAVORequester;

public class AddressProvider extends WAVORequester {

	public static final int MSG_CHANNEL_FAILED = -1;
	// 普通的数据请求
	public final int OK = 0;
	// 部分action请求出错
	public final int PART_ACTION_FAILED = 4;
	public final int FLAG_PART_ACTION_FAILED = 5;

	public AddressProvider(BaseActivity content, Handler handler,
			int msgId) {
		super(content, handler, msgId);
	}

	public AddressProvider(BaseActivity content, Handler handler) {
		super(content, handler, 100);
	}

	public void getLowerLevelAddressReferTo(String contextid) {
		
		WAReqActionVO actionVO = WAReqActionVO.createCommonActionVO("getLowerLevelAddressReferTo");
		
		actionVO.addPar("contextid", contextid);
		actionVO.addPar("condition", "");
		request(CommonServers.getServerAddress(context) + CommonServers.SERVER_SERVLET_WA, 
				ActionType.CID_QDBS, actionVO, this);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void onRequested(WARequestVO vo) {

		List<WAReqActionVO> listAction = vo.getReqComponentVO("WA00038").actionList;
		Map dataMap = new HashMap();
		ExceptionEncapsulationVO partExceptionvo = new ExceptionEncapsulationVO();
		ExceptionEncapsulationVO flagpartExceptionvo = new ExceptionEncapsulationVO();
		AddressListVO att = null;
		for (WAReqActionVO action : listAction) {
			WAResActionVO resVO = action.resActionVO;
			String actionType = action.getActiontype();
			if (resVO.flag == 0) {
				try {
						WAResStructVO structVO = resVO.responseList.get(0);
						Map<String, Map> data = (Map<String, Map>) structVO.returnValue.get(0);
						att = new AddressListVO();
						Map dataM = data.get("addresslist");
						att.setAttributes(dataM);
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
		if (null != att)
			dataMap.put("addresslist", att);
		if (dataMap.size() == 1) 	// 1个action完成正确的情况
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
