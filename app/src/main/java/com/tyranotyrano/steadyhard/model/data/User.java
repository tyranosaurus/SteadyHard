package com.tyranotyrano.steadyhard.model.data;

/**
 * Created by cyj on 2017-11-28.
 */

public class User {
    private int no;
    private String email;
    private String token;
    private String profile_image;
    private String nickname;
    private String cookie;

    public User() { }

    public User(int no, String email, String token, String profile_image, String nickname) {
        this.no = no;
        this.email = email;
        this.token = token;
        this.profile_image = profile_image;
        this.nickname = nickname;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
