package bupt.ygj.datacollector.elementview;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import bupt.ygj.datacollector.R;
import bupt.ygj.datacollector.data.AbsFieldValue;
import bupt.ygj.datacollector.data.ElementDataVO;

/**
 * 金额view
 * @author cuihd
 *
 */
public class CFEditMoneyView extends CFTextEditView implements TextWatcher {
	
	private View rootView = null;
	
	public CFEditMoneyView(Context context, ElementDataVO viewAttribute) {
		super(context, viewAttribute);
		loadResource(R.layout.layout_cf_view_moneydecimal);
		eView.addTextChangedListener(this);
	}

	@Override
	public AbsFieldValue getValue() {
		String value =  eView.getText().toString();
		if(null!= value)
			value = value.replaceAll(",", "");
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
	
	private String decimalFormat(String str) {
		try {
			int index = str.indexOf(".");
			str = str.substring(0, index);
			java.text.DecimalFormat df = new java.text.DecimalFormat("##,###");
	        String str1 = df.format(Double.parseDouble(str) );
	        return str1;
		} catch(Exception e) {
			return str;
		}
	}
	
	
	protected void loadResource(int sourceid) {
		rootView = this.layoutInflater.inflate(sourceid, null);
		eView = (PrecisionEditText)rootView.findViewById(R.id.cfViewTextEdit);
		eView.setOnFocusChangeListener(new View.OnFocusChangeListener() {  
		      
		    @Override  
		    public void onFocusChange(View v, boolean hasFocus) {  
		        if(hasFocus){//获得焦点  
		        	
		        }else{//失去焦点  
		        	boolean b = processPrecision();
		        	decimalFormat();
		        	if(b) {
		        		groupView.textChangeLisenter(CFEditMoneyView.this);
		        	}
		        
		        }
		    }
		});  
		eView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		if(!isEdit()) {
			eView.setEnabled(false);
		} 
		((PrecisionEditText)eView).setMaxPrecision(getPrecision());	//
		this.addView(rootView);
	}
	
	/**
	 * 对金额类型进行千位分隔符处理，小数点后的不处理
	 */
	private void decimalFormat() {
		String textvalue = eView.getText().toString();
    	textvalue = textvalue.replaceAll(",", "");
    	String dotfollow = "";
    	if(textvalue != null && !"".equals(textvalue)) {
    		if(textvalue.contains(".")) {	//输入的数据中包含有小数点
    			int index = textvalue.indexOf(".");
    			dotfollow = textvalue.substring(index);
    		}
    	}
    	textvalue = decimalFormat(textvalue);
    	
    	eView.setText(textvalue + dotfollow);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(null != groupView) {	//此处用于公式计算和相同的itemkey来赋值
			groupView.textChangeLisenter(this);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}
}
