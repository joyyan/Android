package bupt.ygj.datacollector.elementview;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import bupt.ygj.datacollector.R;
import bupt.ygj.datacollector.data.AbsFieldValue;
import bupt.ygj.datacollector.data.ElementDataVO;

public class CFDatePickerView extends AbsCommonFormView{
	
		public static final int DATEMODE = 1;
		public static final int DATETIMEMODE = 2;
		public static final int YEARMODE = 3;
		public static final int MONTHMODE = 4;
		public static final int YEARMONTHMODE = 5;
		public static final int BEGINDATE = 6;
		public static final int ENDDATE = 7;
		public static final int LITERALDATE = 8;
	
	private DateTimePickerDialog dateTimePickerDialog;
	private Context context;
	private View rootView = null;
	protected TextView eView = null;
	protected int mode;
	
	public CFDatePickerView(Context context, int mode,ElementDataVO viewAttribute) {
		super(context, viewAttribute);
		this.context = context;
		this.mode = mode;
		loadResource(R.layout.cf_view_datetime);
	}

	@Override
	public AbsFieldValue getValue() {
		String value = "";
		switch (mode) {
		case CFDatePickerView.YEARMODE:
		case CFDatePickerView.MONTHMODE:
		case CFDatePickerView.YEARMONTHMODE:
			value =  eView.getText().toString();
			break;
		case CFDatePickerView.DATETIMEMODE:
			value =  eView.getText().toString();
			if(value != null && !value.equals("")) {
				value = value + ":00";
			}
			break;
		case CFDatePickerView.BEGINDATE:
		case CFDatePickerView.DATEMODE:
		case CFDatePickerView.LITERALDATE:
			value =  eView.getText().toString();
			if(value != null && !value.equals(""))
				value = value + " " + "00:00:00";
			break;
		case CFDatePickerView.ENDDATE:
			value =  eView.getText().toString();
			if(value != null && !value.equals(""))
				value = value + " " + "23:59:59";
			break;
		default:
			value = "";
		}
		
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
			switch (mode) {
			case CFDatePickerView.DATEMODE:
			case CFDatePickerView.BEGINDATE:
			case CFDatePickerView.ENDDATE:
			case CFDatePickerView.LITERALDATE:
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				Date date1 = sdf1.parse(defaultString);
				defaultString = sdf1.format(date1);
				break;
			case CFDatePickerView.YEARMODE:
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
				Date date2 = sdf2.parse(defaultString);
				defaultString = sdf2.format(date2);
				break;
			case CFDatePickerView.MONTHMODE:
				SimpleDateFormat sdf3 = new SimpleDateFormat("MM");
				Date date3 = sdf3.parse(defaultString);
				defaultString = sdf3.format(date3);
				break;
			case CFDatePickerView.YEARMONTHMODE:
				SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM");
				Date date4 = sdf4.parse(defaultString);
				defaultString = sdf4.format(date4);
				break;
			case CFDatePickerView.DATETIMEMODE:
				SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date5 = sdf5.parse(defaultString);
				defaultString = sdf5.format(date5);
				break;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		eView.setText(defaultString);	
	}

	@Override
	public void setTitle(String title) {
		TextView tv = (TextView)this.findViewById(R.id.cfViewName);
		tv.setText(title);
	}
	
	private void getDatePickerDialog() {
		dateTimePickerDialog = new DateTimePickerDialog(context,this.getItemKey(),groupView);
		dateTimePickerDialog.dateTimePicKDialog(eView, mode);
	}

	
	protected void loadResource(int sourceid) {
		rootView = this.layoutInflater.inflate(sourceid, null);
		eView = (TextView)rootView.findViewById(R.id.cfViewTextEdit);
		eView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getFocus(rootView);
				if(isEdit()) {
					getDatePickerDialog();
				}
			}
			
		});
		this.addView(rootView);
		
	}
}
