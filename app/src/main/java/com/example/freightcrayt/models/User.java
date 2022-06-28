package com.example.freightcrayt.models;

public class User {

    private String userEmail;
    private String userName;
    private String userID;

    public User() {
    }

    public User(String userEmail, String userName, String userID) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userID = userID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return userEmail;
    }
}
