package bupt.ygj.datacollector.data;

import java.util.Map;

public class MessageVO extends ValueObject {

	/**
	* @Fields id : TODO(��Ϣid)
	*/


	private String id;

	/**
	* @Fields title : TODO(����)
	*/


	private String title;

	/**
	* @Fields sender : TODO(����������)
	*/


	private String sender;

	/**
	* @Fields receptor : TODO(�ռ�������)
	*/


	private String receptor;

	/**
	* @Fields isread : TODO(�Ƿ��Ѷ�)
	*/


	private String isread;

	/**
	* @Fields typeid : TODO(��Ϣ����id)
	*/


	private String typeid;

	/**
	* @Fields typename : TODO(��Ϣ�������)
	*/


	private String typename;

	/**
	* @Fields date : TODO(����ʱ��)
	*/


	private String date;


	public void setId( String id ) {
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setTitle( String title ) {
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setSender( String sender ) {
		this.sender = sender;
	}

	public String getSender(){
		return sender;
	}

	public void setReceptor( String receptor ) {
		this.receptor = receptor;
	}

	public String getReceptor(){
		return receptor;
	}

	public void setIsread( String isread ) {
		this.isread = isread;
	}

	public String getIsread(){
		return isread;
	}

	public void setTypeid( String typeid ) {
		this.typeid = typeid;
	}

	public String getTypeid(){
		return typeid;
	}

	public void setTypename( String typename ) {
		this.typename = typename;
	}

	public String getTypename(){
		return typename;
	}

	public void setDate( String date ) {
		this.date = date;
	}

	public String getDate(){
		return date;
	}
	
	public void setAttributes(Map mapvalue) {
		if(mapvalue != null){
			Map<String,String> map = mapvalue;
			this.id = map.get("id");
			this.title = map.get("title");
			this.sender = map.get("sender");
			this.receptor = map.get("receptor");
			this.isread = map.get("isread");
			this.typeid = map.get("typeid");
			this.typename = map.get("typename");
			this.date = map.get("date");
		}
	}

}
