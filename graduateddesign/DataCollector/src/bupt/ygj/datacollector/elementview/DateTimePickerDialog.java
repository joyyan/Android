package bupt.ygj.datacollector.elementview;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import bupt.ygj.datacollector.view.CommonFormGroupView;

public class DateTimePickerDialog extends LinearLayout implements OnDateChangedListener,
		OnTimeChangedListener {

	private DatePicker datePicker;
	private TimePicker timePicker;
	private AlertDialog ad;
	private String dateTime;
	private String initDateTime;
	private Context context;
	private DatePickerDialog yearPicker;
	private DatePickerDialog monthPicker;
	private DatePickerDialog yearmonthPicker;
	private CommonFormGroupView groupView;
	private String itemkey;

	public DateTimePickerDialog(Context context,String itemkey,CommonFormGroupView groupView) {
		super(context);
		this.context = context;
		this.itemkey = itemkey;
		this.groupView = groupView;
	}

	public DateTimePickerDialog init(DatePicker datePicker, TimePicker timePicker) {
		Calendar calendar = Calendar.getInstance();
		
		initDateTime = calendar.get(Calendar.YEAR) + "-"
				+ calendar.get(Calendar.MONTH) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH) + " "
				+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
				+ calendar.get(Calendar.MINUTE) + calendar.get(Calendar.SECOND);
		datePicker.init(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), this);
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
		setOrientation(VERTICAL);
		addView(datePicker);
		addView(timePicker);
		return this;
	}

	public AlertDialog dateTimePicKDialog(final TextView dateTimeTextEdite,int type) {
		Calendar c = Calendar.getInstance();
		switch (type) {
		case CFDatePickerView.DATEMODE:
		case CFDatePickerView.BEGINDATE:
		case CFDatePickerView.ENDDATE:
		case CFDatePickerView.LITERALDATE:
			createDateView(c,dateTimeTextEdite);
			return null;
		case CFDatePickerView.YEARMODE:
			createYearView(c,dateTimeTextEdite);
			return null;
		case CFDatePickerView.MONTHMODE:
			createMonthView(c,dateTimeTextEdite);
			return null;
		case CFDatePickerView.YEARMONTHMODE:
			createYearMonthView(c,dateTimeTextEdite);
			return null;
		case 0:				//只显示时间暂时还没用到
			createTimeView(c,dateTimeTextEdite);
			return null;
		case CFDatePickerView.DATETIMEMODE:
			return createYearMonthView(dateTimeTextEdite);
		}
		return null;
	}
	
	private void createTimeView(Calendar c,final TextView dateTimeTextEdite) {
		new TimePickerDialog(context,
				new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker timePicker,int hourOfDay, int minute) {
						Calendar calendar = Calendar.getInstance();
						calendar.set(Calendar.YEAR, Calendar.MONTH,Calendar.DAY_OF_MONTH,timePicker.getCurrentHour(),timePicker.getCurrentMinute());
						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
						dateTime = sdf.format(calendar.getTime());
						dateTimeTextEdite.setText(dateTime);
						groupView.setSamekeyValue(itemkey,null, dateTime,groupView.isIsheaderGroupView());
					}
				}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
				true).show();
	}
	
	private AlertDialog createYearMonthView(final TextView dateTimeTextEdite) {
		datePicker = new DatePicker(context);
		timePicker = new TimePicker(context);
		DateTimePickerDialog dateTimeLayout = init(datePicker, timePicker);
//		timePicker.setIs24HourView(false);
		timePicker.setOnTimeChangedListener(this);

		ad = new AlertDialog.Builder(context)
				.setTitle(initDateTime)
				.setView(dateTimeLayout)
				.setPositiveButton("设置",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int whichButton) {
								dateTimeTextEdite.setText(dateTime);
								groupView.setSamekeyValue(itemkey,null, dateTime,groupView.isIsheaderGroupView());
							}
						})
				.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int whichButton) {
								//dateTimeTextEdite.setText("");
							}
						}).show();
		onDateChanged(null, 0, 0, 0);
		return ad;
	}
	
	private void createDateView(Calendar c,final TextView dateTimeTextEdite) {
		new DatePickerDialog(context,
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker datePicker, int year,int monthOfYear, int dayOfMonth) {
						Calendar calendar = Calendar.getInstance();
						calendar.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						dateTime = sdf.format(calendar.getTime());
//						GetWeek getWeek = new GetWeek();
//						getWeek.setpTime(dateTime);
//						String week = getWeek.getWeek();
						dateTimeTextEdite.setText(dateTime);
						groupView.setSamekeyValue(itemkey,null, dateTime,groupView.isIsheaderGroupView());
					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DATE)).show();
	}
	
	private void createYearView(Calendar c,final TextView dateTimeTextEdite) {
		yearPicker = new YearPickerDialog(context,
				new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker datePicker, int year,int monthOfYear, int dayOfMonth) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				dateTime = sdf.format(calendar.getTime());
				dateTimeTextEdite.setText(dateTime);
				groupView.setSamekeyValue(itemkey,null, dateTime,groupView.isIsheaderGroupView());
			}
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),c.get(Calendar.DATE));
		yearPicker.setTitle(c.get(Calendar.YEAR) + "年"); 
		yearPicker.show();
		
		DatePicker dp = findDatePicker((ViewGroup) yearPicker.getWindow().getDecorView()); 
		if (dp != null) {
			Class clazz=dp.getClass();
        	Field f;
			try {
				f = clazz.getDeclaredField("mDaySpinner" );
				f.setAccessible(true );  
				LinearLayout l= (LinearLayout)f.get(dp);   
				l.setVisibility(View.GONE);
				
				f = clazz.getDeclaredField("mMonthSpinner" );
				f.setAccessible(true );  
				LinearLayout l1= (LinearLayout)f.get(dp);   
				l1.setVisibility(View.GONE);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
				try {
					f = clazz.getDeclaredField("mDayPicker" );
					f.setAccessible(true );  
					LinearLayout l= (LinearLayout)f.get(dp);   
					l.setVisibility(View.GONE);
					
					f = clazz.getDeclaredField("mMonthPicker" );
					f.setAccessible(true );  
					LinearLayout l1= (LinearLayout)f.get(dp);   
					l1.setVisibility(View.GONE);
				} catch (SecurityException e1) {
					e1.printStackTrace();
				} catch (NoSuchFieldException e1) {
					e1.printStackTrace();
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}  
		} 
	}
	
	private void createMonthView(Calendar c,final TextView dateTimeTextEdite) {
		monthPicker = new MonthPickerDialog(context,
				new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker datePicker, int year,int monthOfYear, int dayOfMonth) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
				SimpleDateFormat sdf = new SimpleDateFormat("MM");
				dateTime = sdf.format(calendar.getTime());
				dateTimeTextEdite.setText(dateTime);
				groupView.setSamekeyValue(itemkey,null, dateTime,groupView.isIsheaderGroupView());
			}
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),c.get(Calendar.DATE));
		monthPicker.setTitle(c.get(Calendar.MONTH)+1 + "月"); 
		monthPicker.show();
		
		DatePicker dp = findDatePicker((ViewGroup) monthPicker.getWindow().getDecorView()); 
		if (dp != null) {
			Class clazz=dp.getClass();
			Field f;
			try {
				f = clazz.getDeclaredField("mYearSpinner" );
				f.setAccessible(true );  
				LinearLayout l= (LinearLayout)f.get(dp);   
				l.setVisibility(View.GONE);
				
				f = clazz.getDeclaredField("mDaySpinner" );
				f.setAccessible(true );  
				LinearLayout l1= (LinearLayout)f.get(dp);   
				l1.setVisibility(View.GONE);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
				try {
					f = clazz.getDeclaredField("mDayPicker" );
					f.setAccessible(true );  
					LinearLayout l= (LinearLayout)f.get(dp);   
					l.setVisibility(View.GONE);
					
					f = clazz.getDeclaredField("mMonthPicker" );
					f.setAccessible(true );  
					LinearLayout l1= (LinearLayout)f.get(dp);   
					l1.setVisibility(View.GONE);
				} catch (SecurityException e1) {
					e1.printStackTrace();
				} catch (NoSuchFieldException e1) {
					e1.printStackTrace();
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}  
		} 
	}
	
	private void createYearMonthView(Calendar c,final TextView dateTimeTextEdite) {
		yearmonthPicker = new YearMonthPickerDialog(context,
				new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker datePicker, int year,int monthOfYear, int dayOfMonth) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				dateTime = sdf.format(calendar.getTime());
				dateTimeTextEdite.setText(dateTime);
				groupView.setSamekeyValue(itemkey,null, dateTime,groupView.isIsheaderGroupView());
			}
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),c.get(Calendar.DATE));
		yearmonthPicker.setTitle(c.get(Calendar.YEAR) + "年" +(c.get(Calendar.MONTH)+1) + "月"); 
		yearmonthPicker.show();
		
		DatePicker dp = findDatePicker((ViewGroup) yearmonthPicker.getWindow().getDecorView()); 
		if (dp != null) {
			Class clazz=dp.getClass();
			Field f;
			try {
				f = clazz.getDeclaredField("mDaySpinner" );
				f.setAccessible(true );  
				LinearLayout l1= (LinearLayout)f.get(dp);   
				l1.setVisibility(View.GONE);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
				try {
					f = clazz.getDeclaredField("mDayPicker" );
					f.setAccessible(true );  
					LinearLayout l= (LinearLayout)f.get(dp);   
					l.setVisibility(View.GONE);
				} catch (SecurityException e1) {
					e1.printStackTrace();
				} catch (NoSuchFieldException e1) {
					e1.printStackTrace();
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}  
		} 
	}
	
	private DatePicker findDatePicker(ViewGroup group) { 
	    if (group != null) { 
	        for (int i = 0, j = group.getChildCount(); i < j; i++) { 
	            View child = group.getChildAt(i); 
	            if (child instanceof DatePicker) { 
	                return (DatePicker) child; 
	            } else if (child instanceof ViewGroup) { 
	                DatePicker result = findDatePicker((ViewGroup) child); 
	                if (result != null) 
	                    return result; 
	            } 
	        } 
	    } 
	    return null; 
	}  

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		onDateChanged(null, 0, 0, 0);
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(datePicker.getYear(), datePicker.getMonth(),
				datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
				timePicker.getCurrentMinute());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		dateTime = sdf.format(calendar.getTime());
		ad.setTitle(dateTime);
	}
	
	class YearPickerDialog extends DatePickerDialog { 
		 
	    public YearPickerDialog(Context context, 
	            OnDateSetListener callBack, int year, int monthOfYear, 
	            int dayOfMonth) { 
	        super(context, callBack, year, monthOfYear, dayOfMonth); 
//	        this.setButton(DialogInterface.BUTTON_POSITIVE, "111", (Message)null);
	    } 
	 
	    @Override 
	    public void onDateChanged(DatePicker view, int year, int month, int day) { 
//	        view.init(year, month, day, this);  
	        yearPicker.setTitle(year + "年"); 
	    }
	} 
	class MonthPickerDialog extends DatePickerDialog { 
		
		public MonthPickerDialog(Context context, 
				OnDateSetListener callBack, int year, int monthOfYear, 
				int dayOfMonth) { 
			super(context, callBack, year, monthOfYear, dayOfMonth); 
		} 
		
		@Override 
		public void onDateChanged(DatePicker view, int year, int month, int day) { 
			monthPicker.setTitle(month+1 + "月"); 
		}
	} 
	class YearMonthPickerDialog extends DatePickerDialog { 
		
		public YearMonthPickerDialog(Context context, 
				OnDateSetListener callBack, int year, int monthOfYear, 
				int dayOfMonth) { 
			super(context, callBack, year, monthOfYear, dayOfMonth); 
		} 
		
		@Override 
		public void onDateChanged(DatePicker view, int year, int month, int day) { 
			yearmonthPicker.setTitle(year + "年" + (month+1) + "月"); 
		}
	} 
}
