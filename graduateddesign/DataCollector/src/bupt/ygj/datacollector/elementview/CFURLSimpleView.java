package bupt.ygj.datacollector.elementview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputType;
import bupt.ygj.datacollector.data.ElementDataVO;

@SuppressLint("ViewConstructor")
public class CFURLSimpleView extends CFEditSimpleView {
	
	public CFURLSimpleView(Context context, ElementDataVO viewAttribute) {
		super(context, viewAttribute);
		eView.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
	}
}
