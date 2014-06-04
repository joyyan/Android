package bupt.ygj.datacollector.datarequester;

import android.os.Handler;
import android.os.Message;
import wa.android.common.activity.BaseActivity;
import wa.android.common.network.RequestListener;
import wa.android.common.network.WAReqActionVO;
import wa.android.common.network.WARequestVO;

public abstract class WAVORequester implements RequestListener{
	
	//通用的请求错误消息ID
	public static final int MSG_REQUEST_FAILED = -10;
	//通用的数据错误消息ID
	public static final int MSG_DATA_FAILED = -20;
	
	protected BaseActivity context = null;
	
	protected Handler handler = null;
	protected int msgId = -1;
	
	public WAVORequester(BaseActivity context, Handler handler, int msgId) {
		this.context = context;
		this.handler = handler;
		this.msgId = msgId;
	}
	
	public Message makeMessage(int msgId, Object obj){
		Message msg = new Message();
		msg.what = msgId;
		msg.obj = obj;
		return msg;
	}
	
	public void request(String url, String componentId, WAReqActionVO actionVO, RequestListener listener){
		context.request(url, componentId, actionVO, listener);
	}
	
	public void request(String url, WARequestVO requestVO) {
		context.request(url, requestVO);
	}
	
	@Override
	public void onRequestFailed(int code) {
		Message msg = makeMessage(WAVORequester.MSG_REQUEST_FAILED, null);
		handler.sendMessage(msg);
	}
}
