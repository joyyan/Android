package bupt.ygj.datacollector.elementview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import bupt.ygj.datacollector.R;
import bupt.ygj.datacollector.data.AbsFieldValue;
import bupt.ygj.datacollector.data.ElementDataVO;

/**
 * 百分比类型，支持精度不支持长度属性
 * @author cuihd
 *
 */
@SuppressLint("ViewConstructor")
public class CFEditPercentView extends CFTextEditView implements TextWatcher {

	private View rootView = null;
	
	public CFEditPercentView(Context context, ElementDataVO viewAttribute) {
		super(context, viewAttribute);
		loadResource(R.layout.cf_view_percent);
		eView.addTextChangedListener(this);
	}

	@Override
	public AbsFieldValue getValue() {
		String value =  eView.getText().toString();
		try{
			AbsFieldValue ret = new FieldValueCommon(getItemKey(), value);
			return ret;
		} catch(Exception e) {
			return null;
		}
	}

	@Override
	public void setDefaultValue(String defaultPk, String defaultString) {
		try {
			Double d = Double.valueOf(defaultString);
			if(d.isNaN() || d.isInfinite()){
				eView.setText("");
				return ;
			}
			if (defaultString != null && !defaultString.equals("") ) {
				if(getPrecision() >= 0 && getPrecision() < 999) {
				int dotIndex = defaultString.indexOf(".");
				if(dotIndex >= 0) {
					String precisionStr = defaultString.substring(dotIndex + 1);
					if (precisionStr.length() > getPrecision()) { // 如果超出了精度
						if(getPrecision() > 0) 
							defaultString = defaultString.substring(0, dotIndex + getPrecision() + 1);
						else
							defaultString = defaultString.substring(0, dotIndex + getPrecision());
					}
				}
				eView.setText(defaultString);	
				processPrecision();
				String value =  eView.getText().toString();
				eView.setSelection(value.length());
			}  else {
				eView.setText(defaultString);
				eView.setSelection(defaultString.length());
			}
			}
		} catch(Exception e)  {
			e.printStackTrace();
			eView.setText("");
		}	
	}
	
	@Override
	public void setTitle(String title){
		TextView tv = (TextView)this.findViewById(R.id.cfViewName);
		tv.setText(title);
	}
	
	
	protected void loadResource(int sourceid) {
		rootView = this.layoutInflater.inflate(sourceid, null);
		eView = (PrecisionEditText)rootView.findViewById(R.id.cfViewTextEdit);
		if(!isEdit()) {
			eView.setEnabled(false);
		} 
		eView.setOnFocusChangeListener(new View.OnFocusChangeListener() {  
		      
		    @Override  
		    public void onFocusChange(View v, boolean hasFocus) {  
		        if(hasFocus){//获得焦点  
		        	
		        }else{//失去焦点  
		        	boolean b = processPrecision();
		        	if(b) {
		        		groupView.textChangeLisenter(CFEditPercentView.this);
		        	}
		        }
		    }             
		});  
		((PrecisionEditText)eView).setMaxPrecision(getPrecision());	//百分比控制精度，并且不控制长度。
		this.addView(rootView);
	}

	@Override
	public void setChangeListener(TextWatcher watcher) {
		eView.addTextChangedListener(watcher);		
	}
	
	@Override
	public void removeChangeListener(TextWatcher watcher) {
		eView.removeTextChangedListener(watcher);		
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(null != groupView) {	//如果支持公式计算
			groupView.textChangeLisenter(this);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

}
