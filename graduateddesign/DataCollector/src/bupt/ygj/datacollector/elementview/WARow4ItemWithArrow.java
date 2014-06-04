package bupt.ygj.datacollector.elementview;

import wa.android.libs.groupview.WARowStyle;
import wa.android.libs.row4itemview.WARow4Item;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import bupt.ygj.datacollector.R;

public class WARow4ItemWithArrow extends RelativeLayout {

	private Context context;
	private WARow4Item leftView;
	private ImageView rightView;
	
	public WARow4ItemWithArrow(Context context) {
		super(context);
		this.context = context;
		initView();
	}
	
	private void initView() {
		WARowStyle rowStyle = new WARowStyle(context);
		setPadding(rowStyle.getRowPaddingLeft(),
					rowStyle.getRowPaddingTop(),
					rowStyle.getRowPaddingRight(),
					rowStyle.getRowPaddingBottom());
		leftView = new WARow4Item(context);
		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams  
	            (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);    
	    lp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);    
	    lp1.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);    
	    // btn1 位于父 View 的顶部，在父 View 中水平居中    
	    addView(leftView, lp1 );   
	    
	    rightView = new ImageView(context);
	    rightView.setBackgroundResource(R.drawable.detail_row_arrow);
	    RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams  
	    		(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);    
	    lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);    
	    lp2.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);    
	    // btn1 位于父 View 的顶部，在父 View 中水平居中    
	    addView(rightView, lp2);   
	}
	
	public void setRightViewVisibility(int mode) {
		if(mode == View.GONE) {
			rightView.setVisibility(View.GONE);
		} else if(mode == View.VISIBLE) {
			rightView.setVisibility(View.VISIBLE);
		}
	}
	
	public WARow4Item getLeftView() {
		return leftView;
	}

	public void setLeftView(WARow4Item leftView) {
		this.leftView = leftView;
	}

	public ImageView getRightView() {
		return rightView;
	}

	public void setRightView(ImageView rightView) {
		this.rightView = rightView;
	}
}
