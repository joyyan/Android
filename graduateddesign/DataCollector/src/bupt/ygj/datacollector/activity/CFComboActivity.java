package bupt.ygj.datacollector.activity;

import java.util.ArrayList;

import wa.android.common.activity.BaseActivity;
import wa.android.libs.groupview.WAGroupView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import bupt.ygj.datacollector.R;
import bupt.ygj.datacollector.data.EnumValueVO;
import bupt.ygj.datacollector.elementview.ComboView;

/**
 * 下拉，枚举类型
 */
public class CFComboActivity extends BaseActivity implements OnClickListener {

	private Button backBtn;
	private TextView titleView;
	private ScrollView scrollview;
	private ArrayList<EnumValueVO> backeddata = new ArrayList<EnumValueVO>();
	private boolean ismultiselected;	//单选，多选状态属性，false为单选，true为多选
	private String title;				//标题名称

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		setContentView(R.layout.activity_combo);
		backBtn = (Button) findViewById(R.id.comboback);
		titleView = (TextView) findViewById(R.id.combotitle);
		backBtn.setOnClickListener(this);
		scrollview = (ScrollView) findViewById(R.id.scrollview);
		final WAGroupView groupview = new WAGroupView(this);
		scrollview.addView(groupview);
		
		Intent intent = getIntent();
		@SuppressWarnings("unchecked")
		ArrayList<EnumValueVO> enumlist = (ArrayList<EnumValueVO>)intent.getSerializableExtra("enumlist");
		//如果返回为null 那么为false也就是单选
		ismultiselected = intent.getBooleanExtra("ismultiselected", false); 
		title = intent.getStringExtra("title");
		titleView.setText(title);
		
		if(enumlist != null && enumlist.size() > 0) {
			for(final EnumValueVO ev : enumlist) {
				final ComboView comboview = new ComboView(this);
				comboview.setValue(ev.getText());
				comboview.setOnClickListener( new OnClickListener() {

					@Override
					public void onClick(View v) {
						if(ismultiselected) {	
							int status = comboview.getRightView().getVisibility();
							if(status == View.GONE) {
								comboview.getRightView().setVisibility(View.VISIBLE);
								backeddata.add(ev);
							} else if(status == View.VISIBLE) {
								comboview.getRightView().setVisibility(View.GONE);
								backeddata.remove(ev);
							}
						} else {
							int status = comboview.getRightView().getVisibility();
							if(status == View.GONE) {
								for(int i = 0 ;i<groupview.getChildCount();i++) {
									View view  = groupview.getChildAt(i);
									if(view instanceof ComboView) {
										((ComboView)view).getRightView().setVisibility(View.GONE);
									}
								}
								comboview.getRightView().setVisibility(View.VISIBLE);
								backeddata.clear();
								backeddata.add(ev);
							} else if(status == View.VISIBLE) {
								comboview.getRightView().setVisibility(View.GONE);
								backeddata.remove(ev);
							}
						}
					}
					
				});
				if(ev.isIsselect()) {
					comboview.getRightView().setVisibility(View.VISIBLE);
					backeddata.add(ev);
				}
				groupview.addRow(comboview);
			}
		} else {
			showNodataView();
		}
	}
		
	private void showNodataView() {
		
	}

	@Override
	public void onClick(View v) {
		backWithData();
	}

	private void backWithData() {
		Intent intent = getIntent();
		intent.putExtra("backeddata",backeddata);
		setResult(1, intent);
		this.finish();
	}
}
