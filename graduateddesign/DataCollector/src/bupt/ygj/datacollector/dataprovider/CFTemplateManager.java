package bupt.ygj.datacollector.dataprovider;

import wa.android.common.activity.BaseActivity;
import wa.android.common.utils.PreferencesUtil;
import wa.android.constants.CommonWAPreferences;
import android.os.Handler;

public class CFTemplateManager {
	
	private TemplateVORequester2 requester = null;
	private BaseActivity context = null;
	
	public CFTemplateManager(BaseActivity context, Handler handler, int msgId) {
		this.context = context;
		requester = new TemplateVORequester2(context, handler, msgId);
	}
	
	public void getCFTemplate(String functioncode, String pkdoc, String winid) {
		String requestcachepkkey = functioncode + pkdoc + winid + StaticString.timezone + "pk" + getKey();
		String requestcachetskey = functioncode + pkdoc + winid + StaticString.timezone + "ts" + getKey();
		String pkvalue = PreferencesUtil.readPreference(context,requestcachepkkey);
		String tsvalue = PreferencesUtil.readPreference(context,requestcachetskey);
		requester.request(functioncode, pkdoc, winid, pkvalue,tsvalue, StaticString.timezone);
//		requester.request(functioncode, pkdoc, winid, "","", StaticString.timezone);
	}
	
	public void getCFTemplate2(String functioncode, String pkdoc, String winid) {

		requester.setTimes(1);
		requester.request(functioncode, pkdoc, winid, "","", StaticString.timezone);
	}
	
	private String getKey() {
		String key = PreferencesUtil.readPreference(context,CommonWAPreferences.SERVER_IP) 
				+ PreferencesUtil.readPreference(context,CommonWAPreferences.SERVER_PORT) 
 			   + PreferencesUtil.readPreference(context, CommonWAPreferences.USER_NAME)
 			   + "templatevo";
		return key;
	}
	
}	
