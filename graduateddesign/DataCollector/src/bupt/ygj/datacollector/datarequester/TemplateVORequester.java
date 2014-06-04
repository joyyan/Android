package bupt.ygj.datacollector.datarequester;

import java.util.HashMap;
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
import bupt.ygj.datacollector.data.TemplateVO;

/**
 * 模板数据请求类
 * 
 * 包含模板数据，模板默认值，参照默认值，带出项
 * 
 * @author xubob
 *
 */
public class TemplateVORequester extends WAVORequester {
	

	public TemplateVORequester(BaseActivity context, Handler handler, int msgId) {
		super(context, handler, msgId);
	}
	
	public void request(String functioncode, String pkdoc, String winid,
			String templatepk, String ts, String timezone) {
		WAReqActionVO actionVO = WAReqActionVO.createCommonActionVO("getCFTemplateAttribute");
		
		actionVO.addPar("functioncode", functioncode);
		actionVO.addPar("pkdoc", pkdoc);
		actionVO.addPar("winid", winid);
		actionVO.addPar("templatepk", templatepk);
		actionVO.addPar("ts", ts);
		actionVO.addPar("timezone", timezone);
		
		request(CommonServers.getServerAddress(context) + CommonServers.SERVER_SERVLET_WA, 
				ActionType.CID_QDBS, actionVO, this);

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onRequested(WARequestVO vo) {
		WAReqActionVO actionVO = vo.getReqActionVO(ActionType.CID_QDBS, 0);
		WAResActionVO resVO = actionVO.resActionVO;
		if (resVO.flag == 0) {
			WAResStructVO struct = resVO.responseList.get(0);
			Map dataMap = (HashMap)(struct.returnValue.get(0));
			TemplateVO templateVO = TemplateVO.loadVO(dataMap);
			Message msg = makeMessage(msgId, templateVO);
			handler.sendMessage(msg);
		} else {
			Message msg = makeMessage(WAVORequester.MSG_DATA_FAILED, null);
			handler.sendMessage(msg);
		}
	}

	@Override
	public void onRequestFailed(int code) {
		Message msg = makeMessage(WAVORequester.MSG_REQUEST_FAILED, null);
		handler.sendMessage(msg);
	}

}
