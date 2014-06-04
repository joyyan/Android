package bupt.ygj.datacollector.elementview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.widget.TextView;
import bupt.ygj.datacollector.R;
import bupt.ygj.datacollector.data.AbsFieldValue;
import bupt.ygj.datacollector.data.ElementDataVO;

@SuppressLint("ViewConstructor")
public class CFEditSimpleView extends CFTextEditView {
	
	private View rootView = null;
	
	public CFEditSimpleView(Context context, ElementDataVO viewAttribute) {
		super(context, viewAttribute);
		loadResource(R.layout.layout_cf_view_textedit);
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

	@Override
	public void setDefaultValue(String defaultPk, String defaultString) {
		
		if(getLength() != 0) {
            if(defaultString != null && !defaultString.equals("")) {
            	int len = 0;    
            	int i = 0;
              	for(;i<defaultString.length();i++) {
              		String str = defaultString.substring(i, i+1);
              		if(str.getBytes().length > str.length()) {	//全角字符？？？？？
              			len = len + 2;
  	                } else {
  	                	len++;
  	                }
              		if(len > getLength()) {
              			break;
              		}
              	}
              	if(i > 0) {
              		defaultString = defaultString.substring(0, i);
              	}
             }
                
          eView.setText(defaultString);		
          eView.setSelection(defaultString.length());
		} else {
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
		eView = (MaxByteLengthEditText)rootView.findViewById(R.id.cfViewTextEdit);
		if(!isEdit())
			eView.setEnabled(false);
		((MaxByteLengthEditText)eView).setMaxByteLength(getLength());
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

}
