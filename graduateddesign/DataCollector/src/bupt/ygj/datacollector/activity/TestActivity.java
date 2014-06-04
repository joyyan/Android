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

public class TestActivity extends BaseActivity {

	private Button button;
	/** 界面提示 */
	private ProgressDialog progressDlg;

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
										if (which == 0) {

											CFTemplateManager manager = new CFTemplateManager(
													EventDetailActivity.this,
													handler,
													StaticString.MSG_WORKLIST_DETAIL);
											pkdoc = workItem.getPkdoc();
											itemname = workItem.getItemname();
											workitemid = workItem.getItemid();
											functioncode = workItem
													.getFunctioncode();
											winid = workItem.getWinid();
											manager.getCFTemplate(
													workItem.getFunctioncode(),
													workItem.getPkdoc(),
													workItem.getWinid());

										} else if (which == 1) {
											Intent intent = new Intent();
											intent.setClass(
													EventDetailActivity.this,
													CFListActivity.class);
											intent.putExtra("workitemid",
													workItem.getItemid());
											intent.putExtra("visitid",
													dayEvent_visitId);
											intent.putExtra("functioncode",
													workItem.getFunctioncode());
											intent.putExtra("pkdoc",
													workItem.getPkdoc());
											intent.putExtra("winid",
													workItem.getWinid());
											intent.putExtra("workname",
													workItem.getItemname());
											intent.putExtra("nodename",
													eventDetail
															.getChannelname());
											startActivity(intent);
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
