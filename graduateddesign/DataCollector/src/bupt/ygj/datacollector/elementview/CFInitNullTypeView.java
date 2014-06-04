package bupt.ygj.datacollector.elementview;

import android.annotation.SuppressLint;
import android.content.Context;
import bupt.ygj.datacollector.data.AbsFieldValue;
import bupt.ygj.datacollector.data.ElementDataVO;

@SuppressLint("ViewConstructor")
public class CFInitNullTypeView extends AbsCommonFormView {
	
	public CFInitNullTypeView(Context context, ElementDataVO viewAttribute) {
		super(context, viewAttribute);
	
	}

	@Override
	public AbsFieldValue getValue() {
		
		try{
			String value = "";
			if(null != viewAttribute)
				value = viewAttribute.getDefaultValue();
			AbsFieldValue ret = new FieldValueCommon(getItemKey(),value);
			return ret;
		} catch(Exception e) {
			return null;
		}
	}

	@Override
	public void setDefaultValue(String defaultPk, String defaultString) {
		//这里不用设置默认值也可以，因为不需要显示，defaultString也就是viewAttribute.getDefaultValue();
	}
	
	@Override
	public void setTitle(String title){
		
	}
	
}
