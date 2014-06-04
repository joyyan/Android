package bupt.ygj.datacollector.formular;

import java.util.Set;

public interface IDataMap {
	public Set<String> getKeys();
	public Double get(String key);
}
