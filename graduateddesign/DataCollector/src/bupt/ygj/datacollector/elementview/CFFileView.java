package bupt.ygj.datacollector.elementview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import wa.android.common.activity.BaseActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import bupt.ygj.datacollector.R;
import bupt.ygj.datacollector.activity.CFFileUploadActivity;
import bupt.ygj.datacollector.data.AbsFieldValue;
import bupt.ygj.datacollector.data.AttachmentVO;
import bupt.ygj.datacollector.data.ElementDataVO;
import bupt.ygj.datacollector.util.ReadUtil;

public class CFFileView extends AbsCommonFormView implements OnClickListener {

	private View rootView = null;
	private TextView numView = null;
	/** 照片列表 */
	private List<AttachmentVO> attachList;
	private String title;
	private String value = null;

	public CFFileView(Context context, ElementDataVO viewAttribute) {
		super(context, viewAttribute);
		attachList = new ArrayList<AttachmentVO>();
		rootView = this.inflate(context, R.layout.layout_cf_view_file, null);
		rootView.setOnClickListener(this);

		numView = (TextView) rootView.findViewById(R.id.cf_view_right);
		numView.setText("0");
		addView(rootView);

	}

	@Override
	public AbsFieldValue getValue() {
		if (null != attachList && attachList.size() > 0) {
			List<AttachmentVO> newAttachList = new ArrayList<AttachmentVO>();
			for (AttachmentVO attachment : attachList) {
				byte[] filecontent;
				String filestr;
				try {
					filecontent = Base64.encodeBase64(ReadUtil
							.writeToXml(attachment.getPath()
									+ attachment.getFilename()
									+ attachment.getFiletype()));
					filestr = new String(filecontent);

					AttachmentVO attachmentTemp = new AttachmentVO();
					attachmentTemp.setFilecontent(filestr);
					attachmentTemp.setFilename(attachment.getFilename()
							+ attachment.getFiletype());
					newAttachList.add(attachmentTemp);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			AbsFieldValue picFieldValueFile = new FieldValueFile(getItemKey(),
					newAttachList, value);
			return picFieldValueFile;
		} else if (attachList.size() == 0 && value != null) {
			List<AttachmentVO> newAttachList = new ArrayList<AttachmentVO>();
			AbsFieldValue picFieldValueFile = new FieldValueFile(getItemKey(),
					newAttachList, value);
			return picFieldValueFile;
		} else {
			return null; // 如果返回null，那么上传的时候连key都不会传
		}
	}

	@Override
	public void setDefaultValue(String defaultPk, String defaultString) {
		this.value = defaultString;
	}

	public void setSameValue(String defaultPk, String defaultString) {
		numView.setText(defaultString); // 把文件个数显示出来
	}

	public void setSameValue(View view) {
		CFFileView fileView = (CFFileView) view; // 把文件个数显示出来
		this.attachList = fileView.attachList;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
		TextView textView = (TextView) this.findViewById(R.id.cf_view_left);
		textView.setText(title);

	}

	@Override
	public void onClick(View v) {
		getFocus(rootView);
		if (isEdit()) { // 如果可以编辑那么才去跳转
			Intent i = new Intent(getContext(), CFFileUploadActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("attachList", (Serializable) attachList);
			i.putExtras(bundle);
			((BaseActivity) CFFileView.this.getContext())
					.startActivityForResult(i, CFFileView.this.getId());
		}

	}

	@Override
	@SuppressWarnings("unchecked")
	public void processResultIntent(Intent intent, int resultCode) {
		if (resultCode == 0) {
			attachList.clear();
			Bundle data = intent.getExtras();
			List<AttachmentVO> list = (List<AttachmentVO>) data
					.getSerializable("attachList");
			attachList = (List<AttachmentVO>) data
					.getSerializable("attachList");
			numView.setText("" + attachList.size());
			groupView.setView(this);
			groupView.setSamekeyValue(getItemKey(), "", "" + attachList.size());

		}
	}

}
