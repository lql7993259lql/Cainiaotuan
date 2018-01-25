package com.example.cniaotuan.entity;

/**
 * Created by hongkl on 16/9/1.
 */
public class UserEvent {

    String username;
    String pwd;

    public UserEvent(String username, String pwd) {
        this.username = username;
        this.pwd = pwd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
