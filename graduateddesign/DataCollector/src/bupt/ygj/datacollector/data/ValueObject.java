package bupt.ygj.datacollector.data;

import java.util.Map;

public class ValueObject {

	public static final String EMPTY = "";

	public String getMapStringValue(Map<String, String> map, String key,
			String defaultValue) {
		String ret = map.get(key);
		if (ret == null) {
			return defaultValue;
		} else {
			return ret;
		}
	}

	public String getMapStringValue(Map<String, String> map, String key) {
		return getMapStringValue(map, key, ValueObject.EMPTY);
	}
}
