package model;

import java.sql.Timestamp;

public class Conversation {
    private int id;
    private int userId1;
    private int userId2;
    private boolean isUser1Delete;
    private boolean isUser2Delete;
    private int user1UnRead;
    private int user2UnRead;
    private String lastMessage;
    private Timestamp lastTime;

    public int getUserId1() {
        return userId1;
    }

    public void setUserId1(int userId1) {
        this.userId1 = userId1;
    }

    public int getUserId2() {
        return userId2;
    }

    public void setUserId2(int userId2) {
        this.userId2 = userId2;
    }

    public int getUser1UnRead() {
        return user1UnRead;
    }
    public void setUser1UnRead(int user1UnRead) {
        this.user1UnRead = user1UnRead;
    }
    public int getUser2UnRead() {
        return user2UnRead;
    }
    public void setUser2UnRead(int user2UnRead) {
        this.user2UnRead = user2UnRead;
    }
    public String getLastMessage() {
        return lastMessage;
    }
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
    public Timestamp getLastTime() {
        return lastTime;
    }
    public void setLastTime(Timestamp lastTime) {
        this.lastTime = lastTime;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
