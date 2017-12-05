package network.optimization.sdn.bean;

import java.io.Serializable;


public class Message implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3755660275715053348L;
	
	public String destination,source,message,tempfrom;
	public int serial;
	public int type;
	
	public static int count=0;
	public static final int MSG_PLAIN=0;
	public static final int MSG_ROUTE_1=1;
	public static final int MSG_ROUTE_2=2;
	public Message(String from,String to,String payload){
		setType(MSG_PLAIN);
		count=count+1;
		serial=count;
		destination=to;
		source=from;
		tempfrom=from;
		message=payload;
		type=MSG_PLAIN;
	}
	public void setType(int a){
		type=a;
	}
	public String toString(){
		return "Message Type:"+type+"Msg:"+serial+" From:"+source+" To:"+destination+"Payload:"+message;
	}

}
