package com.shixun.android.childtracking;

/**
 * Created by gongmengyu on 2017/9/29.
 */

public class UserDatabase {

    @com.google.gson.annotations.SerializedName("Id")
    private String id;
    @com.google.gson.annotations.SerializedName("Name")
    private String name;
    @com.google.gson.annotations.SerializedName("Email")
    private String userAccount;
    @com.google.gson.annotations.SerializedName("Password")
    private String userPassword;
    @com.google.gson.annotations.SerializedName("Role")
    private boolean isParent;


    public UserDatabase(String userAccount, String userPassword, String name, boolean isParent) {
        this.userAccount=userAccount;
        this.userPassword=userPassword;
        this.name=name;
        this.isParent=isParent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }
}
