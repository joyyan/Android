package bupt.ygj.datacollector.elementview;

import wa.android.common.network.WAParValueVO;
import bupt.ygj.datacollector.data.AbsFieldValue;

public class FieldValueNumber extends AbsFieldValue{
	private Double number = null;
	private String value = null;

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public FieldValueNumber(String key, String value) throws NumberFormatException {
		super(key);
		this.value = value;
		number = Double.valueOf(value);
	}
	@Override
	public WAParValueVO toWAParameter() {
		WAParValueVO valueVO = new WAParValueVO();
		valueVO.addField("itemkey", key);
		valueVO.addField("realvalue", number.toString());
		return valueVO;
	}
	public Double getNumber() {
		return number;
	}

	public boolean getRealValueEmpty() {
		if(number != null && number.toString() != null && !number.toString().equals(""))
			return false;
		else
			return true;
	}
}
