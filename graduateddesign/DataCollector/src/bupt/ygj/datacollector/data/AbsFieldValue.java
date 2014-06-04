package bupt.ygj.datacollector.data;

import wa.android.common.network.WAParValueVO;

public abstract class AbsFieldValue {
	
	protected String key = null;		//itemkey
	
	public AbsFieldValue(String key){
		this.key = key;
	}
	
	//判断该返回值是否是空，true为空，false为不空
	public boolean getRealValueEmpty() {
		
		return false;
	}

	public abstract WAParValueVO toWAParameter();
}
