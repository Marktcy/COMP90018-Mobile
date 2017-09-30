package com.shixun.android.childtracking;

/**
 * Created by markchunyong on 2017/9/30.
 */

public class UserDatabase {

    @com.google.gson.annotations.SerializedName("Id")
    private String id;                                      //Data ID
    @com.google.gson.annotations.SerializedName("Name")
    private String name;                                    //Person Name
    @com.google.gson.annotations.SerializedName("Email")
    private String userAccount;                             //User account (Use email as account)
    @com.google.gson.annotations.SerializedName("Password")
    private String userPassword;                            //User password
    @com.google.gson.annotations.SerializedName("Role")
    private boolean isParent;                               //Role of persons

    //Consturct data
    public UserDatabase(String userAccount, String userPassword, String name, boolean isParent) {
        this.userAccount=userAccount;
        this.userPassword=userPassword;
        this.name=name;
        this.isParent=isParent;
    }

    //Set of assessor and mutator
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
