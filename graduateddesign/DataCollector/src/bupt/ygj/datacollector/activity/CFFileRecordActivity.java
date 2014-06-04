package bupt.ygj.datacollector.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import wa.android.common.activity.BaseActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioEncoder;
import android.media.MediaRecorder.AudioSource;
import android.media.MediaRecorder.OutputFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import bupt.ygj.datacollector.R;

@SuppressLint("HandlerLeak")
public class CFFileRecordActivity extends BaseActivity implements
		OnClickListener {

	private TextView titleTextView;

	private Button startBtn, stopBtn, playBtn, reRecordingButton;
	private TextView timeTv, tipTv, remainTv;
	private String ts, remainTS;

	private boolean isRecord = false; // 判断是否正在录音
	private MediaRecorder mediaRecorder; // 录音控制器
	private MediaPlayer mediaPlayer;// 录音播放器
	private String filePath; // 存储文件夹路径
	private String fileName; // 文件名字
	private String fileAbsPath; // 存储文件路径

	private Timer mTimer; // 计时器
	private TimerTask mTimerTask; // 控制计时器的计时过程
	private Handler mHandler; // 对计时器计时过程进行处理
	private String formStr = ".m4a"; // 录音生成文件格式
	private DelayThread dThread;
	// 为实现暂停功能需要进行录音文件合成
	private int time = 0; // 时长
	private int reTs = 90;
	private ArrayList<String> audioSec; // 录音片段

	private String recordTime;
	private String fileSize = "0";
	private boolean firstPlay = true;
	private boolean recordOK = false;
	private String recordType = "new";
	private volatile boolean stopRequested;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cf_record);

		Intent intent = getIntent();
		recordType = intent.getStringExtra("recordType");

		initView();

		boolean isStoreOK = initFileFloder(); // 初始化文件存储位置
		if (isStoreOK) {
			initRecoder(); // 初始化录音控制器
			initTimer(); // 初始化计时器
		}

	}

	private void initView() {
		titleTextView = (TextView) findViewById(R.id.titlePanel__titleTextView);
		titleTextView.setText(R.string.recording);

		Button backButton = (Button) findViewById(R.id.titlePanel_leftBtn);
		backButton.setOnClickListener(this);

		reRecordingButton = (Button) findViewById(R.id.titlePanel__rightBtn);
		reRecordingButton.setText(R.string.reRecording);
		reRecordingButton.setOnClickListener(this);
		// 录音界面底部的三个按钮，初始只有录音按钮可点击
		startBtn = (Button) findViewById(R.id.start_Btn);
		startBtn.setOnClickListener(this);
		stopBtn = (Button) findViewById(R.id.stop_Btn);
		stopBtn.setOnClickListener(this);
		stopBtn.setClickable(false);
		playBtn = (Button) findViewById(R.id.play_Btn);
		playBtn.setOnClickListener(this);
		playBtn.setClickable(false);
		timeTv = (TextView) findViewById(R.id.record_time_Tv);
		tipTv = (TextView) findViewById(R.id.record_tip_Tv);
		remainTv = (TextView) findViewById(R.id.record_remainTime_Tv);

		audioSec = new ArrayList<String>();
		if (recordType.equals("new")) {
			reRecordingButton.setClickable(false);
		} else {
			reRecordingButton.setClickable(true);
			startBtn.setClickable(false);
			startBtn.setBackgroundResource(R.drawable.record_start_unclick);
			playBtn.setClickable(true);
			playBtn.setBackgroundResource(R.drawable.record_play);
			timeTv.setVisibility(View.VISIBLE);
			timeTv.setText("");
			Intent intent = getIntent();
			fileAbsPath = intent.getStringExtra("fileAbsPath");
		}
	}

	/** 初始化录音控制器，每新生成一个文件调用一次 */
	private String initRecoder() {
		timeTv.setVisibility(View.INVISIBLE);
		remainTv.setVisibility(View.INVISIBLE);
		tipTv.setVisibility(View.INVISIBLE);
		recordOK = false;
//		if (mediaRecorder == null) {
			mediaRecorder = new MediaRecorder();
//		} else {
//			mediaRecorder.reset();
//		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date time = new Date(System.currentTimeMillis());
		String timeStr = formatter.format(time); // 当前系统时间
		Random rd = new Random();
		int ranum = rd.nextInt(9000) + 1000; // 四位随机码
		String filename = timeStr + ranum; // 时间加四位随机码
		String filepath = filePath + "/" + filename + "_temp" + formStr;

		// 对录制的音频相关参数进行设置
		mediaRecorder.setAudioSamplingRate(8000); // 采样频率
		mediaRecorder.setAudioSource(AudioSource.MIC); // 音频来源
		mediaRecorder.setOutputFormat(OutputFormat.MPEG_4); // 音频输出格式
		mediaRecorder.setAudioEncoder(AudioEncoder.AAC); // 音频的编码格式
		mediaRecorder.setOutputFile(filepath); // 音频的输出文件路径

		return filepath;
	}

	/** 初始化计时器相关变量 */
	private void initTimer() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				int t = msg.what;
				ts = "0" + time / 60 + ":"
						+ ((time % 60 >= 10) ? (time % 60) : ("0" + time % 60));
				reTs = 90 - time;
				remainTS = "0" + reTs / 60 + ":"
						+ ((reTs % 60 >= 10) ? (reTs % 60) : ("0" + reTs % 60));
				if (t >= 0 && isRecord) {
					if (t == 0) {
						timeTv.setVisibility(View.VISIBLE);
					}
					tipTv.setText(getResources().getString(R.string.record_ing));
					timeTv.setText(ts);
					timeTv.setVisibility(View.VISIBLE);
					tipTv.setVisibility(View.VISIBLE);
					remainTv.setText("剩余" + remainTS);
					remainTv.setVisibility(View.VISIBLE);

				} else if (t == -1) {
					tipTv.setText(getResources().getString(
							R.string.record_pause));
					timeTv.setText(ts);
					remainTv.setText("剩余" + remainTS);
				} else {
					toastMsg(R.string.max_recordtime);
					stopRecorder();
				}
			}
		};
	}

	/** 结束录音时的一些操作 */
	private boolean finishRecord() {
		isRecord = false;
		stopBtn.setBackgroundResource(R.drawable.record_stop_unclick);
		stopBtn.setClickable(false);
		startBtn.setClickable(false);
		startBtn.setBackgroundResource(R.drawable.record_start_unclick);
		tipTv.setText("录音停止");

		time = 0;
		mTimer.cancel();

		if (null != mediaRecorder) {
			mediaRecorder.stop();
			mediaRecorder.release();
			mediaRecorder = null;
		}

		boolean isFile = pieceAudio(); // 拼凑文件，生成最终的录音文件
		return isFile;
	}

	/** 将List里面的零碎文件拼凑成最终的录音文件 */
	private boolean pieceAudio() {

		// 生成的录音文件存储位置
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date time = new Date(System.currentTimeMillis());
		String timeStr = formatter.format(time); // 当前系统时间
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		recordTime = formatter2.format(time);
		Random rd = new Random();
		int ranum = rd.nextInt(9000) + 1000; // 四位随机码
		fileName = timeStr + "-" + ranum; // 时间加四位随机码
		fileAbsPath = filePath + fileName + formStr;
		File file = new File(fileAbsPath);

		FileOutputStream fileOutputStream = null;

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		try {
			fileOutputStream = new FileOutputStream(file);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		// list里面为暂停录音 所产生的 几段录音文件的名字，中间几段文件的减去前面的6个字节头文件
		for (int i = 0; i < audioSec.size(); i++) {
			String secname = audioSec.get(i);
			File secfile = new File(secname);

			try {
				FileInputStream fileInputStream = new FileInputStream(secfile);
				byte[] myByte = new byte[fileInputStream.available()];
				// 文件长度
				int length = myByte.length;
				// 头文件
				if (i == 0) {
					while (fileInputStream.read(myByte) != -1) {
						fileOutputStream.write(myByte, 0, length);
					}
				}
				// 之后的文件，去掉头文件就可以了
				else {
					while (fileInputStream.read(myByte) != -1) {
						fileOutputStream.write(myByte, 6, length - 6);
						// fileOutputStream.write(myByte, 0, length);
					}
				}

				fileOutputStream.flush();
				fileInputStream.close();
				fileSize = file.length() / 1024 + "KB";
				recordOK = true;
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (secfile.exists()) {
				secfile.delete();
			}
		}

		// 结束后关闭流
		try {
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		// 合成一个文件后，删除之前暂停录音所保存的零碎合成文件
		// audioSec.clear();

		return true;
	}

	/** 初始化录音文件保存位置 */
	private boolean initFileFloder() {
		boolean hasSD = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		String SDState = Environment.getExternalStorageState();
		if(hasSD) {
			filePath = Environment.getExternalStorageDirectory() + "/myRecord/";
			File dir = new File(filePath);

			if (!dir.exists()) {
				dir.mkdirs();
			}
			return true;
		} else {
			toastMsg("内存卡不存在,不能录音");
			startBtn.setClickable(false);
			startBtn.setBackgroundResource(R.drawable.record_start_unclick);
			return false;
		}
		// if (SDState.equals(Environment.MEDIA_MOUNTED)) {
		//
		// filePath = Environment.getExternalStorageDirectory() + "/myRecord/";
		// File dir = new File(filePath);
		//
		// if (!dir.exists()) {
		// dir.mkdirs();
		// }
		// return true;
		//
		// } else {
		// toastMsg("内存卡不存在");
		// return false;
		// }
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.titlePanel_leftBtn) {
			backToUploadActivity();
		} else if (id == R.id.titlePanel__rightBtn) {
			reRecordingButton.setClickable(false);
			alertReRecorder();
		} else if (id == R.id.start_Btn) {
			startRecording();
			reRecordingButton.setClickable(false);
		} else if (id == R.id.stop_Btn) {
			stopRecorder();
		} else if (id == R.id.play_Btn) {
			startPlayRecord();

		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		dThread = null;
		firstPlay = true;
		stopMediaPlayer();
		backToUploadActivity();
		return super.onKeyDown(keyCode, event);
	}

	private void backToUploadActivity() {
		if(mediaRecorder != null) {
			mediaRecorder.release();
			mediaRecorder = null;
		}
		if (recordOK) {
			Intent intent = getIntent();
			Bundle data = new Bundle();
			data.putString("recordType", recordType);
			data.putString("filePath", filePath);
			data.putString("fileName", fileName);
			data.putString("recordTime", recordTime);
			data.putString("fileSize", fileSize);
			data.putString("totalTs", ts);
			data.putInt("time", time);
			intent.putExtras(data);
			this.setResult(Activity.RESULT_OK, intent);
			finish();
		} else if (isRecord) {
			cancelAlart();
		} else {
			finish();
		}
		firstPlay = true;
		stopMediaPlayer();
		stopRequest();
		dThread = null;

	}

	private void stopRecorder() {
		if (finishRecord()) {
			playBtn.setBackgroundResource(R.drawable.record_play);
			playBtn.setClickable(true);
			timeTv.setText("");
			remainTv.setVisibility(View.INVISIBLE);
			reRecordingButton.setClickable(true);
		} else {
			toastMsg("未生成录音文件");
		}

	}

	private void cancelAlart() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				CFFileRecordActivity.this);
		builder.setMessage("录音尚未结束，是否取消录音？");
		builder.setPositiveButton("确定放弃",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();

					}
				});
		builder.setNegativeButton("保存录音",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (finishRecord()) {
							playBtn.setClickable(true);
							reRecordingButton.setClickable(true);
						} else {
							toastMsg("未生成录音文件");
						}

					}
				});
		builder.setNeutralButton("取消", null);
		builder.create().show();

	}

	private void startPlayRecord() {
		if (firstPlay) {
			mediaPlayer = new MediaPlayer();
			playBtn.setBackgroundResource(R.drawable.record_pause);
			tipTv.setVisibility(View.VISIBLE);
			timeTv.setVisibility(View.VISIBLE);
			try {
				firstPlay = false;
				mediaPlayer.setDataSource(fileAbsPath);
				mediaPlayer.prepare();
				mediaPlayer.start();
				tipTv.setText("录音试听");
				remainTv.setVisibility(View.VISIBLE);
				int allts = mediaPlayer.getDuration() / 1000;
				String tsString = "0"
						+ allts
						/ 60
						+ ":"
						+ ((allts % 60 >= 10) ? (allts % 60)
								: ("0" + allts % 60));
				remainTv.setText("时长" + tsString);
				startProgressUpdate();
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						firstPlay = true;
						stopMediaPlayer();
						stopRequest();
						dThread = null;
						playBtn.setBackgroundResource(R.drawable.record_play);
						tipTv.setText("录音试听停止");
					}
				});
			} catch (IOException e) {
				Log.e("recording error", "prepare() failed");
			}
		} else if (!firstPlay && !mediaPlayer.isPlaying()) {
			mediaPlayer.start();
			tipTv.setText("录音试听");
			playBtn.setBackgroundResource(R.drawable.record_pause);
		} else {
			mediaPlayer.pause();
			tipTv.setText("录音试听暂停");
			playBtn.setBackgroundResource(R.drawable.record_play);
		}

	}

	private void stopMediaPlayer() {
		if (mediaPlayer != null) {

			stopRequest();
			dThread = null;
			firstPlay = true;
			mediaPlayer.release();
		}

	}

	private void alertReRecorder() {

		AlertDialog.Builder builder = new AlertDialog.Builder(
				CFFileRecordActivity.this);

		builder.setMessage(R.string.is_reRecord);
		builder.setPositiveButton(R.string.reRecording,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						stopMediaPlayer();
						startBtn.setBackgroundResource(R.drawable.record_start);
						startBtn.setClickable(true);
						stopBtn.setBackgroundResource(R.drawable.record_stop_unclick);
						stopBtn.setClickable(false);
						playBtn.setBackgroundResource(R.drawable.record_play_unclick);
						playBtn.setClickable(false);
						firstPlay = true;
						boolean isStoreOK = initFileFloder(); // 初始化文件存储位置
						if (isStoreOK) {
							initRecoder(); // 初始化录音控制器
							initTimer(); // 初始化计时器
						}
					}

				});
		builder.setNegativeButton(R.string.cancel, null);
		builder.create().show();

	}

	private void startRecording() {

		// 第一次录音的时候，创建计时器，停止键可点击
		if (time == 0) {
			mTimer = new Timer();
			mTimerTask = new TimerTask() {
				@Override
				public void run() {
					Message message = new Message();
					if (isRecord) {
						if (time < 90)
							message.what = time++;
						else {
							message.what = -2;
						}
					} else
						message.what = -1;
					mHandler.sendMessage(message);
				}
			};
			mTimer.schedule(mTimerTask, 0, 1000);
			stopBtn.setBackgroundResource(R.drawable.record_stop);
			stopBtn.setClickable(true);
			startBtn.setBackgroundResource(R.drawable.record_start_unclick);
			startBtn.setClickable(false);
		}
		// 点击后背景更改:录制按钮改为暂停按钮，暂停按钮改为录制按钮
		if (!isRecord) {
			// 表示之前没有正在录音，为开始按钮
			startBtn.setBackgroundResource(R.drawable.record_pause);
			isRecord = true;
			audioSec.clear();
			audioSec.add(initRecoder());
			try {
				mediaRecorder.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mediaRecorder.start();
		} else {
			// 表示之前正在录音，为暂停按钮
			startBtn.setBackgroundResource(R.drawable.record_start);
			isRecord = false;
			mediaRecorder.stop();
			mediaRecorder.release();
			mediaRecorder = null;
		}

		// 没有暂停功能时，交互发生变化
		startBtn.setClickable(false);
		startBtn.setBackgroundResource(R.drawable.record_start_unclick);

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
				int mm = (position / 1000) % 60;
				String mmString = Integer.toString(mm);
				if (mm < 10) {
					mmString = "0" + mm;
				}
				String tsString = "0" + (position / 1000) / 60 + ":" + mmString;
				timeTv.setText(tsString);

			} else {
				timeTv.setText("");
				remainTv.setVisibility(View.INVISIBLE);
			}
		}
	};

	public void stopRequest() {
		stopRequested = true;
		if (dThread != null) {
			dThread.interrupt();
		}
	}

	public class DelayThread extends Thread {
		int milliseconds;

		public DelayThread(int i) {
			milliseconds = i;
		}

		public void run() {
			stopRequested = false;
			while (!stopRequested) {
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
}
