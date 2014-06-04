package bupt.ygj.datacollector.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessageGroupVO extends ValueObject {
	
	private String name;
	
	private List<MessageVO> message;
	public MessageGroupVO(){
		super();
	}
	public MessageGroupVO(String name,List<MessageVO> message){
		this.name = name;
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MessageVO> getMessage() {
		return message;
	}

	public void setMessage(List<MessageVO> message) {
		this.message = message;
	}
	
	public void setAttributes(Map mapvalue) {
		if(mapvalue != null){
			this.setName((String) mapvalue.get("name"));
			List<MessageVO> message = null;
			List<Map<String,String>> list = (List<Map<String, String>>) mapvalue.get("message");
			if(list != null){
				message = new ArrayList<MessageVO>();
				for(int i=0; i<list.size(); i++){
					MessageVO messagevo = new MessageVO();
					messagevo.setAttributes(list.get(i));
					message.add(messagevo);
				}
			}			
			this.setMessage(message);
		}
		
	}
}
