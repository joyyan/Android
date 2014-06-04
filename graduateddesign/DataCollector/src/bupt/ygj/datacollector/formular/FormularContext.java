package bupt.ygj.datacollector.formular;

import java.util.ArrayList;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.webkit.WebView;
import bupt.ygj.datacollector.elementview.AbsCommonFormView;

public class FormularContext {
	
	private ArrayList<Expression> formulars = null;
	private IDataMap dataMap = null;
	
	private OnExpressionCalcListener calcListener = null;
	
	private FormularContext headFormularContext = null;
	

	public FormularContext(ArrayList<Expression> formulars, IDataMap dataMap) {
		this.formulars = formulars;
		this.dataMap = dataMap;
	}
	
	public FormularContext(IDataMap dataMap) {
		this.formulars = new ArrayList<Expression>();
		this.dataMap = dataMap;
	}
	
	public void setHeadFormularContext(FormularContext context) {
		headFormularContext = context;
	}
	
	public void addExpression(Expression exp){
		formulars.add(exp);
	}
	

	public void setCalcListener(OnExpressionCalcListener calcListener) {
		this.calcListener = calcListener;
	}


	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	public void register2WebView(WebView wv) {
		wv.getSettings().setJavaScriptEnabled(true);
		wv.addJavascriptInterface(this, "formular");
	}
	
	public void result(String key, double result){
		if (calcListener != null) {
			calcListener.onExpressionCalcResult(key, result);
		}
		Log.d(key, "" + result);
	}
	
	public void end(){
		if (calcListener != null) {
			calcListener.onCalcEnd();
		}
	}

	public String makeHtml(){
		if (formulars.isEmpty()) return null;
		StringBuffer sb = new StringBuffer();
		sb.append("<html><head><script>\r\n");
		
		if (headFormularContext != null) {
			headFormularContext.makeJS(sb);
		}
		sb.append("function calc() {\r\n");
		makeJS(sb);
		sb.append("}\r\n");
		sb.append("window.formular.end();\r\n");
		sb.append("</script></header><body onload=\"calc();\" /></html>");
		Log.d("CF_Formular_HtmlText", sb.toString());
		return sb.toString();
	}
	
	private void setSameKey() {
		
	}
	
	
	private void makeJS(StringBuffer sb){
		Set<String> keyset = dataMap.getKeys();
		//设置所有的变量
		for (String key : keyset) {
			sb.append("var ");
			sb.append(key);
			sb.append("=");
			sb.append(dataMap.get(key));
			sb.append(";\r\n");
		}
		//设置所有公式
		for (Expression exp : formulars) {
			//去除公式左项与变量重复的部分
			if (!keyset.contains(exp.value)) {
				sb.append("var ");
				sb.append(exp.value);
				sb.append("=0;\r\n");
			}
		}
		for (int i = 0; i < formulars.size() - 1; i++) {
			//为保证嵌套的公式可以正确计算
			//多个公式时，需要重复计算，N个公式计算N遍
			//由于还有一次最后计算，这里只添加N-1次
			for (Expression exp : formulars) {
//				sb.append(exp.toJS());
			}
		}
		//最后一次计算，结果会回调回去
		for (Expression exp : formulars) {
//			sb.append(exp.toJS4Result());
		}
	}
	
	private void makeJS2(StringBuffer sb){
		Set<String> keyset = dataMap.getKeys();
		//设置所有的变量
		for (String key : keyset) {
			sb.append("var ");
			sb.append(key);
			sb.append("=");
			sb.append(dataMap.get(key));
			sb.append(";\r\n");
		}
		//设置所有公式
		for (Expression exp : formulars) {
			//去除公式左项与变量重复的部分
			if (!keyset.contains(exp.value)) {
				sb.append("var ");
				sb.append(exp.value);
				sb.append("=0;\r\n");
			}
		}
		for (int i = 0; i < formulars.size() - 1; i++) {
			//为保证嵌套的公式可以正确计算
			//多个公式时，需要重复计算，N个公式计算N遍
			//由于还有一次最后计算，这里只添加N-1次
			for (Expression exp : formulars) {
//				sb.append(exp.toJS());
			}
		}
		//最后一次计算，结果会回调回去
		for (Expression exp : formulars) {
//			sb.append(exp.toJS4Result());
		}
	}
	
	
	public void calc(Context context){
		WebView wb = new WebView(context);
		register2WebView(wb);
		String htmlText = makeHtml();
		if (htmlText != null) {
			wb.loadData(htmlText, "text/html", "utf-8");
		}
	}
	
	public void calc(Context context,AbsCommonFormView view){
		WebView wb = new WebView(context);
		register2WebView(wb);
		String htmlText = makeHtml();
		if (htmlText != null) {
			wb.loadData(htmlText, "text/html", "utf-8");
		}
	}
}
