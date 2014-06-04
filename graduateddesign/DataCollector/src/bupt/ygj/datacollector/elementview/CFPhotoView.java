package bupt.ygj.datacollector.elementview;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import wa.android.common.activity.BaseActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import bupt.ygj.datacollector.R;
import bupt.ygj.datacollector.activity.CFPhotoDetailActivity;
import bupt.ygj.datacollector.data.AbsFieldValue;
import bupt.ygj.datacollector.data.AttachmentVO;
import bupt.ygj.datacollector.data.ElementDataVO;
import bupt.ygj.datacollector.util.ReadUtil;

@SuppressLint("ShowToast")
public class CFPhotoView extends AbsCommonFormView implements OnClickListener {

	private Context context;
	private String filePath;
	private String fileName;
	private String title;
	private View rootView = null;
	private ImageView iconView = null;
	private TextView textView = null;
	private boolean isHavePhoto = false;
	private List<AttachmentVO> newAttachList = null;
	private AttachmentVO newAttachItem = null;

	public CFPhotoView(Context context, ElementDataVO viewAttribute) {
		super(context, viewAttribute);
		this.context = context;
		// this.mode = mode;
		initView(context);
	}

	@SuppressWarnings("static-access")
	private void initView(Context context) {
		rootView = this.inflate(context, R.layout.layout_cf_view_file, null);
		textView = (TextView) rootView.findViewById(R.id.cf_view_left);
		iconView = (ImageView) rootView.findViewById(R.id.cf_view_right_icon);
		iconView.setBackgroundResource(R.drawable.icon_takephoto_selector);
		iconView.setVisibility(View.VISIBLE);
		this.addView(rootView);
		rootView.setOnClickListener(this);
		if (!isEdit()) {
			rootView.setOnClickListener(null);
		}
	}

	@Override
	public void processResultIntent(Intent data, int resultCode) {
		super.processResultIntent(data);
		if (resultCode == Activity.RESULT_OK) {

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
				newAttachItem = new AttachmentVO();
				newAttachItem.setFileid("");
				newAttachItem.setPath(filePath);
				newAttachItem.setFilename(fileName);
				newAttachItem.setFilesize(Long.toString(file.length() / 1024));
				iconView.setBackgroundResource(R.drawable.icon_hasphoto_selector);
				isHavePhoto = true;
				if (fileName.contains(".")) {
					String type = fileName.substring(fileName.lastIndexOf('.'));
					newAttachItem.setFiletype(type);
				}
				
				groupView.setView(this);
				groupView.setSamekeyValue(getItemKey(),"","");
			} catch (Exception e) {
				Toast.makeText(context, "存储照片失败！", Toast.LENGTH_SHORT).show();
			}

		} else if (resultCode == 1) {
			takeNewPhoto();
		} else if (resultCode == 2) {

		} else if (resultCode == 0) {

		}
	}

	@Override
	public void onClick(View v) {
		getFocus(rootView);
		if(isEdit()) {	//如果可以编辑那么才去跳转
			if (!isHavePhoto) {
				takeNewPhoto();
			} else {
				Intent intent = new Intent(context, CFPhotoDetailActivity.class);
				intent.putExtra("filepath", newAttachItem.getPath());
				intent.putExtra("filename", newAttachItem.getFilename());
				intent.putExtra("title", title);
	
				((BaseActivity) context).startActivityForResult(intent,
						CFPhotoView.this.getId());
	
			}
		}
	}

	private void takeNewPhoto() {
		// 执行拍照前判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {

			filePath = Environment.getExternalStorageDirectory() + "/myImage/";
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
			((BaseActivity) context).startActivityForResult(intent,
					CFPhotoView.this.getId());

		} else {
			Toast.makeText((CFPhotoView.this.getContext()), "内存卡不存在",
					Toast.LENGTH_SHORT).show();
		}

	}

	@SuppressLint("SimpleDateFormat")
	private String createFileName() {

		String fileName = "";
		Date date = new Date(System.currentTimeMillis()); // 系统当前时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		fileName = dateFormat.format(date);

		return fileName + ".jpg";
	}

	@Override
	public AbsFieldValue getValue() {
		byte[] filecontent;
		String filestr;
			try {
				filecontent = Base64.encodeBase64(ReadUtil.writeToXml(newAttachItem.getPath() + newAttachItem.getFilename() ));
				filestr = new String(filecontent);
				newAttachItem.setFilecontent(filestr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		newAttachList = new ArrayList<AttachmentVO>();
		newAttachList.add(newAttachItem);
		AbsFieldValue picFieldValueFile = new FieldValueFile(getItemKey(),
				newAttachList,null);
		return picFieldValueFile;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
		textView.setText(title);

	}

	@Override
	public void setDefaultValue(String defaultPk, String defaultString) {
		// TODO Auto-generated method stub

	}

	public void setSameValue(View view) {
		iconView.setBackgroundResource(R.drawable.icon_hasphoto_selector);
		CFPhotoView photoView = (CFPhotoView)view;
		isHavePhoto = true;
		this.newAttachItem = photoView.newAttachItem;
	}

}
