package bupt.ygj.datacollector.elementview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import bupt.ygj.datacollector.R;
import bupt.ygj.datacollector.data.AbsFieldValue;
import bupt.ygj.datacollector.data.ElementDataVO;

/**
 * 整数类型，无精度无长度属性
 * @author cuihd
 *
 */
@SuppressLint("ViewConstructor")
public class CFEditIntegerView extends CFTextEditView {
	
	private View rootView = null;

	public CFEditIntegerView(Context context,ElementDataVO viewAttribute) {
		super(context, viewAttribute);
		loadResource(R.layout.cf_view_integer);
	}

	
	@Override
	public void setDefaultValue(String defaultPk, String defaultString) {
		try{
			Double i = Double.valueOf(defaultString);
			if(i.isNaN() || i.isInfinite()){
				eView.setText("");
				return ;
			}
			if(defaultString.indexOf(".") > 0)
				defaultString = (String) defaultString.subSequence(0, defaultString.indexOf("."));
			eView.setText(defaultString);
			eView.setSelection(defaultString.length());
		}catch(Exception e)  {
			e.printStackTrace();
			eView.setText("");		//防止
			
		}
	}
	
	@Override
	public AbsFieldValue getValue() {
		String value =  eView.getText().toString();
		try{
			AbsFieldValue ret = new FieldValueCommon(getItemKey() , value);
			return ret;
		} catch(Exception e) {
			return null;
		}
	}
	
	protected void loadResource(int sourceid) {
		rootView = this.layoutInflater.inflate(sourceid, null);
		eView = (EditText)rootView.findViewById(R.id.cfViewTextEdit);
		eView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
		if(!isEdit()) {
			eView.setEnabled(false);
		} 
		eView.addTextChangedListener(this);
		this.addView(rootView);
	}
	

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(null != groupView) {	//如果支持公式计算
			groupView.textChangeLisenter(this);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		
	}
	
	@Override
	public void setTitle(String title) {
		TextView tv = (TextView)this.findViewById(R.id.cfViewName);
		tv.setText(title);
	}
}
