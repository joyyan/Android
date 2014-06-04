package bupt.ygj.datacollector.elementview;

import java.util.ArrayList;
import java.util.List;

import wa.android.common.activity.BaseActivity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import bupt.ygj.datacollector.R;
import bupt.ygj.datacollector.activity.CFComboActivity;
import bupt.ygj.datacollector.data.AbsFieldValue;
import bupt.ygj.datacollector.data.ElementDataVO;
import bupt.ygj.datacollector.data.EnumValueVO;

public class CFEnumView extends AbsCommonFormView implements OnClickListener {

	private View rootView = null;
	private TextView eView = null;
	private String returnvalue = "";
	
	public CFEnumView(Context context, ElementDataVO viewAttribute) {
		super(context, viewAttribute);
		rootView = layoutInflater.inflate(R.layout.layout_cf_view_refer, null);
		rootView.setOnClickListener(this);
		addView(rootView);
		eView = (TextView) rootView.findViewById(R.id.cf_view_text_view);
		if(!isEdit()) {
			ImageView image = (ImageView) rootView.findViewById(R.id.cf_view_right_array);
			image.setVisibility(View.GONE);
		}
	}

	@Override
	public AbsFieldValue getValue() {
		return new FieldValueCommon(getItemKey(), returnvalue);
	}

	@Override
	public void setDefaultValue(String defaultPk, String defaultString) {
		returnvalue = defaultPk;
		if(defaultPk != null && viewAttribute != null) {
			String[] pks = defaultPk.split(",");
			String valueForShow = "";
			List<String> listvalue = new ArrayList<String>();
			List<EnumValueVO> elist = viewAttribute.getEnumItemList();
			if(null != pks && null != elist) {
				for(String s : pks) {
					for(int i = 0;i<elist.size();i++) {
						EnumValueVO e = elist.get(i);
						if(e.getValue() != null && e.getValue().equals(s)) {
							listvalue.add(e.getText());
						}
					}
				}
			}
			for(int i = 0;i<listvalue.size();i++) {
				if(i == (listvalue.size() - 1))
					valueForShow = valueForShow + listvalue.get(i);
				else
					valueForShow = valueForShow + listvalue.get(i) + ",";
			}
			eView.setText(valueForShow);
		}
	}

	@Override
	public void setTitle(String title) {
		TextView textView = (TextView) this.findViewById(R.id.cfViewName);
		textView.setText(title);
	}

	@Override
	public void onClick(View v) {
		getFocus(rootView);
		if(isEdit()) {	//如果可以编辑那么才去跳转
			//枚举列表和是否多选为下拉类型独有的属性
			ArrayList<EnumValueVO> enumlist = (ArrayList<EnumValueVO>)viewAttribute.getEnumItemList();
			String title = viewAttribute.getItemName();
			boolean b = viewAttribute.isMultiselected();
			
			if(null != enumlist) {
				for(EnumValueVO en : enumlist) {
					en.setIsselect(false);
				}
			}
			
			if(returnvalue != null) {
				String[] strs = returnvalue.split(",");
				if(strs != null && strs.length >0) {
					for(int i = 0 ;i<strs.length;i++) {
						String id = strs[i];
						if(id != null && enumlist != null) {
							for(EnumValueVO en : enumlist) {
								if(en.getValue() != null && en.getValue().equals(id)) {
									en.setIsselect(true);
								} 
							}
						}
					}
				}
			}
			
			Intent i = new Intent(getContext(), CFComboActivity.class);
			i.putExtra("enumlist", enumlist);
			i.putExtra("ismultiselected", b);
			i.putExtra("title", title);
			((BaseActivity) getContext()).startActivityForResult(i, getId());
		} 
	}

	@Override
	public void processResultIntent(Intent data, int resultCode) {
		if (1 == resultCode) {
			ArrayList<EnumValueVO> enumvalues = (ArrayList<EnumValueVO>) data.getSerializableExtra("backeddata");
			if (null != enumvalues && enumvalues.size() != 0) {
				String backvalue = "";
				String value = "";
				int size = enumvalues.size();
				for(int i = 0;i<size;i++) {
					EnumValueVO enumvalue = enumvalues.get(i);
					if((size - 1) == i) {
						value = value + enumvalue.getText();
						backvalue = backvalue + enumvalue.getValue();
					} else {
						value = value + enumvalue.getText() + ",";
						backvalue = backvalue + enumvalue.getValue() + ",";
					}
				}
				returnvalue = backvalue;
				eView.setText(value); //
				groupView.setSamekeyValue(this.getItemKey(), returnvalue, value,groupView.isIsheaderGroupView());
			} else {
				returnvalue = "";
				eView.setText(""); //
				groupView.setSamekeyValue(this.getItemKey(), returnvalue, "",groupView.isIsheaderGroupView());
			}
		}
	}
}
