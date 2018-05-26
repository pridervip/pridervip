package com.pridervip.entity;

public class User {
    private int id;
    private String phone_mobile;//用户手机号
    private String login_password;//用户登录密码

    public User(int id, String phone_mobile, String login_password ) {
        super();
        this.id = id;
        this.phone_mobile = phone_mobile;
        this.login_password = login_password;

    }

    public User(String phone_mobile, String login_password) {
        super();
        this.phone_mobile = phone_mobile;
        this.login_password = login_password;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone_mobile() {
        return phone_mobile;
    }

    public void setPhone_mobile(String phone_mobile) {
        this.phone_mobile = phone_mobile;
    }

    public String getLogin_password() {
        return login_password;
    }

    public void setLogin_password(String login_password) {
        this.login_password = login_password;
    }

}
