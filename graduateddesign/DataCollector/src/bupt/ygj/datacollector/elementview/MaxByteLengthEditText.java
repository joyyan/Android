package bupt.ygj.datacollector.elementview;

import android.content.Context;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.EditText;

public class MaxByteLengthEditText extends EditText {  
    
  private int maxByteLength = -1;  
//  private  int len = 0;    
  private String encoding = "GBK";  

  public MaxByteLengthEditText(Context context) {  
      super(context);  
      init();  
  }  
    
  public MaxByteLengthEditText(Context context,AttributeSet attrs) {    
      super(context,attrs);  
      init();  
  }  
    
  private void init() {  
		  setFilters(new InputFilter[] {inputFilter});  
  }  
    
  public int getMaxByteLength() {  
      return maxByteLength;  
  }  

  public void setMaxByteLength(int maxByteLength) {  
      this.maxByteLength = maxByteLength;  
  }  

  public String getEncoding() {  
      return encoding;  
  }  

  public void setEncoding(String encoding) {  
      this.encoding = encoding;  
  }  

  /** 
   * input输入过滤 
   */  
  private InputFilter inputFilter = new InputFilter() {  
	  
	  /**
	   * @param source 将要输入的数据
	   * @param dest 已经存在的数据
	   */
      @Override  
      public CharSequence filter(CharSequence source, int start, int end,  
              Spanned dest, int dstart, int dend) {  
          try {  
        	  if(maxByteLength != -1) {		//如果需要控制长度
	        	  int len = 0;    
	              boolean more = false;  
	              SpannableStringBuilder builder = new SpannableStringBuilder(dest).replace(dstart, dend, source.subSequence(start, end));  
	              String text = builder.toString();
	              if(text != null && !text.equals("")) {
	            	  for(int i = 0 ;i<text.length();i++) {
	            		  String str = text.substring(i, i+1);
	            		  if(str.getBytes().length > str.length()) {	//全角字符？？？？？
	            			  len = len + 2;
	    	              } else {
	    	              	  len++;
	    	              }
	            	  }
	              }
	              more = len > maxByteLength;  
	              if (more) {  
	            	  return "";
	              }  
	              return source; 
        	  } else {
        		  return source;
        	  }
          } catch (Exception e) {  
        	  e.printStackTrace();
              return source;  
          }  
      }  
  };  
}
