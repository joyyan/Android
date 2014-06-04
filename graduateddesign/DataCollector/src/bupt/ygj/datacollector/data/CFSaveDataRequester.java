package bupt.ygj.datacollector.data;

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
import bupt.ygj.datacollector.datarequester.WAVORequester;

public class CFSaveDataRequester extends WAVORequester{
	
	public static final int MSG_CHANNEL_FAILED = -1;
	// 普通的数据请求
	public final int OK = 2;
	// 部分action请求出错
	public final int PART_ACTION_FAILED = 4;
	public final int FLAG_PART_ACTION_FAILED = 5;

	public CFSaveDataRequester(BaseActivity context, Handler handler, int msgId) {
		super(context, handler, msgId);
	}
	
	public void request(CFSaveDataVO saveData){
		request(CommonServers.getServerAddress(context) + CommonServers.SERVER_SERVLET_WA, 
				ActionType.CID_QDBS, 
				saveData.toWAParameter(),
				this);
	}

	@Override
	public void onRequested(WARequestVO vo) {
		List<WAReqActionVO> listAction = vo.getReqComponentVO("WA00038").actionList;
		Map dataMap = new HashMap();
		ExceptionEncapsulationVO partExceptionvo = new ExceptionEncapsulationVO();
		ExceptionEncapsulationVO flagpartExceptionvo = new ExceptionEncapsulationVO();
		SaveCFVOSuc rra = null;
		for (WAReqActionVO action : listAction) {
			WAResActionVO resVO = action.resActionVO;
			String actionType = action.getActiontype();
			if (resVO.flag == 0) {
				try {
					WAResStructVO structVO = resVO.responseList.get(0);
						Map<String, Map> data = (Map<String, Map>) structVO.returnValue.get(0);
						rra = new SaveCFVOSuc();
						rra.setAttributes(data.get("saveformdata"));
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
			dataMap.put("saveformdata", rra);
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
