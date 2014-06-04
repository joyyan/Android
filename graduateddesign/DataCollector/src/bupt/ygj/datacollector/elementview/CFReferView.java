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
import bupt.ygj.datacollector.activity.CommonFormActivity;
import bupt.ygj.datacollector.activity.ReferListActivity;
import bupt.ygj.datacollector.activity.ReferValuesActivity;
import bupt.ygj.datacollector.data.AbsFieldValue;
import bupt.ygj.datacollector.data.ElementDataVO;
import bupt.ygj.datacollector.data.ReferCommonVO;
import bupt.ygj.datacollector.data.ReferToItemVO;
import bupt.ygj.datacollector.data.RelatedItemVO;

public class CFReferView extends AbsCommonFormView implements OnClickListener{
	
	private View rootView = null;
	private TextView eView = null;
	
	private boolean paging = true;			//用于区分物料参照（物料分类参照）和通用参照
	
	private String pkOrg = null;
	private String referTo = null;
	private String category;		//用于区分是物料参照还是物料分类参照

	private ArrayList<RelatedItemVO> relatedItemList = null;	//参照关联项
	private ReferCommonVO refer;		//用于保存提交选中的物料，进而可以在自由表单成功后把她作为历史记录保存起来(这里应该只是指物料参照)
										//其次也用于提交自由表单时，把refer中的id（pk）取出来
	
	
	private int mode = 0;
	
	//mode用于区分是表体的关联项还是表头的关联项0 表示表头，1表示表体
	public CFReferView(Context context, ElementDataVO viewAttribute,String pkOrg ,int mode) {
		super(context, viewAttribute);
		this.mode = mode;
		init(pkOrg);
	}
	
	private void init(String pkOrg) {
		
		this.pkOrg = pkOrg;
		this.referTo = viewAttribute.getReferTo();
		relatedItemList = (ArrayList<RelatedItemVO>) viewAttribute.getRelatedItemList();
		if(!"物料（多版本）".equals(referTo) && !"物料基本分类".equals(referTo)) {
			this.paging = false;
		}
		rootView = layoutInflater.inflate(R.layout.layout_cf_view_refer, null);
		rootView.setOnClickListener(this);
		addView(rootView);
		eView = (TextView)rootView.findViewById(R.id.cf_view_text_view);
		if(!isEdit()) {
			ImageView image = (ImageView) rootView.findViewById(R.id.cf_view_right_array);
		}
	}
	
	@Override
	public AbsFieldValue getValue() {
		String value = "";
		if(viewAttribute != null) {
			if(viewAttribute.getDefaultReferPK() != null)
				value = viewAttribute.getDefaultReferPK();
			else if (null != refer) {
				value = refer.getId();
			}
				
		}
		if(value != null)
			return new FieldValueCommon(getItemKey(), value);
		else
			return null;
	}
	
	public String getValueid() {
		String value = "";
		if(viewAttribute != null) {
			if(viewAttribute.getDefaultReferPK() != null)
				value = viewAttribute.getDefaultReferPK();
			else if (null != refer) {
				value = refer.getId();
			}
		}
		return value;
	}

	/**
	 * 参照类型只支持获取模板处的默认值，且只支持一个。
	 */
	@Override
	public void setDefaultValue(String defaultPk, String defaultString) {	
		viewAttribute.setDefaultReferPK(defaultPk);//这里只是用于相同的itemkey的赋值
		eView.setText(defaultString);
	}

	@Override
	public void setTitle(String title) {
		TextView textView = (TextView)this.findViewById(R.id.cfViewName);
		textView.setText(title);
	}
	
	@Override
	public void onClick(View v) {
		getFocus(rootView);
		if(isEdit()) {
			if(paging) {
				Intent i = new Intent(getContext(), ReferListActivity.class);
				//关联项请求所需要的参数 - start
				i.putExtra("itemkey", getItemKey());
				i.putExtra("referto", referTo);
				i.putExtra("relatedpathlist",relatedItemList);
				//--end
				
				i.putExtra("pk_org", pkOrg);
				if(null != relatedItemList) 
					i.putExtra("hasrelated", "true");
				if ("物料（多版本）".equals(referTo)) {
					category = "materials";
					i.putExtra("category", category);
				} else if ("物料基本分类".equals(referTo)) {
					category = "materialscls";
					i.putExtra("category", category);
				}
				((BaseActivity)CFReferView.this.getContext()).startActivityForResult(i, CFReferView.this.getId());
			} else {	//通用参照
				Intent i = new Intent(getContext(), ReferValuesActivity.class);
				String itemname = "";
				if(null != viewAttribute) {
					itemname = viewAttribute.getItemName();
				}
				//关联项请求所需要的参数 - start
				i.putExtra("itemkey", getItemKey());
				i.putExtra("itemname", itemname);
				i.putExtra("referto", referTo);			//既为通用参照请求参数也为关联项请求参数
				i.putExtra("relatedpathlist", referTo);
				//--end	
				
				if(null != relatedItemList) 
					i.putExtra("hasrelated", "true");
				i.putExtra("pk_org", pkOrg);
				((BaseActivity)CFReferView.this.getContext()).startActivityForResult(i, CFReferView.this.getId());
			}
		}
	}
	
	@Override
	public void processResultIntent(Intent data,int resultCode) {
		ReferToItemVO referToItem = new ReferToItemVO();
		if(1 == resultCode) {
			refer = (ReferCommonVO)data.getSerializableExtra("material");
			if(null != refer) 
				eView.setText(refer.getName());								//data还需要进一步处理，既为getValue方法服务
			if(relatedItemList != null && relatedItemList.size() > 0) {		//如果存在参照关联项
				referToItem.setReferto("物料（多版本）");				//参照的referto 为汉字
			}
		} else if(2 == resultCode) {
			refer = (ReferCommonVO)data.getSerializableExtra("material");
			if(null != refer) 
				eView.setText(refer.getName());
			if(relatedItemList != null && relatedItemList.size() > 0) {		//如果存在参照关联项
				referToItem.setReferto("物料基本分类");				//参照的referto 为汉字
			}
		} else if(3 == resultCode) {
			refer = (ReferCommonVO)data.getSerializableExtra("refervalue");
			eView.setText(refer.getName());
		}
		viewAttribute.setDefaultReferPK(null);
		if(null != refer) 
			groupView.setSamekeyValue(this.getItemKey(),refer.getId(), refer.getName(),groupView.isIsheaderGroupView());//给其他的相同的itemkey赋值
		if(relatedItemList != null && relatedItemList.size() > 0) {		//如果存在参照关联项
			CommonFormActivity cformActivity = (CommonFormActivity)getContext();
			referToItem.setReferto(referTo);				//参照的referto 为汉字
			referToItem.setPkvalue(refer.getId());					//参照的默认pk
			referToItem.setItemkey(getItemKey());					
			List<String> list = new ArrayList<String>();
			for(RelatedItemVO related : relatedItemList) {
				list.add(related.getRelatedPathString());
			}
			referToItem.setRelatedpathlist(list);
			
			boolean isheader = true;
			if(mode == 1)
				isheader = false;
			cformActivity.getBReferRelated(referToItem,isheader,null,groupView);
		}
	}
	
	public ArrayList<RelatedItemVO> getRelatedItemList() {
		return relatedItemList;
	}

	public void setRelatedItemList(ArrayList<RelatedItemVO> relatedItemList) {
		this.relatedItemList = relatedItemList;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public ReferCommonVO getRefer() {
		return refer;
	}

	public void setRefer(ReferCommonVO refer) {
		this.refer = refer;
	}

}
