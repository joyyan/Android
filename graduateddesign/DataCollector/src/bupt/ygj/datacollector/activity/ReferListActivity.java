package bupt.ygj.datacollector.activity;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import wa.android.common.activity.BaseActivity;
import wa.android.common.utils.PreferencesUtil;
import wa.android.constants.CommonWAPreferences;
import wa.android.libs.listview.WALoadListView;
import wa.android.libs.listview.WALoadListView.OnRefreshListener;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import bupt.ygj.datacollector.R;
import bupt.ygj.datacollector.adapter.MaterialReferListAdapter;
import bupt.ygj.datacollector.data.ExceptionEncapsulationVO;
import bupt.ygj.datacollector.data.ReferCommonVO;
import bupt.ygj.datacollector.datarequester.ReferInventoryRequester;

public class ReferListActivity extends BaseActivity {

	private Handler handler;
	private Button backButton;
	private EditText searchEditText;			/** 查询条件输入框 */
	private ImageView staffsearch_delete;		/** 删除图标 */
	private WALoadListView staffListView;		/** 人员列表View */
	private ListView histryListView;			/** 搜索条件缓存 */ 
	private List<String> historyData = new ArrayList<String>();		/** 搜索记录 */
	private ArrayAdapter<String> listadapter;
	private ProgressDialog progressDlg;
	private LinearLayout noDataPanel;		/** 没有数据页面 */
	private RelativeLayout dataPanel;		/** 有数据页面 */
	private boolean isKeyUp = false;		/** 搜索视图切换 */
	
	private String condition = "";			//保存搜索条件
	private String pk_org = "";		
	private String category = "";		
	
	//关联项请求所需要的参数 - start
	private String itemkey = "";		
	private String referto = "";		
	private ArrayList<String> relatedpathlist ;
	//--end
	
	private List<ReferCommonVO> referlistall = new ArrayList<ReferCommonVO>();
	private int pageCount = 1;		//记录当前的list页数
	private boolean up = false;		//是否为上拉加载的动作，true为是
	private MaterialReferListAdapter adapter;
	private TextView titleView;
	
	private TextWatcher textWatcher = new TextWatcher() {  
        
        @Override    
        public void afterTextChanged(Editable s) {     
            Log.d("TAG","afterTextChanged--------------->");   
        }   
          
        @Override 
        public void beforeTextChanged(CharSequence s, int start, int count,  
                int after) {  
            Log.d("TAG","beforeTextChanged--------------->");  
        }  
 
         @Override    
        public void onTextChanged(CharSequence s, int start, int before,     
                int count) {     
            Log.d("TAG","onTextChanged--------------->");    
            String str = searchEditText.getText().toString();  
            if(str != null && str.length() > 0) 
            	staffsearch_delete.setVisibility(View.VISIBLE);
            else
            	staffsearch_delete.setVisibility(View.GONE);
        }                    
    };  
	
	@SuppressLint("HandlerLeak")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personlist);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					Map map = (Map) msg.obj;
					updateList(map);
					progressDlg.dismiss();
					break;
				case 4:
					Map mapforerror = (Map) msg.obj;
					ExceptionEncapsulationVO ex = (ExceptionEncapsulationVO) mapforerror.get("exception");
//					toastMsg(ex.getMessageList().get(0));
					updateList(mapforerror);
					progressDlg.dismiss();
					break;
				case 5:
					Map mapforflag = (Map) msg.obj;
					ExceptionEncapsulationVO exflge = (ExceptionEncapsulationVO) mapforflag.get("flagexception");
					toastMsg(exflge.getFlagmessageList().get(0));
					updateList(mapforflag);
					progressDlg.dismiss();
					break;
				case -10:	//网络失败或者不是200
					showNoDataView();	//列表被空页面代替
					progressDlg.dismiss();
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
				this.referlistall.clear();
			}
			this.referlistall.addAll(mlbv);
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
				staffListView.onRefreshComplete();
			} else {
				showNoDataView();
			}
		}
	}
	
	private void showNoDataView() {
		staffListView.onRefreshComplete();
		histryListView.setVisibility(View.GONE);
		staffListView.setVisibility(View.GONE);
		noDataPanel.setVisibility(View.VISIBLE);
	}
	
	private void initData() {
		adapter = new MaterialReferListAdapter(this,referlistall);
		staffListView.setAdapter(adapter);
		Intent intent = getIntent();
		category = intent.getStringExtra("category");			//保存搜索条件
		pk_org = intent.getStringExtra("pk_org");
		
		itemkey = intent.getStringExtra("itemkey");
		referto = intent.getStringExtra("pk_org");
		relatedpathlist = (ArrayList<String>) intent.getSerializableExtra("relatedpathlist");
		
		if(category.equals("materials")) {
			titleView.setText("搜索物料");
			searchEditText.setHint("搜索物料");
		} else {
			titleView.setText("搜索物料分类");
			searchEditText.setHint("搜索物料分类");
		}
		Map map = new HashMap();
		
		map.put("inventory", getSavedReciversHistry(category));
		updateList(map);
	}
	
	private String getKey() {
		String key = PreferencesUtil.readPreference(this,CommonWAPreferences.SERVER_IP) 
				+ PreferencesUtil.readPreference(this,CommonWAPreferences.SERVER_PORT) 
 			   + PreferencesUtil.readPreference(this, CommonWAPreferences.USER_NAME)
 			   + "histrymaterial";
		return key;
	}
	
	private List<ReferCommonVO> getSavedReciversHistry(String category) {
		List<ReferCommonVO> personForSave = null;
		try {  
	       SharedPreferences mSharedPreferences = getSharedPreferences(getKey(), Context.MODE_PRIVATE);  
	       String personBase64 = mSharedPreferences.getString(category, "");  
	       if(null != personBase64 && !"".equals(personBase64)) {
		       byte[] base64Bytes = Base64.decodeBase64(personBase64.getBytes());  
		       ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);  
		       ObjectInputStream ois = new ObjectInputStream(bais);  
		       personForSave = (List<ReferCommonVO>) ois.readObject();
	       }
	     } catch (Exception e) {  
	       e.printStackTrace();  
	     }
		if(null != personForSave) {
			for(ReferCommonVO p : personForSave) {
				p.setIsSelected(false);
			}
		}
		return personForSave;
	}

	private void initView() {
		progressDlg = new ProgressDialog(this);
		progressDlg.setMessage(getResources().getString(R.string.loading));
		progressDlg.setIndeterminate(true);
		progressDlg.setCancelable(false);
		
		backButton = (Button) findViewById(R.id.tasktitlepanel_leftBtn);
		titleView = (TextView) findViewById(R.id.tasktitlepanel_titleTextView);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		histryListView = (ListView) findViewById(R.id.search_listview);		//搜索历史列表
		staffListView = (WALoadListView) findViewById(R.id.Staff_list);		//搜素人员列表
		staffListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onUpRefresh() {
				up = false;
				pageCount = 1;
				requestData(category,"1");
			}

			@Override
			public void onDownRefresh() {
				up = true;
				int startline3 = pageCount * 25 + 1;
				requestData(category,String.valueOf(startline3));
				pageCount = pageCount + 1;
			}
		});
		staffListView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long id) {
				TextView tv = (TextView)arg1.findViewById(R.id.refer_selectedicon);
				tv.setVisibility(View.VISIBLE);
				backWithData(id);
			}
		});
		listadapter = new ArrayAdapter<String>(getBaseContext(),R.layout.layout_searchhistorylist, historyData);
		histryListView.setAdapter(listadapter);
		noDataPanel = (LinearLayout) findViewById(R.id.stafflist_nodataPanel);		// 没有数据panel
		dataPanel = (RelativeLayout) findViewById(R.id.stafflist_dataPanel);		// 搜索条件缓存列表视图
		searchEditText = (EditText) findViewById(R.id.staffsearch_editText);		// 搜索编辑框
		searchEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				ctrSearchEdit();
			}
		});
		
		searchEditText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ctrSearchEdit();
			}
		});
		final String str11 = this.getResources().getString(R.string.all); 
		searchEditText.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				switch (event.getAction()) {
				case KeyEvent.ACTION_DOWN:
					if (keyCode == 66 || keyCode ==20 || keyCode == 99) {
						if (searchEditText.getText().toString().equals("")) {
							return false;
						} else {
							SharedPreferences sharedPreferences = getSharedPreferences("", MODE_PRIVATE);
							Editor editor = sharedPreferences.edit();
							List<String> history = new ArrayList<String>();
							history.add(sharedPreferences.getString("1", null));
							history.add(sharedPreferences.getString("2", null));
							history.add(sharedPreferences.getString("3", null));
							history.add(sharedPreferences.getString("4", null));
							history.add(sharedPreferences.getString("5", null));
							condition = searchEditText.getText().toString();
							if (!condition.equals("")&& !((history.get(0) != null && condition.equals(history.get(0))) || (history.get(0) == null && condition.equals(str11))))
								history.add(0, condition);
							editor.clear();
							editor.putString("", str11);
							int i = 0;
							for (i = 1; i <= 5; i++) {
								if (history.get(i - 1) != null) 
									editor.putString(Integer.valueOf(i).toString(), history.get(i - 1));
								else
									break;
							}
							editor.commit();
							staffListView.setVisibility(View.VISIBLE);
							histryListView.setVisibility(View.GONE);
							// 隐藏键盘
							InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
							isKeyUp = true;
							
							up = false;
							pageCount = 1;
							
							requestData(category,"1");
						}
					}
					break;
				}
				return false;
			}
		});
		histryListView.setOnItemClickListener(new OnItemClickListener() {		// 搜索缓存条目的监听事件

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3) {
				if (historyData.get(position).equals(str11)) {
					condition = "";
				} else {
					condition = historyData.get(position);
				}
				searchEditText.setText(condition);
				staffListView.setVisibility(View.VISIBLE);
				histryListView.setVisibility(View.GONE);
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
				
				up = false;
				pageCount = 1;
				requestData(category,"1");
			}
		});
		staffsearch_delete = (ImageView) findViewById(R.id.staffsearch_delete);
		staffsearch_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				staffsearch_delete.setImageResource(R.drawable.search_delete_touch);
				String str = searchEditText.getText().toString();
				int end = str.length();
				if (end > 0) {
					searchEditText.setText("");
					staffsearch_delete.setImageResource(R.drawable.search_delete);
				}
			}
		});
		searchEditText.addTextChangedListener(textWatcher);
	}
	
	private void ctrSearchEdit() {
		if (!isKeyUp) {
			if (noDataPanel.getVisibility() == View.VISIBLE) {
				noDataPanel.setVisibility(View.GONE);
			}
			staffListView.setVisibility(View.GONE);
			histryListView.setVisibility(View.VISIBLE);
			historyData.clear();
			SharedPreferences sharedPreferences = getSharedPreferences("", MODE_PRIVATE);
			for (int i = 1; i <= 5; i++) {
				String str = sharedPreferences.getString(Integer.valueOf(i).toString(), null);
				if (str != null) {
					historyData.add(str);
				}
			}
			historyData.add((String) ReferListActivity.this.getResources().getText(R.string.all));
			listadapter.notifyDataSetChanged();
		}
		isKeyUp = false;
	}
	
	private void requestData(String category,String startline) {
		ReferInventoryRequester psonlistProvider = new ReferInventoryRequester(ReferListActivity.this,handler);
		if(category.equals("materials")) {
			progressDlg.show();
			psonlistProvider.getReferInventoryValues(pk_org, condition, startline);
		} else if(category.equals("materialscls")) {
			progressDlg.show();
			psonlistProvider.getReferInventoryClassValues(pk_org, condition, startline);
		} 
	}

	private void showStaffListview() {
		dataPanel.setVisibility(View.VISIBLE);
		staffListView.setVisibility(View.VISIBLE);
		noDataPanel.setVisibility(View.GONE);
		histryListView.setVisibility(View.GONE);
	}

	private void backWithData(long index) {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
		Intent intent = getIntent();
		intent.putExtra("material",referlistall.get((int)index));
		setResult(1, intent);
		this.finish();
	}
}
