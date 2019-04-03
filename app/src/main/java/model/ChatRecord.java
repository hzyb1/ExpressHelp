package model;

import java.sql.Date;
import java.sql.Timestamp;

public class ChatRecord {
	private int id;
	private int senderId;	//发送者id
	private int geterId;	//接收者id
	private String message;		//发送内容
	private Timestamp sendTime;	//发送时间
	private int conversationId;		//该聊天记录所属的对话id
	
	
	
	public Timestamp getSendTime() {
		return sendTime;
	}
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSenderId() {
		return senderId;
	}
	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}
	public int getGeterId() {
		return geterId;
	}
	public void setGeterId(int geterId) {
		this.geterId = geterId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getConversationId() {
		return conversationId;
	}
	public void setConversationId(int conversationId) {
		this.conversationId = conversationId;
	}
	
}
