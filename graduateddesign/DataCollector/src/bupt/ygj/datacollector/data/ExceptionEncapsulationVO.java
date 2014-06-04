package bupt.ygj.datacollector.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 多actiontype情况下的部分aciton异常信息
 * @author cuihd
 *
 */
public class ExceptionEncapsulationVO {

	/**
	 * 出错信息的列表(数据解析错误)
	 */
	private List<String> messageList = new ArrayList<String>();
	private List<String> flagmessageList = new ArrayList<String>();
	/**
	 * 正常信息业务数据
	 */
	private Map map;
	
	public List<String> getFlagmessageList() {
		return flagmessageList;
	}

	public void setFlagmessageList(List<String> flagmessageList) {
		this.flagmessageList = flagmessageList;
	}

	public List<String> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<String> messageList) {
		this.messageList = messageList;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

}
