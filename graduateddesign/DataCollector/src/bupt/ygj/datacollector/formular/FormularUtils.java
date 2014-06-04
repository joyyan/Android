package bupt.ygj.datacollector.formular;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.webkit.WebView;
import bupt.ygj.datacollector.elementview.AbsCommonFormView;
import bupt.ygj.datacollector.elementview.FieldValueCommon;

public class FormularUtils {
	
	private ArrayList<Expression> formulars = null;
	private List<AbsCommonFormView> listviews = null;
	private Context c;
	private Object object;		//用于公式计算完之后凋此object上的相应方法
	
	public FormularUtils(List<AbsCommonFormView> listviews) {
		this.formulars = new ArrayList<Expression>();
		this.listviews = listviews;
	}
	
	public FormularUtils(Context c,Object object) {
		this.c = c;
		this.object = object;
		this.formulars = new ArrayList<Expression>();
	}
	
	public void addExpression(Expression exp){
		for(Expression e : formulars){
			String expstr = e.getExpression();
			if(exp != null && expstr!= null && exp.equals(expstr))
				return;
		}
		formulars.add(exp);
	}

	@SuppressLint("SetJavaScriptEnabled")
	public void register2WebView(WebView wv) {
		wv.getSettings().setJavaScriptEnabled(true);
		wv.addJavascriptInterface(object, "formular");
	}
	
	
	public void end(){
		if (formulars.isEmpty())
			return ;
	}

	public String makeHtml(){
		if (formulars.isEmpty()) return null;
		StringBuffer sb = new StringBuffer();
		sb.append("<html><head><script>\r\n");
		
		sb.append("function calc() {\r\n");
		makeJS(sb);
		sb.append("}\r\n");
		sb.append("window.formular.end();\r\n");
		sb.append("</script></header><body onload=\"calc();\" /></html>");
		Log.d("CF_Formular_HtmlText", sb.toString());
		return sb.toString();
	}

	
	
	
	private void makeJS(StringBuffer sb) {
		//设置所有的变量
		List<String> list = new ArrayList<String>();
		for (AbsCommonFormView absv : listviews) {
			FieldValueCommon number = (FieldValueCommon) absv.getValue();
			if(null != number && number.getValue() != null && !number.getValue().equals("")) {
				sb.append("var ");
				sb.append(absv.getItemKey());
				sb.append("=");
				sb.append(number.getValue());
				sb.append(";\r\n");
				list.add(absv.getItemKey());
			} else if(null != number ) {
				sb.append("var ");
				sb.append(absv.getItemKey());
				sb.append(";\r\n");
			}
			
		}
		for (int i = 0; i < formulars.size() - 1; i++) {
			//为保证嵌套的公式可以正确计算
			//多个公式时，需要重复计算，N个公式计算N遍
			//由于还有一次最后计算，这里只添加N-1次
			for (Expression exp : formulars) {
				sb.append(exp.toJS(list));
			} 
		}
		//最后一次计算，结果会回调回去
		for (Expression exp : formulars) {
			sb.append(exp.toJS4Result(list));
		}
	}
	
	/**
	 * 开始计算
	 * @param listviews （需要进行计算的所有的view的集合）
	 */
	public void calc(List<AbsCommonFormView> listviews){
		this.listviews = listviews;
		WebView wb = new WebView(c);
		register2WebView(wb);
		String htmlText = makeHtml();
		if (htmlText != null) {
			wb.loadData(htmlText, "text/html", "utf-8");
		}
	}
}
