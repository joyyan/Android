package bupt.ygj.datacollector.activity;

import wa.android.common.activity.BaseActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import bupt.ygj.datacollector.R;

public class CFPhotoDetailActivity extends BaseActivity implements
		OnClickListener {

	private ImageView photoView = null;
	private String filepath;
	private String filename;
	private String title;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cfphoto_detail);
		Intent intent = getIntent();
		filepath = intent.getStringExtra("filepath");
		filename = intent.getStringExtra("filename");
		title = intent.getStringExtra("title");
		initView();

	}

	private void initView() {
		TextView titleTextView = (TextView)findViewById(R.id.titlePanel__titleTextView);
		titleTextView.setText(title);
		Button rePhotoButton = (Button) findViewById(R.id.titlePanel__rightBtn);
		rePhotoButton.setText(R.string.rePhoto);
		rePhotoButton.setOnClickListener(this);
		Button cancelButton = (Button) findViewById(R.id.titlePanel_leftBtn);
		cancelButton.setOnClickListener(this);
		photoView = (ImageView) findViewById(R.id.image_cf_photo);

		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inSampleSize = 4;
		Bitmap bm = BitmapFactory.decodeFile(filepath + filename, option);
		photoView.setImageBitmap(bm);

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.titlePanel__rightBtn) {
			Intent intent = getIntent();
			this.setResult(1, intent);
			this.finish();
		} else if (id == R.id.titlePanel_leftBtn) {
			Intent intent = getIntent();
			this.setResult(2, intent);
			this.finish();
		}
	}

}
