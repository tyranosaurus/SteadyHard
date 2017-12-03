package com.tyranotyrano.steadyhard.model.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cyj on 2017-11-28.
 */

public class User {
    @SerializedName("no")
    private int no;
    @SerializedName("email")
    private String email;
    @SerializedName("token")
    private String token;
    @SerializedName("profile_image")
    private String profileImage;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("cookie")
    private String cookie;

    public User() { }

    public User(int no, String email, String token, String profileImage, String nickname) {
        this.no = no;
        this.email = email;
        this.token = token;
        this.profileImage = profileImage;
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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
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
