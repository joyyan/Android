package bupt.ygj.datacollector.data;

import java.io.Serializable;
import java.util.Map;

public class AddressVO implements Serializable{

	/**
	* @Fields id : TODO(������id)
	*/


	private String id;

	/**
	* @Fields code : TODO(���������)
	*/


	private String code;

	/**
	* @Fields name : TODO(���������)
	*/


	private String name;

	private String sortLetters; // 显示数据拼音的首字母

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public void setId( String id ) {
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setCode( String code ) {
		this.code = code;
	}

	public String getCode(){
		return code;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setAttributes(Map mapvalue) {
		if(mapvalue != null){
			Map<String,String> map = mapvalue;
			this.id = map.get("id");
			this.code = map.get("code");
			this.name = map.get("name");
		}
	}
}
