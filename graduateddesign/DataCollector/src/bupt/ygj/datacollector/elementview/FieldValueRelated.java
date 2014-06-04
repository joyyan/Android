package bupt.ygj.datacollector.elementview;

import wa.android.common.network.WAParValueVO;
import bupt.ygj.datacollector.data.AbsFieldValue;


public class FieldValueRelated extends AbsFieldValue{

	private String value = null;
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public FieldValueRelated(String key, String value) {
		super(key);
		this.value = value;

	}

	@Override
	public WAParValueVO toWAParameter() {	
		WAParValueVO valueVO = new WAParValueVO();
		valueVO.addField("itemkey", key);
		valueVO.addField("realvalue", value);
		return valueVO;
	}
	
	public boolean getRealValueEmpty() {
		if(value != null && !value.equals(""))
			return false;
		else
			return true;
	}
	
	
}
