package bupt.ygj.datacollector.elementview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextWatcher;
import android.widget.EditText;
import bupt.ygj.datacollector.data.AbsFieldValue;
import bupt.ygj.datacollector.data.ElementDataVO;

@SuppressLint("ViewConstructor")
public class CFTextEditView extends AbsCommonFormView {
	
	protected EditText eView = null;

	public CFTextEditView(Context context, ElementDataVO viewAttribute) {
		super(context, viewAttribute);
	}

	@Override
	public AbsFieldValue getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDefaultValue(String defaultPk, String defaultString) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTitle(String title) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void setChangeListener(TextWatcher watcher) {
		eView.addTextChangedListener(watcher);		
	}
	
	@Override
	public void removeChangeListener(TextWatcher watcher) {
		eView.removeTextChangedListener(watcher);		
	}
	
	/**
	 * 在设置默认值和失去焦点时，格式化精度
	 * @return true 表示需要格式化，false表示不需要格式化
	 */
	protected boolean processPrecision() {
		int precision = getPrecision();
		if(precision > 0 && precision < 999) {
        	String value =  eView.getText().toString();
        	if(value != null && !"".equals(value)) {
        		if(value.contains(".")) {	//输入的数据中包含有小数点
        			int index = value.indexOf(".") + 1;
        			String dotfollow = value.substring(index);
        			if(dotfollow.length() < precision) {		//小数点后的位数小于精度长度
        				for(int i = 0;i<precision - dotfollow.length();i++) {
        					value = value + "0";
        				}
        				eView.setText(value);
        				return true;
        			}
        		} else {
        			for(int i = 0;i<precision;i++) {
        				if(i == 0) {
        					value = value + ".0";
        				} else {
        					value = value + "0";
        				}
    				}
		        	eView.setText(value);
		        	return true;
        		}
        	}
        }  
		return false;
	}

}
