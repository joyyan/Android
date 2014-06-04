package bupt.ygj.datacollector.elementview;

import java.util.Map;

import wa.android.common.activity.BaseActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import bupt.ygj.datacollector.R;
import bupt.ygj.datacollector.activity.AddressReferActivity;
import bupt.ygj.datacollector.data.AbsFieldValue;
import bupt.ygj.datacollector.data.AddressVO;
import bupt.ygj.datacollector.data.ElementDataVO;
import bupt.ygj.datacollector.data.ExceptionEncapsulationVO;
import bupt.ygj.datacollector.dataprovider.AddressProvider;

@SuppressLint("ViewConstructor")
public class CFAddressView extends AbsCommonFormView {

	public ProgressDialog progressDlg;
	private View rootView = null;
	private TextView state;
	private TextView city;
	private TextView area;
	private EditText addressno;
	private EditText mailcode;
	
	private AddressVO statevo;
	private AddressVO cityvo;
	private AddressVO areavo;
	
	private String titlename = "";
	
	private int viewid = 0;		
	
	private ETextWatcher addrcode = new ETextWatcher(4,this);	//街道号码edittext 的listener
	private ETextWatcher mailrcode = new ETextWatcher(5,this);	//街道号码edittext 的listener
	
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Map map = (Map) msg.obj;
				BaseActivity context = (BaseActivity) getContext();
				context.putGlobalVariable("addresslist",map.get("addresslist"));
				Intent i = new Intent(getContext(), AddressReferActivity.class);
				i.putExtra("titlename", titlename);
				((BaseActivity)CFAddressView.this.getContext()).startActivityForResult(i, viewid);
				progressDlg.dismiss();
				break;
			case 4:
				Map mapforerror = (Map) msg.obj;
				ExceptionEncapsulationVO ex = (ExceptionEncapsulationVO) mapforerror
						.get("exception");
				Toast.makeText(getContext(), ex.getMessageList().get(0), Toast.LENGTH_SHORT).show();
				progressDlg.dismiss();
				break;
			case 5:
				Map mapforflag = (Map) msg.obj;
				ExceptionEncapsulationVO exflge = (ExceptionEncapsulationVO) mapforflag
						.get("flagexception");
				Toast.makeText(getContext(),exflge.getFlagmessageList().get(0), Toast.LENGTH_SHORT).show();
				progressDlg.dismiss();
				break;
			case -10: 
				Toast.makeText(getContext(),"网络错误", Toast.LENGTH_SHORT).show();
				progressDlg.dismiss();
				break;
			}
		}
	};
	
	
	public CFAddressView(Context context, int mode) {
		super(context, mode);
		float unit = getResources().getDisplayMetrics().density;
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, (int)(unit*198)));
		this.setGravity(Gravity.CENTER_VERTICAL);
		init();
	}
	
	public CFAddressView(Context context, ElementDataVO viewAttribute) {
		super(context, viewAttribute);
		float unit = getResources().getDisplayMetrics().density;
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, (int)(unit*198)));
		this.setGravity(Gravity.CENTER_VERTICAL);
		init();
	}
	
	private void init() {
		progressDlg = new ProgressDialog(getContext());
		progressDlg.setMessage(getResources().getString(R.string.dataLoading));
		progressDlg.setIndeterminate(true);
		progressDlg.setCancelable(false);
		loadResource(R.layout.cf_view_address);
	}

	@Override
	public AbsFieldValue getValue() {
		try{
			String addno = addressno.getText().toString();
			String code =mailcode.getText().toString();
			String stateid = "";
			if(null != statevo) 
				stateid = statevo.getId();
			String cityid ="";
			if(null != cityvo)
				cityid = cityvo.getId();
			String areaid = "";
			if(null != areavo)
				areaid = areavo.getId();
			AbsFieldValue ret = new FieldValueAddress(getItemKey(), "0001Z010000000079UJJ",stateid,
					cityid,areaid,addno,code);
			return ret;
		} catch(Exception e) {
			return  null;
		}
	}

	@Override
	public void setDefaultValue(String defaultPk, String defaultString) {
		//地址类型不支持默认值
	}
	
	/**
	 * @param index 1 表示省份，2表示市，3，表示区县
	 */
	public void setSameValue(AddressVO data, int index,String text) {
		if(index == 1) {
			statevo = data;
			state.setText(data.getName());
		} else if(index == 2) {
			cityvo = data;
			city.setText(data.getName());
		} else if(index == 3) {
			areavo = data;
			area.setText(data.getName());
		} else if(index == 4) {
			addressno.setText(text);
			addressno.setSelection(text.length());
		} else if(index == 5) {
			mailcode.setText(text);
			mailcode.setSelection(text.length());
		}
	}
	
	@Override
	public void setTitle(String title){
		TextView tv = (TextView)this.findViewById(R.id.cfViewName);
		tv.setText(title);
	}
	
	protected void loadResource(int sourceid) {
		rootView = this.layoutInflater.inflate(sourceid, null);
		state = (TextView)rootView.findViewById(R.id.state);
		state.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getFocus(rootView);
				viewid = state.getId();
				titlename = "省";
				getAddress("0001Z010000000079UJJ");	//中国初始值id
				
			}
		});
		city = (TextView)rootView.findViewById(R.id.city);
		city.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getFocus(rootView);
				String text = (String) state.getText();
				if(text != null && !text.equals("")) {
					viewid = city.getId();
					titlename = "市";
					getAddress(statevo.getId());
				}
			}
		});
		area = (TextView)rootView.findViewById(R.id.area);
		area.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getFocus(rootView);
				String text = (String) city.getText();
				if(text != null && !text.equals("")) {
					viewid = area.getId();
					titlename = "区/县";
					getAddress(cityvo.getId());
				}
			}
		});
		addressno = (EditText)rootView.findViewById(R.id.addressno);
		mailcode = (EditText)rootView.findViewById(R.id.mailcode);
		if(!isEdit()) {
			addressno.setEnabled(false);
			mailcode.setEnabled(false);
		} else {
			addressno.addTextChangedListener(addrcode);
			mailcode.addTextChangedListener(mailrcode);
		}
		this.addView(rootView);
	}
	
	private void getAddress(String id) {
		if(isEdit()) {
			progressDlg.show();
			AddressProvider addp = new AddressProvider((BaseActivity)getContext(), handler);
			addp.getLowerLevelAddressReferTo(id);
		} else {
			addressno.setEnabled(false);
			mailcode.setEnabled(false);
		}

	}

	/**
	 * 
	 * @param data
	 * @param resultCode
	 * @param index 1 表示省份，2表示市，3，表示区县
	 */
	public void processResultIntent(AddressVO data,int resultCode,int index) {
		if(index == 1) {
			statevo = data;
			groupView.setSamekeyValueForAddress(this,data,index);
		} else if(index == 2) {
			cityvo = data;
			groupView.setSamekeyValueForAddress(this,data,index);
		} else if(index == 3) {
			areavo = data;
			groupView.setSamekeyValueForAddress(this,data,index);
		}
		
	}
	
	public TextView getState() {
		return state;
	}

	public void setState(TextView state) {
		this.state = state;
	}

	public TextView getCity() {
		return city;
	}

	public void setCity(TextView city) {
		this.city = city;
	}

	public TextView getArea() {
		return area;
	}

	public void setArea(TextView area) {
		this.area = area;
	}
	
	@Override
	public void setChangeListener(TextWatcher watcher) {
		addressno.addTextChangedListener(addrcode);		
		mailcode.addTextChangedListener(mailrcode);		
	}
	
	@Override
	public void removeChangeListener(TextWatcher watcher) {
		addressno.removeTextChangedListener(addrcode);		
		mailcode.removeTextChangedListener(mailrcode);			
	}
	
	
	class ETextWatcher implements TextWatcher {
		private int which;		//4,表示地址编码，5表示邮政编码
		private CFAddressView view;
		
		public ETextWatcher(int which,CFAddressView view) {
			this.which = which;
			this.view = view;
		}
	
		@Override
		public void afterTextChanged(Editable s) {
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String value = "";
			if(4 == which) {
				value = addressno.getEditableText().toString();
			} else if(5 == which) {
				value = mailcode.getEditableText().toString();
			}
			groupView.setSamekeyForAddressCode(view,value,which);
		}
	}
}
