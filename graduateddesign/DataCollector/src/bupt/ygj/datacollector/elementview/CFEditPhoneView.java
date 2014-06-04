package bupt.ygj.datacollector.elementview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputType;
import bupt.ygj.datacollector.data.ElementDataVO;

@SuppressLint("ViewConstructor")
public class CFEditPhoneView extends CFEditSimpleView {

	public CFEditPhoneView(Context context,ElementDataVO viewAttribute) {
		super(context, viewAttribute);
		if(!isEdit())
			eView.setEnabled(false);
		eView.setInputType(InputType.TYPE_CLASS_NUMBER);
		
	}

	
	
}
