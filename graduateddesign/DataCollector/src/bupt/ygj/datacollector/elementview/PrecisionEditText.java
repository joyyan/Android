package bupt.ygj.datacollector.elementview;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * 控制decimal:数值 money：金额 percent：百分比 的显示精度
 * 
 * @author cuihd
 * 
 */
public class PrecisionEditText extends EditText {

	/**
	 * 0控制为整数，999不控制精度，其他的整数为几就代表控制到小数点后几位
	 */
	private int maxPrecision = -1;	

	public int getMaxPrecision() {
		return maxPrecision;
	}

	public void setMaxPrecision(int maxPrecision) {
		this.maxPrecision = maxPrecision;
	}

	public PrecisionEditText(Context context) {
		super(context);
		init();
	}

	public PrecisionEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
			setFilters(new InputFilter[] { inputFilter });
	}


	/**
	 * input输入过滤
	 */
	private InputFilter inputFilter = new InputFilter() {
		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			try {
				SpannableStringBuilder builder = new SpannableStringBuilder(
						dest).replace(dstart, dend,
						source.subSequence(start, end));
				String text = builder.toString();
				if(text != null && text.equals("."))
					return "";
				if (maxPrecision == 0) 
					setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
				else if(maxPrecision > 0 && maxPrecision != 999) {
					
					if (text != null && !text.equals("")) {
						int dotIndex = text.indexOf(".");
						if(dotIndex >= 0) {
							String precisionStr = text.substring(dotIndex + 1);
							if (precisionStr.length() > maxPrecision) { // 如果超出了精度
								source = source.subSequence(start, end-1);
							}
						} 
					}
				}
				if (text != null && (text.startsWith("00") || text.startsWith("-00"))) {
					source = source.subSequence(start, end-1);
				}
				return source;
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
	};
}
