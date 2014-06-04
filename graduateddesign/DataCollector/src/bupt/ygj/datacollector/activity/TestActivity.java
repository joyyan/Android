package bupt.ygj.datacollector.activity;

import wa.android.common.activity.BaseActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import bupt.ygj.datacollector.R;
import bupt.ygj.datacollector.constants.StaticString;
import bupt.ygj.datacollector.dataprovider.CFTemplateManager;
import bupt.ygj.datacollector.test.WorkItemVO;

public class TestActivity extends BaseActivity {

	private Button button;
	/** 界面提示 */
	private ProgressDialog progressDlg;
	private String pkdoc;
	private String itemname;
	private String workitemid;
	private String functioncode;
	private String winid;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		handler = new Handler() {
			volatile boolean isUpdateUI = false;
			Intent intent = new Intent();

			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case StaticString.MSG_WORKLIST_DETAIL:
					putGlobalVariable("TemplateVO", msg.obj);
					Intent i = new Intent(TestActivity.this,
							CommonFormActivity.class);
					i.putExtra("pkdoc", pkdoc);
					i.putExtra("functioncode", functioncode);
					i.putExtra("winid", winid);
					i.putExtra("eventid", "104");
					i.putExtra("visitid", "10201");
					i.putExtra("workitemid", workitemid);

					i.putExtra("pk_org", "pk_org0001");
					i.putExtra("itemname", itemname);
					startActivityForResult(i, 10);
					progressDlg.dismiss();
					break;
				default: // 网络失败或者不是200
					progressDlg.dismiss();
					showNoDataView(); // 列表被空页面代替
					break;

				}
			}
		};
		initView();
	}

	/**
	 * 
	 */
	protected void showNoDataView() {
		// TODO Auto-generated method stub

	}

	/**
	 * 初始化界面元素
	 */
	private void initView() {
		progressDlg = new ProgressDialog(this);
		progressDlg.setMessage(getString(R.string.dataLoading));
		progressDlg.setIndeterminate(true);
		progressDlg.setCancelable(false);
		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 进入自由表单

				new AlertDialog.Builder(TestActivity.this)
						.setTitle("")
						.setItems(
								getResources().getStringArray(
										R.array.eventDetailForm),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										WorkItemVO workItem;
										if (which == 0) {

											CFTemplateManager manager = new CFTemplateManager(
													TestActivity.this,
													handler,
													StaticString.MSG_WORKLIST_DETAIL);
											workItem = new WorkItemVO();
											String pkdoc = workItem.getPkdoc();
											String itemname = workItem
													.getItemname();
											String workitemid = workItem
													.getItemid();
											String functioncode = workItem
													.getFunctioncode();
											String winid = workItem.getWinid();
											manager.getCFTemplate(
													workItem.getFunctioncode(),
													workItem.getPkdoc(),
													workItem.getWinid());

										} else if (which == 1) {

										} else if (which == 2) {

										}
									}
								}).show();

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}

}
