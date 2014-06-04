package bupt.ygj.datacollector.formular;

import java.util.List;

public class Expression {
	String value = null;
	String formular = null;
	private String expression;
	
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getFormular() {
		return formular;
	}

	public void setFormular(String formular) {
		this.formular = formular;
	}
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Expression(String value, String formular) {
		this.value = value;
		this.formular = formular;
	}
	
	public Expression(String expression) throws Exception {
		this.expression = expression;
		String[] splited = expression.split("->");
		this.value = splited[0];
		this.formular = splited[1];
	}

	public String toJS(List<String> list){
		StringBuffer sb = new StringBuffer();
		
					sb.append(value);
					sb.append("=");
					sb.append(formular);
					sb.append(";\r\n");
		
		return sb.toString();
	}
	
	public String toJS4Result(List<String> list){
		StringBuffer sb = new StringBuffer();
		
					sb.append("window.formular.result(\"");
					sb.append(value);
					sb.append("\",");
					sb.append(formular);
					sb.append(");\r\n");
	
		return sb.toString();
	}
}
