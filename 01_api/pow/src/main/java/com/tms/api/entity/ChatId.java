package com.tms.api.entity;

import java.io.Serializable;

public class ChatId implements Serializable {
    private static final long serialVersionUID = -5688043643278441710L;

    private String channel;
    private String id;
    private String nickname;
    private String fullname;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

}