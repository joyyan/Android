package bupt.ygj.datacollector.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import wa.android.common.activity.BaseActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import bupt.ygj.datacollector.R;
import bupt.ygj.datacollector.adapter.AddressSortAdapter;
import bupt.ygj.datacollector.data.AddressListVO;
import bupt.ygj.datacollector.data.AddressVO;
import bupt.ygj.datacollector.data.ExceptionEncapsulationVO;
import bupt.ygj.datacollector.dataprovider.AddressProvider;
import bupt.ygj.datacollector.elementview.SideBarView;
import bupt.ygj.datacollector.elementview.SideBarView.OnTouchingLetterChangedListener;
import bupt.ygj.datacollector.util.PinyinComparator;
import bupt.ygj.datacollector.util.pinyin4jUtils;

public class AddressReferActivity extends BaseActivity  {
	
	private Handler handler;
	private ProgressDialog progressDlg;
	private ListView sortListView;
	private SideBarView sideBar;
	private TextView dialog;
	private TextView titleview;
	private AddressSortAdapter adapter;
	private List<AddressVO> list = new ArrayList<AddressVO>();
	private PinyinComparator pinyinComparator = new PinyinComparator();;

	private String contextid;		//请求参数
	private Button back;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addressrefer);
		
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					Map map = (Map) msg.obj;
					updateUI(map);
					progressDlg.dismiss();
					break;
				case 4:
					Map mapforerror = (Map) msg.obj;
					ExceptionEncapsulationVO ex = (ExceptionEncapsulationVO) mapforerror
							.get("exception");
					toastMsg(ex.getMessageList().get(0));
					updateUI(mapforerror);
					progressDlg.dismiss();
					break;
				case 5:
					Map mapforflag = (Map) msg.obj;
					ExceptionEncapsulationVO exflge = (ExceptionEncapsulationVO) mapforflag
							.get("flagexception");
					toastMsg(exflge.getFlagmessageList().get(0));
					updateUI(mapforflag);
					progressDlg.dismiss();
					break;
				case -10: 
					toastMsg("网络错误");
					progressDlg.dismiss();
					showNoDataView();
					break;
				}
			}
		};
		initViews();
//		initData();
	}
	
	private void initData() {
		Intent intent = getIntent();
		contextid = intent.getStringExtra("contextid");
		contextid = contextid != null ? contextid : "";
		progressDlg.show();
		AddressProvider addp = new AddressProvider(this, handler);
		addp.getLowerLevelAddressReferTo(contextid);
	}

	private void initViews() {
		progressDlg = new ProgressDialog(this);
		progressDlg.setMessage(getResources().getString(R.string.dataLoading));
		progressDlg.setCancelable(false);
		progressDlg.setIndeterminate(true);
		Intent intent = getIntent();
		String title = intent.getStringExtra("titlename");
		
		titleview = (TextView)findViewById(R.id.addtitle);
		titleview.setText(title);
		back = (Button) findViewById(R.id.addback);
		back.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AddressVO address = list.get(position);
				Intent intent = getIntent();
				intent.putExtra("address",address);
				setResult(1, intent);
				finish();
			}
		});
		
		
		AddressListVO addresslist = (AddressListVO)removeGlobalVariable("addresslist");
		
		List<String> strings = new ArrayList<String>();
		if(null != addresslist && addresslist.getList() != null && addresslist.getList().size() != 0) {
			list = addresslist.getList();
			filledletter(list);
			
			for(AddressVO stm : list) {
				if(!strings.contains(stm.getSortLetters())) {
					strings.add(stm.getSortLetters());
				}
			}
			long start = System.currentTimeMillis();
			Collections.sort(strings);
			Collections.sort(list, pinyinComparator);
			long end = System.currentTimeMillis();
			Log.d(getClass().getSimpleName(), "拼音用时：" + (end - start) + "毫秒");
			adapter = new AddressSortAdapter(this, list);
			sortListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		
		
		long start1 = System.currentTimeMillis();
		sideBar = (SideBarView) findViewById(R.id.sidrbar);
		sideBar.setData(strings);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					sortListView.setSelection(position);
				}
			}
		});
		long end1 = System.currentTimeMillis();
		Log.d(getClass().getSimpleName(), "VIew用时：" + (end1 - start1) + "毫秒");
	}

	private List<AddressVO> filledletter(List<AddressVO> list){
		for(int i=0; i<list.size(); i++){
			 //汉字转换成拼音  
			String pinyin = pinyin4jUtils.hanziToPinyin((list.get(i).getName()), "");
			String sortString = "";
			if(pinyin != null && !pinyin.equals(""))
				sortString = pinyin.substring(0, 1).toUpperCase();
			// 正则表达式，判断首字母是否是英文字母  
			if(sortString.matches("[A-Z]")){
				list.get(i).setSortLetters(sortString.toUpperCase());
			}else{
				list.get(i).setSortLetters("#");
			}
		}
		return list;
	}
	
	private void updateUI(Map map) {
		AddressListVO addresslist = (AddressListVO)map.get("addresslist");
		if(null != addresslist && addresslist.getList() != null && addresslist.getList().size() != 0) {
			list = addresslist.getList();
			filledletter(list);
			List<String> strings = new ArrayList<String>();
			for(AddressVO stm : list) {
				if(!strings.contains(stm.getSortLetters())) {
					strings.add(stm.getSortLetters());
				}
			}
			Collections.sort(strings);
			Collections.sort(list, pinyinComparator);
			adapter = new AddressSortAdapter(this, list);
			sortListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	}
	
	private void showNoDataView() {
		
		
	}
}
