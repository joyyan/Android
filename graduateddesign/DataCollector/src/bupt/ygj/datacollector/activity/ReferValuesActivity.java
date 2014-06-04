package bupt.ygj.datacollector.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wa.android.common.activity.BaseActivity;
import wa.android.libs.listview.WALoadListView;
import wa.android.libs.listview.WALoadListView.OnRefreshListener;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import bupt.ygj.datacollector.R;
import bupt.ygj.datacollector.adapter.ReferValuesAdapter;
import bupt.ygj.datacollector.data.ExceptionEncapsulationVO;
import bupt.ygj.datacollector.data.ReferCommonVO;
import bupt.ygj.datacollector.datarequester.ReferValuesRequester;

public class ReferValuesActivity extends BaseActivity {
	private Handler handler;
	private Button backButton;
	private WALoadListView staffListView;		/** 人员列表View */
	private ProgressDialog progressDlg;
	private LinearLayout noDataPanel;		/** 没有数据页面 */
	
	private String pk_org = "";		
	private String referto = "";
	private String condition = "";
	
	private List<ReferCommonVO> referList = new ArrayList<ReferCommonVO>();
	private ReferCommonVO selectedrefer = new ReferCommonVO();
	private ReferValuesAdapter adapter;
	
	private int pageCount = 1;		//记录当前的list页数
	private boolean up = false;		//是否为上拉加载的动作，true为是
	private TextView titletext;
	
	@SuppressLint("HandlerLeak")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refervalues);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					Map map = (Map) msg.obj;
					updateList(map);
					progressDlg.dismiss();
					staffListView.onRefreshComplete();
					break;
				case 4:
					Map mapforerror = (Map) msg.obj;
					ExceptionEncapsulationVO ex = (ExceptionEncapsulationVO) mapforerror.get("exception");
//					toastMsg(ex.getMessageList().get(0));
					updateList(mapforerror);
					progressDlg.dismiss();
					staffListView.onRefreshComplete();
					break;
				case 5:
					Map mapforflag = (Map) msg.obj;
					ExceptionEncapsulationVO exflge = (ExceptionEncapsulationVO) mapforflag.get("flagexception");
					toastMsg(exflge.getFlagmessageList().get(0));
					updateList(mapforflag);
					progressDlg.dismiss();
					staffListView.onRefreshComplete();
					break;
				case -10:	//网络失败或者不是200
					showNoDataView();	//列表被空页面代替
					progressDlg.dismiss();
					staffListView.onRefreshComplete();
					break;
				}
			}
		};
		initView(); 
		initData();
	}

	private void updateList(Map map) {
		List<ReferCommonVO> mlbv = (List<ReferCommonVO>)map.get("inventory");		
		if(null != mlbv && mlbv.size() != 0) {
			if(!up) {
				this.referList.clear();
			}
			this.referList.addAll(mlbv);
			adapter.notifyDataSetChanged();
			if(mlbv.size() < 25)
				staffListView.setCanLoad(false);
			else
				staffListView.setCanLoad(true);
			staffListView.onRefreshComplete();
			staffListView.setVisibility(View.VISIBLE);
			noDataPanel.setVisibility(View.GONE);
		} else {
			if(up) {
				toastMsg("没有更多数据");
				staffListView.setCanLoad(false);
			} else {
				showNoDataView();
			}
		}
	}
	
	private void showNoDataView() {
		staffListView.onRefreshComplete();
		staffListView.setVisibility(View.GONE);
		noDataPanel.setVisibility(View.VISIBLE);
	}
	
	private void initData() {
		adapter = new ReferValuesAdapter(ReferValuesActivity.this, referList);
		staffListView.setAdapter(adapter);
		Intent intent = getIntent();
		pk_org = intent.getStringExtra("pk_org");
		referto = intent.getStringExtra("referto");
		condition = intent.getStringExtra("condition");
		String itemname = intent.getStringExtra("itemname");
		condition = "";
		titletext.setText(itemname);
		ReferValuesRequester rvr = new ReferValuesRequester(ReferValuesActivity.this,handler);
		progressDlg.show();
		rvr.getReferInventoryValues(pk_org, referto, condition, "1");
	}

	private void initView() {
		progressDlg = new ProgressDialog(this);
		progressDlg.setMessage(getResources().getString(R.string.loading));
		progressDlg.setIndeterminate(true);
		progressDlg.setCancelable(false);
		backButton = (Button) findViewById(R.id.backBtn);
		titletext = (TextView) findViewById(R.id.title);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				backWithData();
			}
		});
		staffListView = (WALoadListView) findViewById(R.id.refervalueslist);		
		staffListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onUpRefresh() {
				up = false;
				pageCount = 1;
				ReferValuesRequester rvr = new ReferValuesRequester(ReferValuesActivity.this,handler);
				progressDlg.show();
				rvr.getReferInventoryValues(pk_org, referto, condition, "1");
			}

			@Override
			public void onDownRefresh() {
				up = true;
				int startline3 = pageCount * 25 + 1;
				ReferValuesRequester rvr = new ReferValuesRequester(ReferValuesActivity.this,handler);
				progressDlg.show();
				rvr.getReferInventoryValues(pk_org, referto, condition, String.valueOf(startline3));
				pageCount = pageCount + 1;
			}
		});
		staffListView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long id) {
				
				ReferCommonVO refer = referList.get((int)id);
				if(refer.getIsSelected()) {
					refer.setIsSelected(false);
					selectedrefer = refer;
				} else {
					for(ReferCommonVO re : referList) {
						re.setIsSelected(false);
					}
					refer.setIsSelected(true);
					selectedrefer = refer;
				}
				adapter.notifyDataSetInvalidated();
			}
		});
		noDataPanel = (LinearLayout) findViewById(R.id.stafflist_nodataPanel);		// 没有数据panel
	}

	private void backWithData() {
		Intent intent = getIntent();
		intent.putExtra("refervalue",selectedrefer);
		setResult(3, intent);
		this.finish();
	}
}
