package bupt.ygj.datacollector.elementview;

import wa.android.libs.groupview.WARowStyle;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import bupt.ygj.datacollector.R;

public class NameValueView extends LinearLayout {
	
	private Context context;
	private TextView titleTextView;
	private TextView sizeTextView;
	private ImageView rightIconImageView;

	public NameValueView(Context context) {
		super(context);
		this.context = context;
		init();
	}
	
	private void init() {
		setOrientation(HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);
		setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		setMinimumHeight((int) (48 * getResources().getDisplayMetrics().density));
		WARowStyle rowStyle = new WARowStyle(context);
		setPadding(rowStyle.getRowPaddingLeft(),
					rowStyle.getRowPaddingTop(),
					rowStyle.getRowPaddingRight(),
					rowStyle.getRowPaddingBottom());
		//title
		titleTextView = new TextView(context);
		LayoutParams params = new LayoutParams((int) (80 * getResources().getDisplayMetrics().density), android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.weight = 0.7f;
		titleTextView.setLayoutParams(params);
//		titleTextView.setSingleLine();
		titleTextView.setMaxLines(2);
		titleTextView.setEllipsize(TruncateAt.END);
		titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		titleTextView.setTextColor(getResources().getColor(R.color.list_text_blue));
		titleTextView.setGravity(Gravity.RIGHT);
		
//		params = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params = new LayoutParams((int) (168 * getResources().getDisplayMetrics().density), android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.rightMargin = (int) (8 * getResources().getDisplayMetrics().density);
		params.leftMargin= (int) (8 * getResources().getDisplayMetrics().density);
//		params.leftMargin = rowStyle.getCommonPadding();
		params.weight = 1;
		sizeTextView = new TextView(context);
		sizeTextView.setGravity(Gravity.LEFT);
//		sizeTextView.setMaxLines(2);
		sizeTextView.setLayoutParams(params);
		sizeTextView.setTextColor(getResources().getColor(R.color.list_text_black));
//		sizeTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, rowStyle.getValueTextSize());
		sizeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		
		//righticon
		rightIconImageView = new ImageView(context);
		rightIconImageView.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		rightIconImageView.setBackgroundResource(R.drawable.detail_row_arrow);
		rightIconImageView.setVisibility(View.INVISIBLE);
		addView(titleTextView);
		addView(sizeTextView);
		addView(rightIconImageView);
	}
	
	@Override
	public void setVisibility(int mode) {
		if(mode == View.GONE) {
			rightIconImageView.setVisibility(View.GONE);
		} else if(mode == View.VISIBLE) {
			rightIconImageView.setVisibility(View.VISIBLE);
		}
	}
	
	public void setValue(String name, String value) {
		titleTextView.setText(name);
		sizeTextView.setText(value);
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
		void onClick(View v) ;
	}
	
	public int dpToPx(int dp){
		return (int) (dp*context.getResources().getDisplayMetrics().density);
	}
}
