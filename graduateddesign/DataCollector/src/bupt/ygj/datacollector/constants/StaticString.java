package bupt.ygj.datacollector.constants;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;

@SuppressLint("SimpleDateFormat")
public class StaticString {
	public static java.util.Date today = new java.util.Date();
	/** ZZZZZ */
	public static SimpleDateFormat format = new SimpleDateFormat("ZZZZZ");
	public static String tempString = format.format(today);
	public static String timezone = tempString.substring(1, 3) 
			+ tempString.substring(3);

	public static SimpleDateFormat formatDateTime2 = new SimpleDateFormat(
			"yyyy年MM月dd日 HH:mm:ss");
	
	
	public static String maclientid = "001";
	public static String type = "1";

	/** 工作项目类型3*/
	public static String workItemType = "3";
	/** Channel的请求错误消息ID */
	public final static int MSG_CHANNEL_FAILED = -1;
	/** action请求出错 */
	public final static int FLAG_ACTION_FAILED = 5;
	/** 普通的数据请求 */
	public final static int MSG_CHANNEL_OK = 0;
	public final static int MSG_CHANNEL_OK_1 = 1;

	public final static int MSG_CHANNEL_DETAIL_OK = 4;
	/** 请求日程基本信息和工作列表成功*/
	public final static int MSG_CHANNEL_DETAIL2_OK = 8;
	
	/** 获取附件数据请求 */
	public final static int MSG_ATTACH_OK = 1;
	/** 单向请求成功，即返回数据为success*/
	public final static int SIMPLE_ACTION_SUCCESS = 2;
	/** 签到、退成功请求*/
	public final static int MSG_SIGN_OK = 3;
	/** 点击日程列表条目事件*/
	public static final int MSG_DAYEVENTLIST_ADAPTER = 10;
	/** 查询地址成功*/
	public final static int MSG_ADDRESS_OK = 4;
	/** 切换日期拜访日程列表请求*/
	public final static int MSG_DAYEVENTLIST_OK = 6;
	/** 请求工作项目详情*/
	public final static int MSG_WORKLIST_DETAIL = 7;
	public final static int MSG_EVENTDETAIL_PUSH = 12;
	
	public  static final int CALENDAR_NEXTMONTH = 19;
	public  static final int CALENDAR_PREMONTH = 20;
	public static final int REQUESTCODE_NEWEVENT_CHANNELNODE = 1;
	
	public static final int RESULTCODE_SELECTCHANNELNODEACTIVITY = 2;
	public static final int REQUESTCODE_SELECTCHANNELNODEACTIVITY = 3;
	public static final int RESULTCODE_ROUTEDETAILACTIVITY = 4;
}
