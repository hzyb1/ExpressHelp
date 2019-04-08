package model;

public class Conversation {
	private int id;
	private int user1;		//对话者1
	private int user2;		//对话者2
	private boolean isUser1Delete;		//对话者1是否删除对话
	private boolean isUser2Delete;		//对话者2是否删除对话
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUser1() {
		return user1;
	}
	public void setUser1(int user1) {
		this.user1 = user1;
	}
	public int getUser2() {
		return user2;
	}
	public void setUser2(int user2) {
		this.user2 = user2;
	}
	public boolean isUser1Delete() {
		return isUser1Delete;
	}
	public void setUser1Delete(boolean isUser1Delete) {
		this.isUser1Delete = isUser1Delete;
	}
	public boolean isUser2Delete() {
		return isUser2Delete;
	}
	public void setUser2Delete(boolean isUser2Delete) {
		this.isUser2Delete = isUser2Delete;
	}
	
	
}
