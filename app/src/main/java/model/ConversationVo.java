package model;

import java.io.Serializable;

public class ConversationVo extends Conversation implements Serializable {
    private String photo;
    private String name;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}