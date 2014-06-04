package bupt.ygj.datacollector.view;

import java.util.ArrayList;
import java.util.List;

import wa.android.libs.groupview.WAPanelView;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import bupt.ygj.datacollector.R;
import bupt.ygj.datacollector.activity.CommonFormActivity;
import bupt.ygj.datacollector.data.AbsFieldValue;
import bupt.ygj.datacollector.data.CFSaveDataVO;
import bupt.ygj.datacollector.data.ElementDataVO;
import bupt.ygj.datacollector.data.ElementGroupVO;
import bupt.ygj.datacollector.data.ElementGroupVO4Body;
import bupt.ygj.datacollector.data.ElementGroupVO4Header;
import bupt.ygj.datacollector.data.ReferCommonVO;
import bupt.ygj.datacollector.data.ReferToItemVO;
import bupt.ygj.datacollector.data.RelatedItemVO;
import bupt.ygj.datacollector.data.TemplateVO;
import bupt.ygj.datacollector.elementview.AbsCommonFormView;
import bupt.ygj.datacollector.elementview.CFEditDoubleView;
import bupt.ygj.datacollector.elementview.CFEditIntegerView;
import bupt.ygj.datacollector.elementview.CFEditMoneyView;
import bupt.ygj.datacollector.elementview.CFEditPercentView;
import bupt.ygj.datacollector.elementview.CFEnumView;
import bupt.ygj.datacollector.elementview.CFFileView;
import bupt.ygj.datacollector.elementview.CFPhotoView;
import bupt.ygj.datacollector.elementview.CFReferView;
import bupt.ygj.datacollector.factory.ElementViewFactory;

public class CommonFormLayout extends WAPanelView {

	private TemplateVO content = null;
	private Context context = null;
	private ElementViewFactory elementViewFactory = null;
	private CommonFormGroupView headerGroupView = null;
	private List<CommonFormGroupView> headerGroupList = new ArrayList<CommonFormGroupView>();

	private List<CommonFormGroupView> bodyGroupViewList = null;
	
	private List<ReferCommonVO> refers = new ArrayList<ReferCommonVO>();//用于保存提交选中的物料，进而可以在自由表单成功后把她作为历史记录保存起来
	private List<ReferCommonVO> refersclass = new ArrayList<ReferCommonVO>();//用于保存提交选中的物料分类，进而可以在自由表单成功后把她作为历史记录保存起来
	private List<ReferToItemVO> referToItemListB = new ArrayList<ReferToItemVO>();	//用于发送参照关联项的请求参数(表体)

	public CommonFormLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		bodyGroupViewList = new ArrayList<CommonFormGroupView>();
	}
	
	public CommonFormLayout(Context context) {
		super(context);
		this.context = context;
		bodyGroupViewList = new ArrayList<CommonFormGroupView>();
	}
	
	public void initContent(TemplateVO content, ElementViewFactory formElementFactory) {
		this.content = content;
		this.elementViewFactory = formElementFactory;
		createHeader();
	}
	
	private void initContent(CommonFormGroupView groupView, ElementGroupVO groupVO, ElementViewFactory formElementFactory, int mode) {
		List<ElementDataVO> elements = groupVO.getElements();
		if (elements != null) {
			for(ElementDataVO element : elements) {
				AbsCommonFormView view = formElementFactory.createCommonFormElementVO(content, element, mode);
				if (view != null) {
					setItemDefault(groupVO, view, element);			//设置默认值
					view.setGroupView(groupView);
					
					if(view instanceof CFEditIntegerView || view instanceof CFEditDoubleView || 
							view instanceof CFEditMoneyView || view instanceof CFEditPercentView) {
						groupView.addFormularView(view);
						String formular = element.getEditFormula();
						if (formular != null && !"".equals(formular)) {
							try{
								groupView.addExpression(formular);		//公式类型的view添加监听，注册公式
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} 
					groupView.addView(view);
				}
			}
		}
	}
	
	private void createHeader() {
		if(this.content != null) {
			List<ElementGroupVO4Header> headerList = this.content.getHeaderList();
			if (headerList != null) {
				for (ElementGroupVO4Header groupVO : headerList) {
					headerGroupView = new CommonFormGroupView(context);		//一个表头分组view（在view展示上继承WAGroupView）
					headerGroupView.setTitle(groupVO.getGroupName());
					headerGroupView.setGroupid(groupVO.getGroupid());
					headerGroupView.setIsheaderGroupView(true);
					headerGroupList.add(headerGroupView);
					initContent(headerGroupView, groupVO, elementViewFactory, 0);
					addGroup(headerGroupView);
				}
			}
			if (content.getBodyList() != null && content.getBodyList().size() > 0 && content.getBodyList().get(0).getElements() != null) {
				LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View viewFuncAdd = layoutInflater.inflate(R.layout.layout_cf_func_add_line, null);
				viewFuncAdd.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						initOneBodyLine();
					}
				});
				addView(viewFuncAdd);
			}
		}
	}
	
	private void initOneBodyLine() {
		List<ElementGroupVO4Body> bodyList = content.getBodyList();
		if (bodyList != null) {
			for(ElementGroupVO4Body groupVO : bodyList) {
				LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final RelativeLayout layoutBody = (RelativeLayout)layoutInflater.inflate(R.layout.cf_layout_body, null);
				final CommonFormGroupView bodyGroupView = (CommonFormGroupView)layoutBody.findViewById(R.id.cf_layout_body);
				final ImageButton btnDelete = (ImageButton)layoutBody.findViewById(R.id.cf_btn_del_body);
				addView(layoutBody, this.getChildCount() - 1); 
				bodyGroupView.setIsheaderGroupView(false);
				bodyGroupViewList.add(bodyGroupView);
				initContent(bodyGroupView, groupVO, elementViewFactory, 1);
				
				btnDelete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						CommonFormLayout.this.removeView(layoutBody);
						CommonFormLayout.this.bodyGroupViewList.remove(bodyGroupView);
					}
				});
				//给表体关联项赋值 start
				List<ElementGroupVO4Body> bodys = content.getBodyList();
				if(bodys != null && bodys.size() != 0) { 
					for(ElementGroupVO4Body body : bodys) {
						if(null != body) {
							List<ElementDataVO> elementList = body.getElements();
							setReferRelated(elementList,referToItemListB);
						}
					}
				}
				
				//如果存在有默认值的参照类型，并且此参照类型有关联项，那么继续请求该参照的关联项(表体)
				if(referToItemListB.size() > 0) {
					Context context = this.getContext();
					((CommonFormActivity)context).getBReferRelated(null,false,referToItemListB,bodyGroupView);
				}
				//给表体关联项赋值 end
			}
		}
	}
	
	//为模板上的各个view赋值
	private void setItemDefault(ElementGroupVO groupVO, AbsCommonFormView view, ElementDataVO element){
		if (groupVO instanceof ElementGroupVO4Header) {
			setDefaultValue(view,element);
		} else if (groupVO instanceof ElementGroupVO4Body) {		
			setDefaultValue(view,element);
		}
	}
	
	private void setDefaultValue(AbsCommonFormView view,ElementDataVO element) {
		if(view instanceof CFReferView) {
			view.setDefaultValue(element.getDefaultReferPK(), element.getDefaultValue());
		} else if (view instanceof CFEnumView) {
			view.setDefaultValue(element.getDefaultValue(), null);
		} else {
			view.setDefaultValue(null,element.getDefaultValue());
		}
	}
	
	public CFSaveDataVO getSubmitData(String pkdoc, String eventid, String visitid, String workitemid,String childid){
		CFSaveDataVO submitData = new CFSaveDataVO(pkdoc, eventid, visitid, workitemid);
		
		List<String> fileItemKeyList = new ArrayList<String>();			//用于保存file和filefiled类型的itemkey，列表中不会重复，避免重复将附件加载到内存
		List<String> picItemKeyList = new ArrayList<String>();			//用于保存pic类型的itemkey，列表中不会重复，避免重复将附件加载到内存

		//构造表头
		List<AbsFieldValue> hSaveItemList = new ArrayList<AbsFieldValue>();
		for(int j = 0;j<this.getChildCount();j++) {
			View view1 = this.getChildAt(j);
			if(view1 instanceof CommonFormGroupView) {
				CommonFormGroupView groupview = (CommonFormGroupView)view1;
				for(int i = 0;i<groupview.getChildCount();i++) {
					View view = groupview.getChildAt(i);
					if(view instanceof AbsCommonFormView) {
						boolean b = ((AbsCommonFormView)view).isCanNull();
						if(!b) {
							AbsFieldValue absvalue = ((AbsCommonFormView)view).getValue();
							if(absvalue == null || absvalue.getRealValueEmpty()) {	///判断是否可以为空
								ElementDataVO ele = ((AbsCommonFormView)view).getViewAttribute();
								CommonFormActivity formActivity = (CommonFormActivity)getContext();
								formActivity.alertNull(ele.getItemName());
								return null;
							}
						}
//						boolean isr = ((AbsCommonFormView)view).isRelatedItem();	//判断是否是关联项
//						if(isr) {//如果是关联项，那么上传参照的id，比如是参照的关联项，那么上传该参照的id
//							AbsCommonFormView viewtemp = ((AbsCommonFormView)view);
//							FieldValueRelated fieldvalue = new FieldValueRelated(((AbsCommonFormView)view).getItemKey(),viewtemp.getUpid());
//							hSaveItemList.add(fieldvalue);
//						} else {
//							hSaveItemList.add(((AbsCommonFormView)view).getValue());
//						}
						
						
						if(view instanceof CFFileView) {
							AbsCommonFormView absCFView = (AbsCommonFormView)view;
							String itemkey = absCFView.getItemKey();
							if(!fileItemKeyList.contains(itemkey)) {
								fileItemKeyList.add(itemkey);
								hSaveItemList.add(((AbsCommonFormView)view).getValue());
							}
						} else if(view instanceof CFPhotoView) {
							AbsCommonFormView absCFView = (AbsCommonFormView)view;
							String itemkey = absCFView.getItemKey();
							if(!picItemKeyList.contains(itemkey)) {
								picItemKeyList.add(itemkey);
								hSaveItemList.add(((AbsCommonFormView)view).getValue());
							}
						} else {
							hSaveItemList.add(((AbsCommonFormView)view).getValue());
						}
						
						
						if(view instanceof CFReferView) {
							String category = ((CFReferView)view).getCategory();
							ReferCommonVO refer = ((CFReferView)view).getRefer();
							if(category != null && category.equals("materials")) {
								if(refer != null && !refers.contains(refer)) {
									refers.add(refer);
								}
							} else if(category != null && category.equals("materialscls")) {
								if(refer != null && !refersclass.contains(refer)) {
									refersclass.add(refer);
								}
							}
						}
					}
				}
			}
		}
		submitData.setHeaderItemList(hSaveItemList);
		
		//表体(这里不是多表体，而是逐步遍历每一个表体行)
		if(bodyGroupViewList != null && bodyGroupViewList.size() > 0) {
			for (CommonFormGroupView bodyGroupView : bodyGroupViewList) {
				if(bodyGroupView != null) {
					List<AbsFieldValue> bodyRow = new ArrayList<AbsFieldValue>();
					for(int i = 0;i<bodyGroupView.getChildCount();i++) {
						View view = bodyGroupView.getChildAt(i);
						if(view instanceof AbsCommonFormView) {
							
							//判断是否可以为空
							boolean b = ((AbsCommonFormView)view).isCanNull();
							if(!b) {
								AbsFieldValue absvalue = ((AbsCommonFormView)view).getValue();
								if(absvalue == null || absvalue.getRealValueEmpty()) {	///判断是否可以为空
									ElementDataVO ele = ((AbsCommonFormView)view).getViewAttribute();
									CommonFormActivity formActivity = (CommonFormActivity)getContext();
									formActivity.alertNull(ele.getItemName());
									return null;
								}
							}

							bodyRow.add(((AbsCommonFormView)view).getValue());
							
							//这里判断是否是参照，仅仅是为了保存参照提交成功后，参照列表的历史记录
							if(view instanceof CFReferView) {	
								String category = ((CFReferView)view).getCategory();
								ReferCommonVO refer = ((CFReferView)view).getRefer();
								if(category != null && category.equals("materials")) {
									if(refer != null && !refers.contains(refer)) {
										refers.add(refer);
									}
								} else if(category != null && category.equals("materialscls")) {
									if(refer != null && !refersclass.contains(refer)) {
										refersclass.add(refer);
									}
								}
							}
						}
					}
					submitData.addBodyRow(bodyRow,childid);
				}
			}
		} else {
			submitData.setbSaveItemList(null);
		}
		return submitData;
	}
	
	
	private void setReferRelated(List<ElementDataVO> elementList,List<ReferToItemVO> referlist) {
		if(null != elementList) {
			for(ElementDataVO ele : elementList) {
				//如果此元素为参照类型，并且此参照类型有默认值;
				if(ele.getItemType() != null && ele.getItemType().equals("refertype")) {
					if(ele.getDefaultReferPK() != null && !ele.getDefaultReferPK().equals("")) {
						//并且此参照类型有参照关联项.
						List<RelatedItemVO> relatedItemList = ele.getRelatedItemList();
						if(relatedItemList != null && relatedItemList.size() > 0) {
							ReferToItemVO referToItem = new ReferToItemVO();
							referToItem.setItemkey(ele.getItemKey());	//参照的itemkey
							referToItem.setReferto(ele.getReferTo());	//参照的referto 为汉字
							referToItem.setPkvalue(ele.getDefaultReferPK());	//参照的默认pk
							
							List<String> list = new ArrayList<String>();
							for(RelatedItemVO related : relatedItemList) {
								list.add(related.getRelatedPathString());
							}
							referToItem.setRelatedpathlist(list);
							referlist.add(referToItem);
						}
					}
				}
			}
		}
	}
	
	public List<ReferCommonVO> getRefers() {
		return refers;
	}

	public void setRefers(List<ReferCommonVO> refers) {
		this.refers = refers;
	}
	
	public List<ReferCommonVO> getRefersclass() {
		return refersclass;
	}

	public void setRefersclass(List<ReferCommonVO> refersclass) {
		this.refersclass = refersclass;
	}

	public CommonFormGroupView getHeaderGroupView() {
		return headerGroupView;
	}

	public void setHeaderGroupView(CommonFormGroupView headerGroupView) {
		this.headerGroupView = headerGroupView;
	}
	
	public List<CommonFormGroupView> getBodyGroupViewList() {
		return bodyGroupViewList;
	}

	public void setBodyGroupViewList(List<CommonFormGroupView> bodyGroupViewList) {
		this.bodyGroupViewList = bodyGroupViewList;
	}
	
	public List<CommonFormGroupView> getHeaderGroupList() {
		return headerGroupList;
	}

	public void setHeaderGroupList(List<CommonFormGroupView> headerGroupList) {
		this.headerGroupList = headerGroupList;
	}
}
 