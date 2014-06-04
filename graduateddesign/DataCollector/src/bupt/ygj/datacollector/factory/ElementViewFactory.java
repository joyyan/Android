package bupt.ygj.datacollector.factory;

import android.content.Context;
import android.util.Log;
import bupt.ygj.datacollector.data.ElementDataVO;
import bupt.ygj.datacollector.data.TemplateVO;
import bupt.ygj.datacollector.elementview.AbsCommonFormView;
import bupt.ygj.datacollector.elementview.CFAddressView;
import bupt.ygj.datacollector.elementview.CFDatePickerView;
import bupt.ygj.datacollector.elementview.CFEditBooleanView;
import bupt.ygj.datacollector.elementview.CFEditDoubleView;
import bupt.ygj.datacollector.elementview.CFEditIntegerView;
import bupt.ygj.datacollector.elementview.CFEditMoneyView;
import bupt.ygj.datacollector.elementview.CFEditPercentView;
import bupt.ygj.datacollector.elementview.CFEditPhoneView;
import bupt.ygj.datacollector.elementview.CFEditSimpleView;
import bupt.ygj.datacollector.elementview.CFEnumView;
import bupt.ygj.datacollector.elementview.CFFileView;
import bupt.ygj.datacollector.elementview.CFInitNullTypeView;
import bupt.ygj.datacollector.elementview.CFPhotoView;
import bupt.ygj.datacollector.elementview.CFReferView;
import bupt.ygj.datacollector.elementview.CFTextAreaView;

public class ElementViewFactory {
	private static int id = 1;
	private Context context = null;
	public ElementViewFactory(Context context) {
		this.context = context;
	}
	
	//mode为0代表表头，mode为1代表表体
	public AbsCommonFormView createCommonFormElementVO(TemplateVO templateVO, ElementDataVO formDataVO, int mode){
		AbsCommonFormView ret = null;
		String type = formDataVO.getItemType();
		if ("integer".equals(type)) {
			ret = new CFEditIntegerView(context, formDataVO);
		} else if ("decimal".equals(type)) {
			ret = new CFEditDoubleView(context, formDataVO);
		} else if ("string".equals(type)) {
			ret = new CFEditSimpleView(context, formDataVO);
		} else if ("date".equals(type)) {
			ret = new CFDatePickerView(context, CFDatePickerView.DATEMODE,formDataVO);
		} else if ("datetime".equals(type)) {
			ret = new CFDatePickerView(context, CFDatePickerView.DATETIMEMODE,formDataVO);
		} else if ("year".equals(type)) {
			ret = new CFDatePickerView(context, CFDatePickerView.YEARMODE,formDataVO);
		} else if ("month".equals(type)) {
			ret = new CFDatePickerView(context, CFDatePickerView.MONTHMODE,formDataVO);
		} else if ("yearmonth".equals(type)) {
			ret = new CFDatePickerView(context, CFDatePickerView.YEARMONTHMODE,formDataVO);
		} else if ("begindate".equals(type)) {
			ret = new CFDatePickerView(context, CFDatePickerView.BEGINDATE,formDataVO);
		} else if ("enddate".equals(type)) {
			ret = new CFDatePickerView(context, CFDatePickerView.ENDDATE,formDataVO);
		} else if ("literaldate".equals(type)) {
			ret = new CFDatePickerView(context, CFDatePickerView.LITERALDATE,formDataVO);
		} else if ("refertype".equals(type)) {
			String pkOrg = templateVO.getPkorg();
			ret = new CFReferView(context, formDataVO,pkOrg,mode);
		} else if ("money".equals(type)) {
			ret = new CFEditMoneyView(context, formDataVO);
		} else if ("percent".equals(type)) {
			ret = new CFEditPercentView(context, formDataVO);
		} else if ("boolean".equals(type)) {
			ret = new CFEditBooleanView(context, formDataVO);
		} else if ("pic".equals(type) && mode != 1) {
			ret = new CFPhotoView(context, formDataVO);
		} else if ("file".equals(type) && mode != 1) {
			ret = new CFFileView(context, formDataVO);
		} else if ("filefield".equals(type) && mode != 1) {
			ret = new CFFileView(context, formDataVO);
		} else if ("address".equals(type)) {
			ret = new CFAddressView(context, formDataVO);
			CFAddressView addrret = (CFAddressView)ret;
			addrret.getState().setId(++id);
			addrret.getCity().setId(++id);
			addrret.getArea().setId(++id);
			ret = addrret;
		} else if("textarea".equals(type)) {
			ret = new CFTextAreaView(context,formDataVO);
		} else if("combo".equals(type)) {
			ret = new CFEnumView(context,formDataVO);
		} else if("email".equals(type)) {		//email暂时和sting用一个view
			ret = new CFEditSimpleView(context,formDataVO);
		} else if("phone".equals(type)) {	
			ret = new CFEditPhoneView(context,formDataVO);
		} else if("link".equals(type)) {		//link暂时和sting用一个view
			ret = new CFEditSimpleView(context,formDataVO);
		} else if("initnulltype".equals(type)) {		//link暂时和sting用一个view
			ret = new CFInitNullTypeView(context,formDataVO);
		}
		if (ret == null) {
			Log.d("CF", "Unsupported type:" + type);
		} else {
			ret.setId(++id);
			ret.setItemKey(formDataVO.getItemKey());
			ret.setTitle(formDataVO.getItemName());
		}
		return ret;
	}
}
