package bupt.ygj.datacollector.elementview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;
import bupt.ygj.datacollector.R;
import bupt.ygj.datacollector.data.AbsFieldValue;
import bupt.ygj.datacollector.data.ElementDataVO;

/**
 * 数值类型
 * @author cuihd
 *
 */

@SuppressLint("ViewConstructor")
public class CFEditDoubleView extends CFTextEditView {

	private View rootView = null;
	
	public CFEditDoubleView(Context context, ElementDataVO viewAttribute) {
		super(context, viewAttribute);
		loadResource(R.layout.layout_cf_view_moneydecimal);
		
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

	/**
	 * 设置默认值也需要控制精度
	 */
	@Override
	public void setDefaultValue(String defaultPk, String defaultString) {
		try {
			Double d = Double.valueOf(defaultString);
			if(d.isNaN() || d.isInfinite()){
				eView.setText("");
				return ;
			}
			if (defaultString != null && !defaultString.equals("")) {
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
		eView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		eView.setOnFocusChangeListener(new View.OnFocusChangeListener() {  
		      
		    @Override  
		    public void onFocusChange(View v, boolean hasFocus) {  
		        if(hasFocus){//获得焦点  
		        	
		        }else{//失去焦点  
		        	boolean b = processPrecision();
		        	if(b) {
		        		groupView.textChangeLisenter(CFEditDoubleView.this);
		        	}
		        }
		    }             
		});  
		if(!isEdit()) {
			eView.setEnabled(false);
		} 
		((PrecisionEditText)eView).setMaxPrecision(getPrecision());	//
		this.addView(rootView);
		eView.addTextChangedListener(this);
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
