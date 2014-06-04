package bupt.ygj.datacollector.view;

import java.io.File;

import wa.android.common.activity.BaseActivity;
import wa.android.libs.groupview.OnAttachmentOpenedActions;
import wa.android.uploadattachment.data.AttachmentShowVO;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import bupt.ygj.datacollector.R;

public class AttachmentListView extends LinearLayout {

	private Context context;
	private TextView titleTextView;
	private TextView sizeTextView;
	private ImageView rightIconImageView;

	public AttachmentListView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	private int dp2Px(int dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale);
	}

	private void init() {
		setOrientation(HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);
		setBackgroundResource(R.drawable.attactlist_item_bg);
		LayoutParams params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		params.leftMargin = dp2Px(8);
		setLayoutParams(params);
		// title
		titleTextView = new TextView(context);
		params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		params.gravity = Gravity.CENTER_VERTICAL;
		params.leftMargin = dp2Px(8);
		titleTextView.setLayoutParams(params);
		titleTextView.setSingleLine();
		titleTextView.setEllipsize(TruncateAt.END);
		titleTextView.setTextColor(getResources().getColor(
				R.color.list_text_black));
		titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		// titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
		// rowStyle.getValueTextSize());
		// titleTextView.setTextSize(R.dimen.attchmentsize);
		// size
		params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.leftMargin = dp2Px(8);
		params.rightMargin = dp2Px(8);
		params.gravity = Gravity.CENTER_VERTICAL;
		sizeTextView = new TextView(context);
		sizeTextView.setLayoutParams(params);
		sizeTextView.setTextColor(getResources().getColor(
				R.color.list_text_blue));
		sizeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		sizeTextView.setMaxLines(2);
		// lefticon
		// leftIconImageView = new ImageView(context);
		// leftIconImageView.setLayoutParams(new LayoutParams(
		// android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		// android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		// leftIconImageView.setBackgroundResource(R.drawable.attachmenticon);

		// righticon
		rightIconImageView = new ImageView(context);
		params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_VERTICAL;
		params.rightMargin = dp2Px(8);
		rightIconImageView.setLayoutParams(params);
		rightIconImageView.setBackgroundResource(R.drawable.detail_row_arrow);

		addView(titleTextView);
		addView(sizeTextView);
		// addView(leftIconImageView);
		addView(rightIconImageView);
	}

	public void setItemOnClickListener(final BaseActivity c,
			final AttachmentShowVO attachmentShowVO) throws Exception {
		titleTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!attachmentShowVO.getAttachmentID().equals("")) { //

				}

				String path = attachmentShowVO.getAttachmentPath();
				String filetype = attachmentShowVO.getAttachmentType().replace(
						".", "");
				// String filename = attachmentShowVO.getAttachmentName();
				File file = new File(path);

				try {
					c.startActivity(OnAttachmentOpenedActions
							.getAttachmentIntent(file, filetype));
				} catch (Exception e) {
					e.printStackTrace();
				}

				c.startActivity(OnAttachmentOpenedActions.getAttachmentIntent(
						file, filetype));
				// toastMsg(c.getString(R.string.noapptoopen));
			}
		});
	}

	public void setItemOnClickListener(final ItemOnClickListener l) {
		titleTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				l.onClick(v);
			}
		});
	}

	public void remove() {
		// removeView(leftIconImageView);
		removeView(rightIconImageView);
	}

	public TextView getTitleTextView() {
		return titleTextView;
	}

	public void setTitleTextView(TextView titleTextView) {
		this.titleTextView = titleTextView;
	}

	public TextView getSizeTextView() {
		return sizeTextView;
	}

	public void setSizeTextView(TextView sizeTextView) {
		this.sizeTextView = sizeTextView;
	}

	// public ImageView getLeftIconImageView() {
	// return leftIconImageView;
	// }
	//
	// public void setLeftIconImageView(ImageView leftIconImageView) {
	// this.leftIconImageView = leftIconImageView;
	// }

	public ImageView getRightIconImageView() {
		return rightIconImageView;
	}

	public void setRightIconImageView(ImageView rightIconImageView) {
		this.rightIconImageView = rightIconImageView;
	}

	public void setTitle(String title) {
		titleTextView.setText(title);
	}

	public void setSize(String size) {
		sizeTextView.setText(size);
	}

	public interface ItemOnClickListener {
		void onClick(View v);
	}

	public void addSeperator() {
		LayoutParams lp = (LayoutParams) this.getLayoutParams();
		lp.topMargin = dp2Px(-1);

	}
}
