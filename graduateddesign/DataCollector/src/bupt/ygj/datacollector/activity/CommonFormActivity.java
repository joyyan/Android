package bupt.ygj.datacollector.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import wa.android.common.activity.BaseActivity;
import wa.android.common.utils.PreferencesUtil;
import wa.android.constants.CommonWAPreferences;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import bupt.ygj.datacollector.R;
import bupt.ygj.datacollector.data.AddressVO;
import bupt.ygj.datacollector.data.BatchRelatedValue;
import bupt.ygj.datacollector.data.CFSaveDataRequester;
import bupt.ygj.datacollector.data.CFSaveDataVO;
import bupt.ygj.datacollector.data.ElementDataVO;
import bupt.ygj.datacollector.data.ElementGroupVO4Body;
import bupt.ygj.datacollector.data.ElementGroupVO4Header;
import bupt.ygj.datacollector.data.ExceptionEncapsulationVO;
import bupt.ygj.datacollector.data.GPSInfo;
import bupt.ygj.datacollector.data.ReferCommonVO;
import bupt.ygj.datacollector.data.ReferToItemVO;
import bupt.ygj.datacollector.data.RelatedItemVO;
import bupt.ygj.datacollector.data.RelatedValueListVO;
import bupt.ygj.datacollector.data.RelatedValueVO;
import bupt.ygj.datacollector.data.SaveCFVOSuc;
import bupt.ygj.datacollector.data.TemplateVO;
import bupt.ygj.datacollector.dataprovider.BReferRelatedProvider;
import bupt.ygj.datacollector.elementview.AbsCommonFormView;
import bupt.ygj.datacollector.elementview.CFAddressView;
import bupt.ygj.datacollector.elementview.CFReferView;
import bupt.ygj.datacollector.factory.ElementViewFactory;
import bupt.ygj.datacollector.view.CommonFormGroupView;
import bupt.ygj.datacollector.view.CommonFormLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

public class CommonFormActivity extends BaseActivity implements
		AMapLocationListener {

	private static final int MSG_SUBMIT_OK = 100;

	private ProgressDialog progressDlg;
	private Button submitButton = null;
	private Button backButton = null;
	private TextView title;
	private CommonFormLayout commonForm = null;
	private LinearLayout nodataview;
	private ScrollView dataview;
	private CommonFormGroupView bodyLineView;

	private GPSInfo gpsInfo = new GPSInfo();

	private TemplateVO templateVO = null;

	private String pkdoc = "";
	private String eventid = "";
	private String visitid = "";
	private String workitemid = "";
	private String childid = ""; // 表体id，从返回的模板出获取，该属性用于自由表单提交时

	private boolean isheader; // 判断是更新表头关联项还是表体关联项

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Map map = (Map) msg.obj;
				updateReferTo(map);
				progressDlg.dismiss();
				break;
			case 2: // 提交成功
				Map map1 = (Map) msg.obj;
				saveSucess(map1);
				progressDlg.dismiss();
				break;
			case 4:
				Map mapforerror = (Map) msg.obj;
				ExceptionEncapsulationVO ex = (ExceptionEncapsulationVO) mapforerror
						.get("exception");
				toastMsg(ex.getMessageList().get(0));
				updateReferTo(mapforerror);
				progressDlg.dismiss();
				break;
			case 5:
				Map mapforflag = (Map) msg.obj;
				ExceptionEncapsulationVO exflge = (ExceptionEncapsulationVO) mapforflag
						.get("flagexception");
				toastMsg(exflge.getFlagmessageList().get(0));
				updateReferTo(mapforflag);
				progressDlg.dismiss();
				break;
			case -10: // 网络失败，即stauts不为200
				toastMsg("网络错误");
				progressDlg.dismiss();
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		progressDlg = new ProgressDialog(this);
		progressDlg.setMessage(getResources().getString(R.string.dataLoading));
		progressDlg.setCancelable(false);
		progressDlg.setIndeterminate(true);
		String itemname = "";
		if (templateVO == null) {
			templateVO = (TemplateVO) removeGlobalVariable("TemplateVO");
			Intent i = getIntent();
			pkdoc = i.getStringExtra("pkdoc");
			eventid = i.getStringExtra("eventid");
			visitid = i.getStringExtra("visitid");
			workitemid = i.getStringExtra("workitemid");
			String pkorg = i.getStringExtra("pk_org");
			itemname = i.getStringExtra("itemname");
			if (templateVO != null) {
				templateVO.setPkorg(pkorg);
			}
		}
		setContentView(R.layout.activity_common_form);
		commonForm = (CommonFormLayout) findViewById(R.id.cf_main);
		title = (TextView) findViewById(R.id.addtitle);
		title.setText(itemname);
		submitButton = (Button) findViewById(R.id.tasktitlepanel_rightBtn);
		backButton = (Button) findViewById(R.id.addback);
		backButton.setText(R.string.cancel);
		backButton.setBackgroundColor(getResources()
				.getColor(R.color.nav_bkgrd));
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				submit();
			}

		});
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						CommonFormActivity.this);
				builder.setMessage(R.string.isGiveUpEdit);
				builder.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent i = new Intent();
								i.putExtra("visitid", visitid);
								setResult(0, i);
								finish();

							}
						});
				builder.setNegativeButton(R.string.no, null);
				builder.create().show();
			}
		});
		nodataview = (LinearLayout) findViewById(R.id.nodataPanel);
		dataview = (ScrollView) findViewById(R.id.dataPanel);

		if (null == templateVO) {
			showNODataView();
			return;
		}
		commonForm.initContent(templateVO, new ElementViewFactory(this));

		// 如发现参照有关联项，那么需要继续请求关联项（表头）
		List<ReferToItemVO> referToItemList = new ArrayList<ReferToItemVO>(); // 用于发送参照关联项的请求参数(表头)
		List<ElementGroupVO4Header> listHeaderGroup = templateVO
				.getHeaderList();
		if (listHeaderGroup != null && listHeaderGroup.size() != 0) {
			for (ElementGroupVO4Header header : listHeaderGroup) {
				if (null != header) {
					List<ElementDataVO> elementList = header.getElements();
					setReferRelated(elementList, referToItemList);
				}
			}
		}

		// 如果存在有默认值的参照类型，并且此参照类型有关联项，那么继续请求该参照的关联项(表头)
		if (referToItemList.size() > 0) {
			getBReferRelated(null, true, referToItemList, null);
		}
	}

	private void showNODataView() {
		dataview.setVisibility(View.GONE);
		nodataview.setVisibility(View.VISIBLE);
	}

	/*
	 * 看在界面初始化的时候是否继续请求参照关联项
	 */
	private void setReferRelated(List<ElementDataVO> elementList,
			List<ReferToItemVO> referlist) {
		if (null != elementList) {
			for (ElementDataVO ele : elementList) {
				// 如果此元素为参照类型，并且此参照类型有默认值;
				if (ele.getItemType() != null
						&& ele.getItemType().equals("refertype")) {
					if (ele.getDefaultReferPK() != null
							&& !ele.getDefaultReferPK().equals("")) {
						// 并且此参照类型有参照关联项.
						List<RelatedItemVO> relatedItemList = ele
								.getRelatedItemList();
						if (relatedItemList != null
								&& relatedItemList.size() > 0) {
							ReferToItemVO referToItem = new ReferToItemVO();
							referToItem.setItemkey(ele.getItemKey()); // 参照的itemkey
							referToItem.setReferto(ele.getReferTo()); // 参照的referto
																		// 为汉字
							referToItem.setPkvalue(ele.getDefaultReferPK()); // 参照的默认pk

							List<String> list = new ArrayList<String>();
							for (RelatedItemVO related : relatedItemList) {
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

	private void saveSucess(Map map) {

		SaveCFVOSuc sucvo = (SaveCFVOSuc) map.get("saveformdata");
		if (sucvo != null)
			visitid = sucvo.getVisitid();
		saveMaterailHistry();
		Intent i = new Intent();
		i.putExtra("visitid", visitid);
		setResult(0, i);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			super.onKeyDown(keyCode, event);
			Intent i = new Intent();
			i.putExtra("visitid", visitid);
			setResult(0, i);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void alertNull(String itemName) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				CommonFormActivity.this);
		builder.setMessage(itemName + "不能为空");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		// builder.setNegativeButton("取消",null);
		builder.create().show();
	}

	/**
	 * 请求参照关联项
	 * 
	 * @param referToItem
	 *            点击参照后请求的关联项参数
	 * @param isheader
	 *            表明是否是表头，true为表头，false为表体
	 * @param refers
	 *            表头和表体界面初始化时候，关联项的请求参数（表体的话每点击增加一个表体行都会调用）
	 * @param bodyLineView
	 *            所需要更新的关联项所在的表体行，这个参数只为表体服务
	 */
	public void getBReferRelated(ReferToItemVO referToItem, boolean isheader,
			List<ReferToItemVO> refers, CommonFormGroupView bodyLineView) {
		this.isheader = isheader;
		this.bodyLineView = bodyLineView;
		progressDlg.show();
		BReferRelatedProvider proder = new BReferRelatedProvider(this, handler);
		if (referToItem != null && referToItem.getRelatedpathlist() != null
				&& referToItem.getRelatedpathlist().size() > 0) {
			List<ReferToItemVO> referToItemList = new ArrayList<ReferToItemVO>();
			referToItemList.add(referToItem);
			proder.getBatchReferRelatedValues(referToItemList);
		} else {
			proder.getBatchReferRelatedValues(refers);
		}
	}

	// 给表头的所有参照关联项赋值
	public void updateReferTo(Map map) { // 给参照关联项赋值

		BatchRelatedValue data = (BatchRelatedValue) map
				.get("batchrelatedvalue");
		if (data != null) {
			List<RelatedValueListVO> referToList = data.getList();
			if (referToList != null) {
				for (int j = 0; j < referToList.size(); j++) {
					RelatedValueListVO referto = referToList.get(j);
					String itemkey = referto.getItemkey();
					if (isheader) {
						// 逐个为每个参照的一个或者是多个关联项赋值(表头)
						List<CommonFormGroupView> listgroup = commonForm
								.getHeaderGroupList();
						for (CommonFormGroupView group : listgroup) {
							for (int i = 0; i < group.getChildCount(); i++) {
								View view = group.getChildAt(i);
								if (view instanceof CFReferView) {
									String itemkeyview = ((CFReferView) view)
											.getItemKey();
									String upid = ((CFReferView) view)
											.getValueid();
									// 找到了一个参照，并为此参照的所有关联项赋值
									// 参照关联项返回的itemkey应和某一个参照类型的itemkey一样（接口上规定）
									if (itemkey != null && itemkeyview != null
											&& itemkeyview.equals(itemkey)) {
										// 取到对应view的关联项字段路径（通过path来匹配）
										List<RelatedItemVO> listrelated = ((CFReferView) view)
												.getRelatedItemList();
										// 得到服务器对应itemkey的关联项字段路径
										List<RelatedValueVO> newListrelated = referto
												.getRelatedvaluelist();
										comparepath(listrelated,
												newListrelated, upid);
									}
								}
							}
						}
					} else {
						updateReferToB(itemkey, referto);
					}
				}
			}
		}
	}

	// 给表体的所有参照关联项赋值
	public void updateReferToB(String itemkey, RelatedValueListVO referto) { // 给参照关联项赋值

		// 逐个为每个参照的一个或者是多个关联项赋值
		for (int u = 0; u < bodyLineView.getChildCount(); u++) {
			View view = bodyLineView.getChildAt(u);
			if (view instanceof CFReferView) {
				String itemkeyview = ((CFReferView) view).getItemKey();
				String upid = ((CFReferView) view).getValueid();
				// 找到了一个参照，并为此参照的所有关联项赋值
				if (itemkey != null && itemkeyview != null
						&& itemkeyview.equals(itemkey)) { // 找到对应view的参照项itemkey
					// 取到对应view的关联项字段路径（）
					List<RelatedItemVO> listrelated = ((CFReferView) view)
							.getRelatedItemList();
					// 得到服务器对应itemkey的关联项字段路径
					List<RelatedValueVO> newListrelated = referto
							.getRelatedvaluelist();
					comparepath(listrelated, newListrelated, upid);
				}
			}
		}
	}

	/**
	 * @param listrelated
	 *            为界面上的参照类型的 关联项
	 * @param newListrelated
	 *            为服务器批量返回的参照关联项
	 * @param upid
	 *            为关联项上传所需要的id
	 */
	private void comparepath(List<RelatedItemVO> listrelated,
			List<RelatedValueVO> newListrelated, String upid) {
		if (null != listrelated && null != newListrelated) {
			for (RelatedValueVO relatenew : newListrelated) {
				String newPath = relatenew.getRelatedpath();
				if (null != newPath) {
					for (RelatedItemVO item : listrelated) {
						if (null != item.getRelatedPathString()
								&& newPath.equals(item.getRelatedPathString())) {
							if (isheader)
								addValueToReferRelated(
										item.getRelatedItemKey(),
										relatenew.getRelatedvalue(), upid);
							else
								addValueToReferRelatedB(
										item.getRelatedItemKey(),
										relatenew.getRelatedvalue(), upid);
						}
					}
				}
			}
		}
	}

	/**
	 * 给关联项赋值(表头),
	 * 
	 * @param itemkey
	 *            界面上view元素的itemkey
	 * @param value
	 *            关联项接口返回的需要显示的值
	 * @param upid
	 *            关联项上传时所需要的值
	 */
	private void addValueToReferRelated(String itemkey, String value,
			String upid) {
		if (itemkey != null && null != value) {
			List<CommonFormGroupView> listgroup = commonForm
					.getHeaderGroupList();
			for (CommonFormGroupView group : listgroup) {
				for (int i = 0; i < group.getChildCount(); i++) {
					View view = group.getChildAt(i);
					if (view instanceof AbsCommonFormView) {
						String key = ((AbsCommonFormView) view).getItemKey();
						if (itemkey.equals(key)) {
							((AbsCommonFormView) view).setDefaultValue(key,
									value);
							((AbsCommonFormView) view).setUpid(upid);
						}
					}
				}
			}
		}
	}

	/**
	 * 给关联项赋值(表头),
	 * 
	 * @param itemkey
	 *            界面上view元素的itemkey
	 * @param value
	 *            关联项接口返回的需要显示的值
	 * @param upid
	 *            关联项上传时所需要的值
	 */
	private void addValueToReferRelatedB(String itemkey, String value,
			String upid) {
		if (itemkey != null && null != value) {
			for (int u = 0; u < bodyLineView.getChildCount(); u++) {
				View view = bodyLineView.getChildAt(u);
				if (view instanceof AbsCommonFormView) {
					String key = ((AbsCommonFormView) view).getItemKey();
					if (itemkey.equals(key)) {
						((AbsCommonFormView) view).setDefaultValue(key, value);
						((AbsCommonFormView) view).setUpid(upid);
					}
				}
			}
		}
	}

	// private AMapLocation aMapLocation;// 用于判断定位超时
	// private Handler handler1 = new Handler();
	public void submit() {

		if (null != templateVO) {
			List<ElementGroupVO4Body> body = templateVO.getBodyList();
			if (body != null && body.size() > 0 && body.get(0) != null) {
				childid = body.get(0).getChilddocid();
			}
		}
		progressDlg.show();
		submitBOData();
	}

	private String getKey() {
		String key = PreferencesUtil.readPreference(this,
				CommonWAPreferences.SERVER_IP)
				+ PreferencesUtil.readPreference(this,
						CommonWAPreferences.SERVER_PORT)
				+ PreferencesUtil.readPreference(this,
						CommonWAPreferences.USER_NAME) + "histrymaterial";
		return key;
	}

	private void saveMaterailHistry() {

		try {
			List<ReferCommonVO> list = getSavedMaterailHistry();
			List<ReferCommonVO> listcls = getSavedMaterailClsHistry();
			if (null != list) {
				SharedPreferences mSharedPreferences = getSharedPreferences(
						getKey(), MODE_PRIVATE);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(list);
				String personBase64 = new String(Base64.encodeBase64(baos
						.toByteArray()));
				SharedPreferences.Editor editor = mSharedPreferences.edit();
				editor.putString("materials", personBase64);
				editor.commit();
			}
			if (listcls != null) {

				SharedPreferences clsSharedPreferences = getSharedPreferences(
						getKey(), MODE_PRIVATE);
				ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
				ObjectOutputStream os1 = new ObjectOutputStream(baos1);
				os1.writeObject(listcls);
				String clsBase64 = new String(Base64.encodeBase64(baos1
						.toByteArray()));
				SharedPreferences.Editor clseditor = clsSharedPreferences
						.edit();
				clseditor.putString("materialscls", clsBase64);
				clseditor.commit();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<ReferCommonVO> getSavedMaterailHistry() {
		List<ReferCommonVO> refersForSave = null;
		List<ReferCommonVO> refers = null;
		try { // 读取已经保存的
			SharedPreferences mSharedPreferences = getSharedPreferences(
					getKey(), Context.MODE_PRIVATE);
			String personBase64 = mSharedPreferences.getString("materials", ""); // 读取物料参照
			if (null != personBase64 && !"".equals(personBase64)) {
				byte[] base64Bytes = Base64.decodeBase64(personBase64
						.getBytes());
				ByteArrayInputStream bais = new ByteArrayInputStream(
						base64Bytes);
				ObjectInputStream ois = new ObjectInputStream(bais);
				refers = (List<ReferCommonVO>) ois.readObject();

			}
			// 添加新的
			refersForSave = new ArrayList<ReferCommonVO>();
			refersForSave.addAll(commonForm.getRefers());
			if (null != refers) {
				for (ReferCommonVO p : refers) {
					if (!refersForSave.contains(p)) {
						refersForSave.add(p);
					}
					if (refersForSave.size() == 50) {
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return refersForSave;
	}

	private List<ReferCommonVO> getSavedMaterailClsHistry() {
		List<ReferCommonVO> refersForSave = null;
		List<ReferCommonVO> refers = null;
		try { // 读取已经保存的
			SharedPreferences mSharedPreferences = getSharedPreferences(
					getKey(), Context.MODE_PRIVATE);
			String personBase64 = mSharedPreferences.getString("materialscls",
					"");
			if (null != personBase64 && !"".equals(personBase64)) {
				byte[] base64Bytes = Base64.decodeBase64(personBase64
						.getBytes());
				ByteArrayInputStream bais = new ByteArrayInputStream(
						base64Bytes);
				ObjectInputStream ois = new ObjectInputStream(bais);
				refers = (List<ReferCommonVO>) ois.readObject();
			}
			// 添加新的
			refersForSave = new ArrayList<ReferCommonVO>();
			refersForSave.addAll(commonForm.getRefersclass());
			if (null != refers) {
				for (ReferCommonVO p : refers) {
					if (!refersForSave.contains(p)) {
						refersForSave.add(p);
					}
					if (refersForSave.size() == 50) {
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return refersForSave;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			// 找到需要赋值的view
			View view = this.findViewById(requestCode);
			if (view instanceof TextView) {
				AddressVO refer = (AddressVO) data
						.getSerializableExtra("address"); // 服务器返回的结果
				String name = refer.getName();
				String hint = (String) ((TextView) view).getHint();
				if (hint.equals("省")) {
					TextView city = (TextView) this
							.findViewById(requestCode + 1);
					city.setText("");
					TextView area = (TextView) this
							.findViewById(requestCode + 2);
					area.setText("");
					// 更新数据
					CFAddressView addressView = (CFAddressView) this
							.findViewById(requestCode + 3);
					addressView.processResultIntent(refer, resultCode, 1);
				} else if (hint.equals("市")) {
					// 更新界面
					TextView area = (TextView) this
							.findViewById(requestCode + 1);
					area.setText("");
					// 更新数据
					CFAddressView addressView = (CFAddressView) this
							.findViewById(requestCode + 2);
					addressView.processResultIntent(refer, resultCode, 2);
				} else {
					CFAddressView addressView = (CFAddressView) this
							.findViewById(requestCode + 1);
					addressView.processResultIntent(refer, resultCode, 3);
				}
				((TextView) view).setText(name);

			} else {
				AbsCommonFormView absview = (AbsCommonFormView) this
						.findViewById(requestCode);
				absview.processResultIntent(data, resultCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private LocationManagerProxy mAMapLocManager = null;

	public void requestLocation() {

		mAMapLocManager = LocationManagerProxy.getInstance(this);
		mAMapLocManager.setGpsEnable(true);
		mAMapLocManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 5000, 10, this);
		// handler1.postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// if (aMapLocation == null) {
		// submitBOData();
		// }
		// }
		//
		// }, 10000);// 设置超过10秒还没有定位到就停止定位
	}

	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(AMapLocation arg0) {
		if (arg0 != null) {
			// this.aMapLocation = arg0;// 判断超时机制
			Double geoLat = arg0.getLatitude();
			Double geoLng = arg0.getLongitude();
			Double altitude = arg0.getAltitude();
			gpsInfo.setHelevation(Double.toString(altitude));
			gpsInfo.setJlongitude(Double.toString(geoLng));
			gpsInfo.setWlatitude(Double.toString(geoLat));

			deactivate();
		}
	}

	// 真正提交业务数据的方法
	private void submitBOData() {
		CFSaveDataRequester requester = new CFSaveDataRequester(
				CommonFormActivity.this, handler, MSG_SUBMIT_OK);
		CFSaveDataVO saveData = commonForm.getSubmitData(pkdoc, eventid,
				visitid, workitemid, childid);
		if (null != saveData) {
			saveData.setGpsinfo(gpsInfo);
			requester.request(saveData);
		} else {
			progressDlg.dismiss();
		}
	}

	public void deactivate() {
		if (mAMapLocManager != null) {
			mAMapLocManager.removeUpdates(this);
			mAMapLocManager.destory();
		}
		mAMapLocManager = null;
	}

	@Override
	public void onResume() {
		super.onResume();
		requestLocation();
	}

}
