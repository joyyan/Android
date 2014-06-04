package bupt.ygj.datacollector.elementview;

import android.annotation.SuppressLint;
import android.content.Context;
import bupt.ygj.datacollector.data.ElementDataVO;

@SuppressLint("ViewConstructor")
public class CFEmailSimpleView extends CFEditSimpleView {
	
	public CFEmailSimpleView(Context context, ElementDataVO viewAttribute) {
		super(context, viewAttribute);
//		eView.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
//		eView.setOnFocusChangeListener(new OnFocusChangeListener() { 
//
//			@Override 
//			public void onFocusChange(View v, boolean hasFocus) { 
//				EditText et = (EditText)v;
//				String text = et.getEditableText().toString();
//				String check = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
//			    Pattern regex = Pattern.compile(check);
//			    Matcher matcher = regex.matcher(text);
//			    Boolean isMatched = matcher.matches();
////			    et.setText(isMatched.toString());
//			} 
//		});
	}

	

}
