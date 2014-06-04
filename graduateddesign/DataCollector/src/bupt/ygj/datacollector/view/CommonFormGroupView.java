package bupt.ygj.datacollector.view;

import java.util.ArrayList;
import java.util.List;

import wa.android.libs.groupview.WAGroupView;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import bupt.ygj.datacollector.data.AddressVO;
import bupt.ygj.datacollector.elementview.AbsCommonFormView;
import bupt.ygj.datacollector.elementview.CFAddressView;
import bupt.ygj.datacollector.elementview.CFDatePickerView;
import bupt.ygj.datacollector.elementview.CFEditBooleanView;
import bupt.ygj.datacollector.elementview.CFEditDoubleView;
import bupt.ygj.datacollector.elementview.CFEditIntegerView;
import bupt.ygj.datacollector.elementview.CFEditMoneyView;
import bupt.ygj.datacollector.elementview.CFEditPercentView;
import bupt.ygj.datacollector.elementview.CFEnumView;
import bupt.ygj.datacollector.elementview.CFFileView;
import bupt.ygj.datacollector.elementview.CFPhotoView;
import bupt.ygj.datacollector.elementview.CFReferView;
import bupt.ygj.datacollector.elementview.CFTextEditView;
import bupt.ygj.datacollector.elementview.FieldValueCommon;
import bupt.ygj.datacollector.formular.Expression;
import bupt.ygj.datacollector.formular.FormularUtils;

public class CommonFormGroupView extends WAGroupView {
	
	//这个属性包含全部那4中可以进行公式计算的类型
	private List<AbsCommonFormView> listformulars = new ArrayList<AbsCommonFormView>();
	private FormularUtils formularContext1 = null;		//公式计算
	private String groupid = null;
	//为编辑项为TextView类型的字段服务（主要包括相同itemkey的录入，这里不包含地址类型）
	private List<AbsCommonFormView> viewsForTextView = new ArrayList<AbsCommonFormView>();		
	private View view;
	private boolean isheaderGroupView;		//用来标示是否是表头的viewgroup，true表示表头的，false表示表体的
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch(msg.what) {
			case 0:
				Bundle data = msg.getData();
				String key = data.getString("key");
				Double result = data.getDouble("result");
				setSamekeyValueForEditText(key,result.toString(),view);
//				setSamekeyValueForEditText(view);
				break;
			}
		}
	};
	
	public CommonFormGroupView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CommonFormGroupView(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
		formularContext1 = new FormularUtils(context,this);
	}
	
	
	public void setSamekeyValue(String itemkey,String pk,String value,boolean isheader) {
		if(isheader) {
			setSamekeyValue(itemkey,pk,value);
		} else {
			setSamekeyValueBody(itemkey,pk,value);
		}
	}
	
	/**
	 * 为显示值为TextView类型的view赋值
	 * @param itemkey
	 * @param pk
	 * @param value
	 */
	public void setSamekeyValueBody(String itemkey,String pk,String value) {
		for(int i = 0;i<this.getChildCount();i++) {
			View view = this.getChildAt(i);
			if(view instanceof CFDatePickerView) {
				CFDatePickerView dateview = (CFDatePickerView)view;
				String otherkey = dateview.getItemKey();
				if(null != itemkey && null != otherkey) {
					if(otherkey.equals(itemkey)) 
						dateview.setDefaultValue(null, value);
				}
			} else if(view instanceof CFReferView) {
				CFReferView dateview = (CFReferView)view;
				String otherkey = dateview.getItemKey();
				if(null != itemkey && null != otherkey) {
					if(otherkey.equals(itemkey)) 
						dateview.setDefaultValue(null, value);
				}
			} else if(view instanceof CFEnumView) {
				CFEnumView dateview = (CFEnumView)view;
				String otherkey = dateview.getItemKey();
				if(null != itemkey && null != otherkey) {
					if(otherkey.equals(itemkey)) 
						dateview.setDefaultValue(pk, value);
				}
			} else if(view instanceof CFEditBooleanView) {
				CFEditBooleanView dateview = (CFEditBooleanView)view;
				String otherkey = dateview.getItemKey();
				if(null != itemkey && null != otherkey) {
					if(otherkey.equals(itemkey)) 
						dateview.setDefaultValue(null, value);
				}
			} 
		}
	}
	
	public void setSamekeyValue(String itemkey,String pk,String value) {
		CommonFormLayout layoutview = (CommonFormLayout) this.getParent();
		for(int j = 0;j<layoutview.getChildCount();j++) {
			View viewTemp = layoutview.getChildAt(j);
			if(viewTemp instanceof CommonFormGroupView) {
				CommonFormGroupView formGroupView = (CommonFormGroupView)viewTemp;
				for(int i = 0;i<formGroupView.getChildCount();i++) {
					View view = formGroupView.getChildAt(i);
					if(view instanceof CFDatePickerView) {
						CFDatePickerView dateview = (CFDatePickerView)view;
						String otherkey = dateview.getItemKey();
						if(null != itemkey && null != otherkey) {
							if(otherkey.equals(itemkey)) 
								dateview.setDefaultValue(null, value);
						}
					} else if(view instanceof CFReferView) {
						CFReferView dateview = (CFReferView)view;
						String otherkey = dateview.getItemKey();
						if(null != itemkey && null != otherkey) {
							if(otherkey.equals(itemkey)) 
								dateview.setDefaultValue(pk, value);
						}
					} else if(view instanceof CFEnumView) {
						CFEnumView dateview = (CFEnumView)view;
						String otherkey = dateview.getItemKey();
						if(null != itemkey && null != otherkey) {
							if(otherkey.equals(itemkey)) 
								dateview.setDefaultValue(pk, value);
						}
					} else if(view instanceof CFEditBooleanView) {
						CFEditBooleanView dateview = (CFEditBooleanView)view;
						String otherkey = dateview.getItemKey();
						if(null != itemkey && null != otherkey) {
							if(otherkey.equals(itemkey)) 
								dateview.setDefaultValue(null, value);
						}
					} else if(view instanceof CFFileView) {
						CFFileView dateview = (CFFileView)view;
						String otherkey = dateview.getItemKey();
						if(null != itemkey && null != otherkey) {
							if(otherkey.equals(itemkey)) {
								dateview.setSameValue(null, value);
								dateview.setSameValue(this.view);
							}
						}
					} else if(view instanceof CFPhotoView) {
						CFPhotoView dateview = (CFPhotoView)view;
						String otherkey = dateview.getItemKey();
						if(null != itemkey && null != otherkey) {
							if(otherkey.equals(itemkey) && view != this.view) {
								dateview.setSameValue(this.view);
							}
						}
					} 
				}
			}
		}
	}
	
	/**
	 * 为显示值为rEditText类型的view赋值
	 * view 为当前点击的view
	 */
	public void setSamekeyValueForEditText(CFTextEditView view) {
		String itmekey = view.getItemKey();
		FieldValueCommon value = (FieldValueCommon) view.getValue();
		if(this.isheaderGroupView) {
			setSamekeyValueForEditText(itmekey,value.getValue(),view);	//表头
		} else {
			setSamekeyValueForEditTextBody(itmekey,value.getValue(),view);	//表体
		}
	}
	
	public void setSamekeyValueForEditText(String itemkey,String value,View view) {
		if(!this.isheaderGroupView) {
			setSamekeyValueForEditTextBody(itemkey,value,view);	//表体，这里主要是为计算公式时服务的
			return;
		}
		CommonFormLayout layoutview = (CommonFormLayout) this.getParent();
		for(int j = 0;j<layoutview.getChildCount();j++) {
			View viewTemp = layoutview.getChildAt(j);
			if(viewTemp instanceof CommonFormGroupView) {
				CommonFormGroupView formGroupView = (CommonFormGroupView)viewTemp;
				for(int i = 0;i<formGroupView.getChildCount();i++) {
					View view1 = formGroupView.getChildAt(i);
					if(view1 instanceof CFTextEditView) {
						CFTextEditView editTextView = (CFTextEditView)view1;
						if(null != itemkey) {
							String tempItemkey = editTextView.getItemKey();
							if(null != view && null != tempItemkey && tempItemkey.equals(itemkey)) {
								editTextView.removeChangeListener(editTextView);
								if(view != editTextView)
									editTextView.setDefaultValue("", value);
								editTextView.setChangeListener(editTextView);
							}
						}
					}
				}
			}
		}
	}
	
	
	
	public void setSamekeyValueForEditTextBody(String itemkey,String value,View view) {
		for(int i = 0;i<this.getChildCount();i++) {
			View view1 = this.getChildAt(i);
			if(view1 instanceof CFTextEditView) {
				CFTextEditView editTextView = (CFTextEditView)view1;
				if(null != itemkey) {
					String tempItemkey = editTextView.getItemKey();
					if(null != view && null != tempItemkey && tempItemkey.equals(itemkey)) {
						editTextView.removeChangeListener(editTextView);
						if(view != editTextView)
							editTextView.setDefaultValue("", value);
						editTextView.setChangeListener(editTextView);
					}
				}
			}
		}
	}
	
	/////////////////为相同的itemkey的地址类型赋值--start///////////////////////////////////
	
	/**
	 * * @param view	当前被点击的view 即事件源
	 * @param data
	 * @param index
	 */
	public void setSamekeyValueForAddress(CFAddressView view,AddressVO data,int index) {
		String itmekey = view.getItemKey();
		if(this.isheaderGroupView) {
			CommonFormLayout layoutview = (CommonFormLayout) this.getParent();
			for(int j = 0;j<layoutview.getChildCount();j++) {
				View viewTemp = layoutview.getChildAt(j);
				if(viewTemp instanceof CommonFormGroupView) {
					CommonFormGroupView formGroupView = (CommonFormGroupView)viewTemp;
					setSamekeyValueForAddress(itmekey,data,index,formGroupView);
				}
			}
		} else {
			setSamekeyValueForAddress(itmekey,data,index,this);	//表体
		}
		
	}
	
	public void setSamekeyForAddressCode(CFAddressView view,String value,int index) {
		String itmekey = view.getItemKey();
		if(this.isheaderGroupView) {
			CommonFormLayout layoutview = (CommonFormLayout) this.getParent();
			for(int j = 0;j<layoutview.getChildCount();j++) {
				View viewTemp = layoutview.getChildAt(j);
				if(viewTemp instanceof CommonFormGroupView) {
					CommonFormGroupView formGroupView = (CommonFormGroupView)viewTemp;
						setSamekeyForAddressCode(itmekey,value,index,formGroupView,view);
				}
			}
		} else {
			setSamekeyForAddressCode(itmekey,value,index,this,view);	//表体
		}
	}
	
	/**
	 * 为地址类型的3个参照副相同的值
	
	 * @param data	
	 * @param index	分别取值 1,2,3，分别代表省，市，县
	 */
	public void setSamekeyValueForAddress(String itemkey,AddressVO data,int index,ViewGroup viewTemp) {
		for(int i = 0;i<viewTemp.getChildCount();i++) {
			View view1 = viewTemp.getChildAt(i);
			if(view1 instanceof CFAddressView) {
				String tempItemkey = ((CFAddressView) view1).getItemKey();
				if(null != itemkey && null != tempItemkey) {
					if(tempItemkey.equals(itemkey)) {
						((CFAddressView) view1).setSameValue(data, index,"");
					}
				}
			}
		}
	}
	
	//为地址类型的2个edittext副相同的值
	public void setSamekeyForAddressCode(String itemkey,String value,int index,ViewGroup viewTemp,View view) {
		for(int i = 0;i<viewTemp.getChildCount();i++) {
			View view1 = viewTemp.getChildAt(i);
			if(view1 instanceof CFAddressView) {
				CFAddressView editTextView = (CFAddressView)view1;
				if(null != itemkey) {
					String tempItemkey = editTextView.getItemKey();
					if(null != tempItemkey && tempItemkey.equals(itemkey)) {
						editTextView.removeChangeListener(null);
						if(view != editTextView)
							editTextView.setSameValue(null, index,value);
						editTextView.setChangeListener(null);
					}
				}
			}
		}
	}
/////////////////为相同的itemkey的地址类型赋值 -- end///////////////////////////////////
	

	public void addFormularView(AbsCommonFormView view) {
		listformulars.add(view);
	}

	public void addView(AbsCommonFormView view) {
//		super.addRow(view);
		super.addRowForFrom(view);
	}
	
	/**
	 * 将公式添加到分组
	 * @param expression
	 */
	public void addExpression(String expression){
		Expression exp;
		try {
			exp = new Expression(expression);
			formularContext1.addExpression(exp);
		} catch (Exception e) {
			Log.d("CF_Formular", "不符合预期的公式格式：" + expression);
		}
	}
	
	public void result(String key, double result) {
		Bundle bundle = new Bundle();
		bundle.putString("key", key);
		bundle.putDouble("result", result);
		Message msg = new Message();
		msg.setData(bundle);
		msg.what = 0;
		handler.sendMessage(msg);
	}

	public void textChangeLisenter(CFTextEditView view) {
		this.view = view;
		setSamekeyValueForEditText(view);
		if(view instanceof CFEditIntegerView || view instanceof CFEditDoubleView || 
				view instanceof CFEditMoneyView || view instanceof CFEditPercentView) {
			formularContext1.calc(listformulars);
		}
	}
	
	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	
	public List<AbsCommonFormView> getViewsForTextView() {
		return viewsForTextView;
	}

	public void setViewsForTextView(List<AbsCommonFormView> viewsForTextView) {
		this.viewsForTextView = viewsForTextView;
	}
	
	public boolean isIsheaderGroupView() {
		return isheaderGroupView;
	}

	public void setIsheaderGroupView(boolean isheaderGroupView) {
		this.isheaderGroupView = isheaderGroupView;
	}
	
	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

}
