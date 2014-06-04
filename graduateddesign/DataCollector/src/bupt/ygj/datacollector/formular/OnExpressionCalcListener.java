package bupt.ygj.datacollector.formular;

public interface OnExpressionCalcListener {
	public void onExpressionCalcResult(String key, double result);
	public void onCalcEnd();
}
