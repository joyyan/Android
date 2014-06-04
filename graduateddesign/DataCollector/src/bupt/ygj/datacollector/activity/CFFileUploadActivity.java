package bupt.ygj.datacollector.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wa.android.common.activity.BaseActivity;
import wa.android.libs.groupview.OnAttachmentOpenedActions;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import bupt.ygj.datacollector.R;
import bupt.ygj.datacollector.data.AttachmentVO;
import bupt.ygj.datacollector.data.RecordData;
import bupt.ygj.datacollector.view.AttachmentListView;
import bupt.ygj.datacollector.view.AttachmentView;

@SuppressLint("HandlerLeak")
public class CFFileUploadActivity extends BaseActivity implements
		OnClickListener {

	private final int REQUEST_PHOTO = 3;
	private final int REQUEST_RECORD = 4;
	/** 照片列表pannel */
	private LinearLayout attachListLayout;
	/** 录音列表 */
	private LinearLayout recordLayout;
	/** 照片列表 */
	private List<AttachmentVO> attachList = new ArrayList<AttachmentVO>();
	/** 照片数量 */
	private int picNumber = 0;
	/** 获取到图片路径 */
	private String filePath;
	private String fileName;
	private boolean hasRecord = false;
	private boolean addSuccess = false;
	private SeekBar seekBar;
	private DelayThread dThread;
	private RecordData newRecordItem = new RecordData();

	private TextView recordTip;
	private LinearLayout recordInfo;
	private ImageView addRecordIcon, recordPlayIcon;
	private TextView recordNameView, recordSizeView, recordPlayInfoView,
			recordTimeView;

	private String recordType = "";
	private MediaPlayer mediaPlayer;// 录音播放器
	private boolean firstPlay = true;

	private String filenameforr;

	@Override
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cf_activity_fileupload);
		Bundle bundle = this.getIntent().getExtras();
		attachList = (List<AttachmentVO>) bundle.getSerializable("attachList");
		initViews();
	}

	private void initViews() {
		// tittle
		TextView tittleTextView = (TextView) findViewById(R.id.titlePanel__titleTextView);
		tittleTextView.setText(R.string.file_upload);
		Button rightButton = (Button) findViewById(R.id.titlePanel__rightBtn);
		rightButton.setVisibility(View.INVISIBLE);
		// 返回按钮
		Button backButton = (Button) findViewById(R.id.titlePanel_leftBtn);
		backButton.setOnClickListener(this);
		backButton.setText(R.string.cancel);
		backButton.setBackgroundColor(getResources().getColor(R.color.nav_bkgrd));
		attachListLayout = (LinearLayout) findViewById(R.id.fileUpload_listPanel);
		recordLayout = (LinearLayout) findViewById(R.id.fileUpload_newRecord);
		recordLayout.setOnClickListener(this);

		addRecordIcon = (ImageView) findViewById(R.id.fileUpload_addRecordIcon);
		addRecordIcon.setOnClickListener(this);
		recordTip = (TextView) findViewById(R.id.fileUpload_recordTip);
		recordInfo = (LinearLayout) findViewById(R.id.fileUpload_recordInfo);
		recordPlayIcon = (ImageView) findViewById(R.id.fileUpload_recordPlay);
		recordPlayIcon.setOnClickListener(this);
		recordNameView = (TextView) findViewById(R.id.fileUpload_recordName);
		recordSizeView = (TextView) findViewById(R.id.fileUpload_recordSize);
		recordPlayInfoView = (TextView) findViewById(R.id.fileUpload_recordPlayInfo);
		recordTimeView = (TextView) findViewById(R.id.fileUpload_recordTime);
		seekBar = (SeekBar) findViewById(R.id.fileUpload_seekbar);
		if (hasRecord) {
			showRecordInfo();
		} else {
			showRecordTip();
		}

		updateAttachmentListView();

	}

	private void showRecordTip() {
		addRecordIcon.setBackgroundResource(R.drawable.addicon);
		addRecordIcon.setClickable(false);
		recordPlayIcon.setClickable(false);
		recordTip.setVisibility(View.VISIBLE);
		recordInfo.setVisibility(View.GONE);
	}

	private void showRecordInfo() {
		addRecordIcon.setBackgroundResource(R.drawable.delicon);
		addRecordIcon.setClickable(true);
		recordPlayIcon.setClickable(true);
		recordTip.setVisibility(View.GONE);
		recordInfo.setVisibility(View.VISIBLE);
	}

	private void updateAttachmentListView() {
		attachListLayout.removeAllViews();
		picNumber = 0;
		for (int i = 0; i < attachList.size(); i++) {
			final AttachmentVO attachment = attachList.get(i);
			if (attachment.getFiletype().equals(".jpg")) {
				String filename = attachment.getFilename();
				// 文件名最多只能显示10个字，多了则截断
				if (null != filename && filename.length() > 18) {
					filename = filename.substring(0, 18);
				}
				AttachmentListView attachmentRow = new AttachmentListView(this);
				if (picNumber == 0) {
				} else {
					float scale = getResources().getDisplayMetrics().density;
					int top = (int) ((-1) * scale);
					((LinearLayout.LayoutParams) attachmentRow
							.getLayoutParams()).topMargin = top;
				}

				final String filesize = attachment.getFilesize();

				attachmentRow.setTitle(filename);
				attachmentRow.setSize(filesize + "KB");
				// attachmentRow.getLeftIconImageView().setBackgroundResource(
				// R.drawable.icon_jpg);
				attachmentRow.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String path = attachment.getPath()
								+ attachment.getFilename() + ".jpg";
						String filetype = attachment.getFiletype().replace(".",
								"");
						File file = new File(path);
						try {
							startActivity(OnAttachmentOpenedActions
									.getAttachmentIntent(file, filetype));
						} catch (Exception e) {
							toastMsg(getString(R.string.noapptoopen));
						}

					}
				});

				AttachmentView filledAttachmentView = new AttachmentView(this,
						attachmentRow);
				// 删除照片
				filledAttachmentView.getLeftIconImageView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								attachList.remove(attachment);
								updateAttachmentListView();
							}
						});

				attachListLayout.addView(filledAttachmentView);
				picNumber++;
			} else if (attachment.getFiletype().equals(".m4a")) {
				hasRecord = true;
				newRecordItem = (RecordData) attachment;
				updateRecordView();
			}

		}
		AttachmentView attviews = new AttachmentView(this, taskPhotoView());
		attviews.getLeftIconImageView().setBackgroundResource(
				R.drawable.addicon);
		attachListLayout.addView(attviews);

	}

	private AttachmentListView taskPhotoView() {
		// 拍照接口
		AttachmentListView takePhotoRow = new AttachmentListView(this);
		takePhotoRow.setTitle(getResources().getString(R.string.takePhoto));
		takePhotoRow.getTitleTextView().setTextColor(
				getResources().getColor(R.color.list_text_black));
		takePhotoRow.remove();
		if (picNumber != 0) {
			// takePhotoRow.setBackgroundResource(R.drawable.newattachlistbottom);
			float scale = getResources().getDisplayMetrics().density;
			int top = (int) ((-1) * scale);
			((LinearLayout.LayoutParams) takePhotoRow.getLayoutParams()).topMargin = top;
		} else {
			// takePhotoRow.setBackgroundResource(R.drawable.newattachlistsingle);
		}
		takePhotoRow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 执行拍照前判断SD卡是否存在
				String SDState = Environment.getExternalStorageState();
				if (SDState.equals(Environment.MEDIA_MOUNTED)) {

					filePath = Environment.getExternalStorageDirectory()
							+ "/myImage/";
					File dir = new File(filePath);

					if (!dir.exists()) {
						dir.mkdirs();
					}

					Intent intent = new Intent(
							android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					fileName = createFileName();
					File file = new File(dir, fileName);
					Uri r = Uri.fromFile(file);
					intent.putExtra(ImageColumns.ORIENTATION, 0);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, r);
					startActivityForResult(intent, REQUEST_PHOTO);

				} else {
					toastMsg("内存卡不存在");
				}

			}
		});
		return takePhotoRow;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_PHOTO) {

			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				Log.v("TestFile",
						"SD card is not avaiable/writeable right now.");
				return;
			}

			try {
				File file = new File(filePath + fileName);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				Bitmap bitmap = BitmapFactory.decodeFile(
						file.getAbsolutePath(), options);
				file.delete();
				FileOutputStream fos = new FileOutputStream(file);
				bitmap.compress(CompressFormat.JPEG, 70, fos);
				fos.close();
				AttachmentVO newAttachItem = new AttachmentVO();
				newAttachItem.setFileid("");
				newAttachItem.setPath(filePath);
				newAttachItem.setFilename(filenameforr);
				newAttachItem.setFilesize(Long.toString(file.length() / 1024));
				if (fileName.contains(".")) {
					String type = fileName.substring(fileName.lastIndexOf('.'));
					newAttachItem.setFiletype(type);
				}

				calculateFileList(newAttachItem);
				if (addSuccess) {
					updateAttachmentListView();
				}
			} catch (Exception e) {
				Toast.makeText(this, "存储照片失败！", Toast.LENGTH_SHORT).show();
			}

		} else if (resultCode == Activity.RESULT_OK
				&& requestCode == REQUEST_RECORD) {
			recordType = data.getStringExtra("recordType");
			if (recordType.equals("new")) {
				newRecordItem = new RecordData();
				newRecordItem.setFileid("");
				newRecordItem.setPath(data.getStringExtra("filePath"));
				newRecordItem.setFilename(data.getStringExtra("fileName"));
				newRecordItem.setFilesize(data.getStringExtra("fileSize"));
				newRecordItem.setFiletype(".m4a");
				newRecordItem.setRecordTime(data.getStringExtra("recordTime"));
				newRecordItem.setTotalTs(data.getStringExtra("totalTs"));
			} else if (recordType.equals("old")) {
				attachList.remove(newRecordItem);
				newRecordItem = new RecordData();
				newRecordItem.setFileid("");
				newRecordItem.setPath(data.getStringExtra("filePath"));
				newRecordItem.setFilename(data.getStringExtra("fileName"));
				newRecordItem.setFilesize(data.getStringExtra("fileSize"));
				newRecordItem.setFiletype(".m4a");
				newRecordItem.setRecordTime(data.getStringExtra("recordTime"));
				newRecordItem.setTotalTs(data.getStringExtra("totalTs"));
			}
			calculateFileList(newRecordItem);
			if (addSuccess) {
				hasRecord = true;
				updateRecordView();
			}

		}

	}

	private void calculateFileList(AttachmentVO newAttachItem) {
		int picnum = 0;
		for (int i = 0; i < attachList.size(); i++) {
			if (attachList.get(i).getFiletype().equals(".jpg")) {
				picnum++;
			}
		}
		if ((newAttachItem.getFiletype().equals(".m4a") || picnum < 10)
				&& Integer.parseInt(newAttachItem.getFilesize().replace("KB",
						"")) <= 2048) {// 单个附件不大于200K，附件总数10个
			int totalsize = 0;
			for (AttachmentVO attachmentVO : attachList) {
				String filesize = attachmentVO.getFilesize().replace("KB", "");
				totalsize += Integer.parseInt(filesize);
			}
			if (totalsize
					+ Integer.parseInt(newAttachItem.getFilesize().replace(
							"KB", "")) <= 2048) {
				attachList.add(newAttachItem);
				addSuccess = true;
			} else {
				addSuccess = false;
				Toast.makeText(this, "附件总和大于2M,更多附件上传请使用电脑", Toast.LENGTH_SHORT)
						.show(); // 附件总大小大于2M
			}
		} else if (Integer.parseInt(newAttachItem.getFilesize().replace("KB",
				"")) > 2048) {
			addSuccess = false;
			Toast.makeText(this, R.string.fileSignalSizePre, Toast.LENGTH_SHORT)
					.show();
		} else if (picnum == 10 && newAttachItem.getFiletype().equals(".jpg")) {
			addSuccess = false;
			Toast.makeText(this, "更多附件上传请使用电脑", Toast.LENGTH_SHORT).show();
		}

	}

	private void updateRecordView() {
		showRecordInfo();
		recordNameView.setText(newRecordItem.getFilename()
				+ newRecordItem.getFiletype());
		recordSizeView.setText(newRecordItem.getFilesize());
		recordPlayInfoView.setText("00:00/" + newRecordItem.getTotalTs());
		recordTimeView.setText("录制于" + newRecordItem.getRecordTime());

	}

	@SuppressLint("SimpleDateFormat")
	private String createFileName() {

		String fileName = "";
		Date date = new Date(System.currentTimeMillis()); // 系统当前时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		fileName = dateFormat.format(date);
		filenameforr = fileName;
		return fileName + ".jpg";
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.titlePanel_leftBtn) {
			Intent intent = getIntent();
			Bundle data = new Bundle();
			data.putSerializable("attachList", (Serializable) attachList);
			intent.putExtras(data);
			this.setResult(0, intent);
			dThread = null;
			firstPlay = true;
			stopMediaPlayer();
			finish();
		} else if (id == R.id.fileUpload_newRecord) {
			stopMediaPlayer();
			Intent intent = new Intent();
			intent.setClass(this, CFFileRecordActivity.class);
			if (hasRecord) {
				intent.putExtra("recordType", "old");
				intent.putExtra("fileAbsPath",
						newRecordItem.getPath() + newRecordItem.getFilename()
								+ newRecordItem.getFiletype());
			} else {
				intent.putExtra("recordType", "new");
			}

			startActivityForResult(intent, 4);
			dThread = null;
			firstPlay = true;

		} else if (id == R.id.fileUpload_addRecordIcon) {
			attachList.remove(newRecordItem);
			hasRecord = false;
			showRecordTip();
			dThread = null;
			firstPlay = true;
			stopMediaPlayer();
		} else if (id == R.id.fileUpload_recordPlay) {
			playRecord();
		}

	}

	private void stopMediaPlayer() {
		if (mediaPlayer != null) {
			mediaPlayer.release();
		}

	}

	private void playRecord() {
		if (firstPlay) {
			mediaPlayer = new MediaPlayer();
			recordPlayIcon.setBackgroundResource(R.drawable.icon_pause);
			try {
				firstPlay = false;
				mediaPlayer.setDataSource(newRecordItem.getPath()
						+ newRecordItem.getFilename()
						+ newRecordItem.getFiletype());
				mediaPlayer.prepare();
				mediaPlayer.start();
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						mediaPlayer.release();
						recordPlayIcon
								.setBackgroundResource(R.drawable.icon_play);
						dThread = null;
						firstPlay = true;
						recordPlayInfoView.setText(newRecordItem.getTotalTs()
								+ "/" + newRecordItem.getTotalTs());
					}
				});
				startProgressUpdate();
			} catch (IOException e) {
				Log.e("recording error", "prepare() failed");
			}
		} else if (!firstPlay && !mediaPlayer.isPlaying()) {
			mediaPlayer.start();
			recordPlayIcon.setBackgroundResource(R.drawable.icon_pause);
		} else {
			mediaPlayer.pause();
			recordPlayIcon.setBackgroundResource(R.drawable.icon_play);
		}

	}

	public void startProgressUpdate() {
		// 开辟Thread 用于定期刷新SeekBar
		dThread = new DelayThread(100);
		dThread.start();
	}

	private Handler mHandle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (dThread != null) {
				int position = mediaPlayer.getCurrentPosition();

				int mMax = mediaPlayer.getDuration();
				int sMax = seekBar.getMax();
				int mm = (position / 1000) % 60;
				String mmString = Integer.toString(mm);
				if (mm < 10) {
					mmString = "0" + mm;
				}
				String tsString = "0" + (position / 1000) / 60 + ":" + mmString;
				recordPlayInfoView.setText(tsString + "/"
						+ newRecordItem.getTotalTs());
				seekBar.setProgress(position * sMax / mMax);
			} else {
				initRecordPlay();
			}
		}

	};

	public class DelayThread extends Thread {
		int milliseconds;

		public DelayThread(int i) {
			milliseconds = i;
		}

		public void run() {
			while (true) {
				try {
					sleep(milliseconds);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				mHandle.sendEmptyMessage(0);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Intent intent = getIntent();
		Bundle data = new Bundle();
		data.putSerializable("attachList", (Serializable) attachList);
		intent.putExtras(data);
		this.setResult(0, intent);
		dThread = null;
		firstPlay = true;
		stopMediaPlayer();
		finish();
		return super.onKeyDown(keyCode, event);
	}

	protected void initRecordPlay() {
		recordPlayInfoView.setText("00:00/" + newRecordItem.getTotalTs());
		seekBar.setProgress(0);
		recordPlayIcon.setBackgroundResource(R.drawable.icon_play);

	}

}
