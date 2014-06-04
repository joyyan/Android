package bupt.ygj.datacollector.datarequester;

import java.util.List;
import java.util.Map;

import bupt.ygj.datacollector.constants.ActionType;
import wa.android.common.activity.BaseActivity;
import wa.android.common.network.WAReqActionVO;
import wa.android.common.network.WARequestVO;
import wa.android.common.network.WAResActionVO;
import wa.android.common.network.WAResStructVO;
import wa.android.constants.CommonServers;
import android.os.Handler;
import android.os.Message;


public class ReferCommonRequester extends WAVORequester{
	


	public ReferCommonRequester(BaseActivity context, Handler handler, int msgId) {
		super(context, handler, msgId);
	}
	
	
	public void request(String pkOrg, String referTo, String condition, int pageIndex) {
		WAReqActionVO actionVO = WAReqActionVO.createCommonActionVO("getReferValues");
		actionVO.addPar("referto", referTo);
		actionVO.addPar("condition", condition);
		if(pkOrg == null)
			pkOrg = "";
		actionVO.addPar("pk_org", pkOrg);
		if (pageIndex == -1) {
			//不分页
			actionVO.addPar("ispaging", "N");
			actionVO.addPar("startline", "");
			actionVO.addPar("count", "");
		} else {
			//分页
			actionVO.addPar("ispagint", "Y");
			actionVO.addPar("startline", String.valueOf(pageIndex * 25 + 1));
			actionVO.addPar("count", "25");
		}
		request(CommonServers.getServerAddress(context) + CommonServers.SERVER_SERVLET_WA, 
				ActionType.CID_QDBS, actionVO, this);
	}
	



	@Override
	public void onRequested(WARequestVO vo) {
		
		WAReqActionVO actionVO = vo.getReqActionVO(ActionType.CID_QDBS, 0);
		WAResActionVO resVO = actionVO.resActionVO;
		switch (resVO.flag)	 {
		case 0:
			WAResStructVO structVO = resVO.responseList.get(0);
			Map<String, List<Map<String, String>>> dataMap = (Map<String, List<Map<String, String>>>)structVO.returnValue.get(0);
			Map map = (Map)dataMap.get("inventorylist");
			List<Map<String, String>> mapList = (List<Map<String, String>>)map.get("inventory");
			List<ReferCommonVO> voList = ReferCommonVO.loadVO(mapList);
			Message msgOK = makeMessage(msgId, voList);
			handler.sendMessage(msgOK);
			break;
		default:
			Message msgFailed = makeMessage(WAVORequester.MSG_DATA_FAILED, null);
			handler.sendMessage(msgFailed);
			break;
		}
		
	}
	
	protected String getDefedKeyOfDataList(){
		return "inventorylist";
	}
}
