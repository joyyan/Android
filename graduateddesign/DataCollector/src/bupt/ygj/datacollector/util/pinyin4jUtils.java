package bupt.ygj.datacollector.util;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class pinyin4jUtils {

	/**
	 * 字符串集合转换字符串(逗号分隔)
	 * 
	 * @author wyh
	 * @param stringSet
	 * @return
	 */
	public static String makeStringByStringSet(Set<String> stringSet) {
		if (stringSet == null)
			return "";
		StringBuilder str = new StringBuilder();
		int i = 0;
		for (String s : stringSet) {
			if (s == null)
				continue;
			if (i == stringSet.size() - 1) {
				str.append(s);
			} else {
				str.append(s + ",");
			}
			i++;
		}
		return str.toString().toLowerCase();
	}

	/**
	 * 获取拼音集合
	 * 
	 * @author wyh
	 * @param src
	 * @return Set<String>
	 */
	public static Set<String> getPinyinSet(String src) {
		if (src != null && !src.trim().equalsIgnoreCase("")) {
			char[] srcChar;
			srcChar = src.toCharArray();
			// 汉语拼音格式输出类
			HanyuPinyinOutputFormat hanYuPinOutputFormat = PinyinUtilInstance
					.getInstance();
			;

			// 输出设置，大小写，音标方式等
			hanYuPinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
			hanYuPinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			hanYuPinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

			String[][] temp = new String[src.length()][];
			for (int i = 0; i < srcChar.length; i++) {
				char c = srcChar[i];
				// 是中文或者a-z或者A-Z转换拼音(我的需求，是保留中文或者a-z或者A-Z)
				if (String.valueOf(c).matches("[\\u4E00-\\u9FA5]+")) {
					try {
						temp[i] = PinyinHelper.toHanyuPinyinStringArray(
								srcChar[i], hanYuPinOutputFormat);
					} catch (BadHanyuPinyinOutputFormatCombination e) {
						e.printStackTrace();
					}
				} else if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122)) {
					temp[i] = new String[] { String.valueOf(srcChar[i]) };
				} else {
					temp[i] = new String[] { "" };
				}
			}
			String[] pingyinArray = Exchange(temp);
			Set<String> pinyinSet = new HashSet<String>();
			for (int i = 0; i < pingyinArray.length; i++) {
				pinyinSet.add(pingyinArray[i]);
			}
			return pinyinSet;
		}
		return null;
	}

	/**
	 * 递归
	 * 
	 * @author wyh
	 * @param strJaggedArray
	 * @return
	 */
	public static String[] Exchange(String[][] strJaggedArray) {
		String[][] temp = DoExchange(strJaggedArray);
		return temp[0];
	}

	/**
	 * 递归
	 * 
	 * @author wyh
	 * @param strJaggedArray
	 * @return
	 */
	private static String[][] DoExchange(String[][] strJaggedArray) {
		int len = strJaggedArray.length;
		if (len >= 2) {
			int len1 = strJaggedArray[0].length;
			int len2 = strJaggedArray[1].length;
			int newlen = len1 * len2;
			String[] temp = new String[newlen];
			int Index = 0;
			for (int i = 0; i < len1; i++) {
				for (int j = 0; j < len2; j++) {
					temp[Index] = strJaggedArray[0][i] + strJaggedArray[1][j];
					Index++;
				}
			}
			String[][] newArray = new String[len - 1][];
			for (int i = 2; i < len; i++) {
				newArray[i - 1] = strJaggedArray[i];
			}
			newArray[0] = temp;
			return DoExchange(newArray);
		} else {
			return strJaggedArray;
		}
	}

	public static String getPinyin(String str) {
		String pinyin = makeStringByStringSet(getPinyinSet(str));
		return pinyin;
	}

	@SuppressWarnings("deprecation")
	public static String hanziToPinyin(String hanzi, String separator) {
		// 创建汉语拼音处理类
		if(hanzi.contains("重庆")){
			return "chongqing";
		}
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		// 输出设置，大小写，音标方式
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

		String pinyingStr = "";
		try {
			pinyingStr = PinyinHelper.toHanyuPinyinString(hanzi, defaultFormat,
					separator);
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pinyingStr;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str = "单田芳";
		System.out.println(makeStringByStringSet(getPinyinSet(str)));

	}

}
