package bupt.ygj.datacollector.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import bupt.ygj.datacollector.R;

public class AttachmentView extends LinearLayout {

	private ImageView leftIconImageView;
	private AttachmentListView attachmentListView;
	Context context;

	public AttachmentView(Context context) {
		super(context);
		this.context = context;
		setOrientation(HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);
		leftIconImageView = new ImageView(context);
		leftIconImageView.setLayoutParams(new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		leftIconImageView.setBackgroundResource(R.drawable.addicon);
	}

	private int dp2Px(int dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale);
	}
	public AttachmentView(Context context, AttachmentListView alv) {
		super(context);
		this.context = context;
		setOrientation(HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);
		// setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.MATCH_PARENT));
		leftIconImageView = new ImageView(context);
		leftIconImageView.setLayoutParams(new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		leftIconImageView.setBackgroundResource(R.drawable.delicon);
		this.attachmentListView = alv;
		LayoutParams params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, dp2Px(48));
		params.leftMargin = dp2Px(8);
		setLayoutParams(params);
//		attachmentListView.setLayoutParams(params);
//		attachmentListView.setBackgroundResource(R.drawable.mailcontent);
		addView(leftIconImageView);
		addView(attachmentListView);
	}

	public ImageView getLeftIconImageView() {
		return leftIconImageView;
	}

	public void setLeftIconImageView(ImageView leftIconImageView) {
		this.leftIconImageView = leftIconImageView;
	}

	public void removeLeftIcon() {
		this.leftIconImageView.setVisibility(View.INVISIBLE);
	}

}
