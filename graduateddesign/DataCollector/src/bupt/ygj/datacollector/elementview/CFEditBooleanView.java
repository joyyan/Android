package bupt.ygj.datacollector.elementview;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import bupt.ygj.datacollector.R;
import bupt.ygj.datacollector.data.AbsFieldValue;
import bupt.ygj.datacollector.data.ElementDataVO;

public class CFEditBooleanView extends AbsCommonFormView{
	
	private View rootView = null;
//	protected CheckBox eView = null;
	protected ImageView eView = null;
	private boolean vstatus;
	
	public CFEditBooleanView(Context context, int mode) {
		super(context, mode);
		loadResource(R.layout.layout_cf_view_booleandit);
	}
	
	public CFEditBooleanView(Context context, ElementDataVO viewAttribute) {
		super(context, viewAttribute);
		loadResource(R.layout.layout_cf_view_booleandit);
	}

	@Override
	public AbsFieldValue getValue() {
		String strValue = "N";		//具体值需要再进一步确认
		if(vstatus) 
			strValue = "Y";
		try{
			AbsFieldValue ret = new FieldValueBoolean(getItemKey(), strValue);
			return ret;
		} catch(Exception e) {
			return null;
		}
	}

	@Override
	public void setDefaultValue(String defaultPk, String defaultString) {
		if(defaultString != null && defaultString.equals("Y")) {
			vstatus = true;
			eView.setVisibility(View.VISIBLE);	
		}
		else {
			vstatus = false;
			eView.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void setTitle(String title){
		TextView tv = (TextView)this.findViewById(R.id.cfViewName);
		tv.setText(title);
	}
	
	
	protected void loadResource(int sourceid) {
		rootView = this.layoutInflater.inflate(sourceid, null);
		eView = (ImageView)rootView.findViewById(R.id.cfViewTextEdit);
		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getFocus(rootView);
				if(isEdit()) {
					int status = eView.getVisibility();
					if(status == View.GONE) {
						eView.setVisibility(View.VISIBLE);
						eView.setFocusable(true);
						vstatus = true;
						groupView.setSamekeyValue(getItemKey(),null,"Y",groupView.isIsheaderGroupView());
					} else if(status == View.VISIBLE) {
						eView.setVisibility(View.GONE);
						vstatus = false;
						groupView.setSamekeyValue(getItemKey(),null,"N",groupView.isIsheaderGroupView());
					}
					
				}
			}
			
		});
		this.addView(rootView);
	}

}
