package bupt.ygj.datacollector.view;

import wa.android.libs.groupview.WARowStyle;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import bupt.ygj.datacollector.R;

public class AttachmentListView_new extends LinearLayout {

	private Context context;
	private TextView nameTextView;
	private TextView valueTextView;
	private ImageView rightIconImageView;

	public AttachmentListView_new(Context context) {
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
		setLayoutParams(new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, dp2Px(48)));
		WARowStyle rowStyle = new WARowStyle(context);
		setPadding(rowStyle.getRowPaddingLeft(), rowStyle.getRowPaddingTop(),
				0, rowStyle.getRowPaddingBottom());
		// title
		nameTextView = new TextView(context);
		LayoutParams params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		nameTextView.setLayoutParams(params);
		nameTextView.setSingleLine();
		nameTextView.setEllipsize(TruncateAt.END);
		nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

		// righticon
		rightIconImageView = new ImageView(context);
		params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.rightMargin = dp2Px(8);
		rightIconImageView.setLayoutParams(params);
		rightIconImageView
				.setBackgroundResource(R.drawable.cell_ic_signin_available);

		
		params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.leftMargin = dp2Px(8);
		params.rightMargin = dp2Px(8);
		params.weight = 1;
		valueTextView = new TextView(context);
		valueTextView.setLayoutParams(params);
		valueTextView.setTextColor(getResources().getColor(
				R.color.list_text_gray_new));
		valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		valueTextView.setMaxLines(2);
		valueTextView.setGravity(Gravity.RIGHT);
		valueTextView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						if (valueTextView.getLineCount() == 2) {
							valueTextView.setTextSize(12);
						} else if (valueTextView.getLineCount() >= 3) {
							valueTextView.setTextSize(10);
						}

						valueTextView.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});

		addView(nameTextView);
		addView(valueTextView);
		addView(rightIconImageView);
	}

	public void setRightImage(int id) {
		rightIconImageView.setBackgroundResource(id);

	}

	public void setValue(String name, String value) {
		nameTextView.setText(name);
		valueTextView.setText(value);
	}

	public void setName(String name) {
		nameTextView.setText(name);
	}

	public void setValue(String value) {
		valueTextView.setText(value);
	}

	public TextView getNameTextView() {
		return nameTextView;
	}

	public TextView getValueTextView() {
		return valueTextView;
	}

	public ImageView getRightIconImageView() {
		return rightIconImageView;
	}

	public void setRightIconImageView(ImageView rightIconImageView) {
		this.rightIconImageView = rightIconImageView;
	}

}
