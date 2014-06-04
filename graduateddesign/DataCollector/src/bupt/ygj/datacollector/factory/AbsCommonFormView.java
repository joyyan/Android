package bupt.ygj.datacollector.factory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

@SuppressLint("ViewConstructor")
public abstract class AbsCommonFormView extends RelativeLayout implements TextWatcher{
	
	protected LayoutInflater layoutInflater = null;
	protected String itemKey = null;
	
	protected ElementDataVO viewAttribute;	//此view的所有属性
	
	protected String upid;	//主要为参照提交的时候起作用

	

	protected CommonFormGroupView groupView;
	
	


	/**
	 * mode暂时没有用了
	 * @param context
	 * @param mode
	 */
	public AbsCommonFormView(Context context, int mode) {
		super(context);
		isVisibility();
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		float unit = getResources().getDisplayMetrics().density;
		this.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, (int)(unit*48)));
		this.setGravity(Gravity.CENTER_VERTICAL);
	}
	
	public AbsCommonFormView(Context context, ElementDataVO viewAttribute) {
		super(context);
		this.viewAttribute = viewAttribute;
		isVisibility();
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		float unit = getResources().getDisplayMetrics().density;
		this.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, (int)(unit*48)));
		this.setGravity(Gravity.CENTER_VERTICAL);
	}
	
	public String getItemKey() {
		return itemKey;
	}


	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}
	
	public ElementDataVO getViewAttribute() {
		return viewAttribute;
	}

	public void setViewAttribute(ElementDataVO viewAttribute) {
		this.viewAttribute = viewAttribute;
	}
	
	/**
	 * 判断该view中的值是否可以为空，所有的view都有这个属性
	 * 在提交表单的时候需要判断是否可以为空
	 * @return
	 */
	public boolean isCanNull() {
		if(null != viewAttribute) {
			Boolean b = viewAttribute.isAllowEmpty();
			if(b == null) {
				return true;
			}
			return b;
		}
		return true;
	}
	/**
	 * 判断该view中的值是否可以为空，所有的view都有这个属性（除附件外）
	 * 在提交表单的时候需要判断是否是参照关联项
	 * @return
	 */
	public boolean isRelatedItem() {
		if(null != viewAttribute) {
			Boolean b = viewAttribute.isRelated();
			if(b == null) {
				return false;
			}
			return b;
		}
		return false;
	}
	
	/**
	 * 判断该view是否可见，所有的view都有这个属性
	 * 在提交表单的时候需要判断是否可以为见	
	 * @return
	 */
	public void isVisibility() {
		if(null != viewAttribute) {
			Boolean b = viewAttribute.isVisible();
			if(b == null || b == false) {
				this.setVisibility(View.GONE);
			}
			
		} else {
			this.setVisibility(View.GONE);
		}
		
	}

	
	
	/**
	 * 判断该view是否可见，所有的view都有这个属性
	 * 在提交表单的时候需要判断是否可用	
	 * @return
	 */
	@Override
	public boolean isEnabled() {
		if(null != viewAttribute) {
			Boolean b = viewAttribute.isEnable();
			if(b == null) {
				return false;
			}
			return b;
		}
		return false;
	}
	
	/**
	 * 判断该view是否可见，所有的view都有这个属性
	 * 在提交表单的时候需要判断是否可编辑	，这个属性依赖于是否可用
	 * 即，在不可以的状态下，可编辑为Y也不可编辑
	 * @return
	 */
	public boolean isEdit() {
		if(isEnabled()) {
			Boolean b = viewAttribute.isEditable();
			if(b == null) {
				return false;
			}
			return b;
		}
		return false;
	}
	
	/**
	 * 判断该view是否可见,并不是所有的view都有这个属性，一般用于控制类似于字符串类型的
	 * @return
	 */
	public int getLength() {
		if(null != viewAttribute) {
			int length = viewAttribute.getLength();
			return length;
		}
		return -1;
	}
	
	/**
	 * 得到该view所有控制的精度，只有decimal:数值
									money：金额
									percent：百分比   有
	 * @return
	 */
	public int getPrecision() {
		if(null != viewAttribute) {
			String precision = viewAttribute.getPrecision();
			if(null != precision && !"".equals(precision))
				return Integer.valueOf(precision);
			else
				return -1;
		}
		return -1;
	}
	
	public boolean checkData(){
		return true;
	}
	
	/**
	 * 获取焦点，主要是抢夺edittext的焦点
	 * @param rootView
	 */
	protected void getFocus(View rootView) {
		rootView.requestFocusFromTouch();
		InputMethodManager inputMethodManager = (InputMethodManager) rootView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	public abstract AbsFieldValue getValue();
	
	public abstract void setDefaultValue(String defaultPk, String defaultString);
	
	public abstract void setTitle(String title);
	
	public void setChangeListener(TextWatcher watcher){}

	public void removeChangeListener(TextWatcher watcher){}
	
	
	/**
	 * 参照 等类型，下一界面处理完数据之后回传的Intent，在这里处理
	 * @param data
	 */
	public void processResultIntent(Intent data) {	
		
	}
	
	public void processResultIntent(Intent data,int resultCode) {	
		
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}
	
	public String getUpid() {
		return upid;
	}

	public void setUpid(String upid) {
		this.upid = upid;
	}
	
	public CommonFormGroupView getGroupView() {
		return groupView;
	}

	public void setGroupView(CommonFormGroupView groupView) {
		this.groupView = groupView;
	}
}
