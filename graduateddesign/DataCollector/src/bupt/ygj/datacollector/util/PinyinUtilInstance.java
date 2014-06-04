package bupt.ygj.datacollector.util;

import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;

public class PinyinUtilInstance {
	private static HanyuPinyinOutputFormat hanYuPinOutputFormat ;
	
	private PinyinUtilInstance(){}
	
	public static synchronized HanyuPinyinOutputFormat getInstance() {
		if(hanYuPinOutputFormat == null)
			hanYuPinOutputFormat = new HanyuPinyinOutputFormat();
		return hanYuPinOutputFormat;
	}
}
