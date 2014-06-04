package bupt.ygj.datacollector.elementview;

import wa.android.libs.groupview.WARowStyle;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import bupt.ygj.datacollector.R;

public class ComboView extends RelativeLayout {

	private Context context;
	private TextView nameTextView;
	private ImageView rightView;

	public ImageView getRightView() {
		return rightView;
	}

	public void setRightView(ImageView rightView) {
		this.rightView = rightView;
	}

	public ComboView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	private void init() {
		WARowStyle rowStyle = new WARowStyle(this.context);
		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		lp1.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		setPadding(rowStyle.getRowPaddingLeft(), rowStyle.getRowPaddingTop(),
				rowStyle.getRowPaddingRight(), rowStyle.getRowPaddingBottom());

		nameTextView = new TextView(context);
		nameTextView.setPadding(rowStyle.getNamePaddingLeft(), 0,
				rowStyle.getNamePaddingRight(), 0);
		nameTextView.setSingleLine();
		nameTextView.setEllipsize(TruncateAt.END);
		nameTextView.setTextColor(rowStyle.getColorgray());
		addView(nameTextView, lp1);

		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp2.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		rightView = new ImageView(context);
		rightView.setBackgroundResource(R.drawable.listline_selected);
		rightView.setVisibility(View.GONE);
		lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp2.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		// btn1 位于父 View 的顶部，在父 View 中水平居中

		addView(rightView, lp2);
	}

	public void setValue(String value) {
		nameTextView.setText(value);
	}

}
