package com.example.place.bottomsheetapp;

public class User {
    private String userID;
    private String displayName;
    private String photoUrl;
    private String email;
    private String isActive;

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public User(String userID, String displayName, String photoUrl,String email,String isActive) {
        this.userID = userID;
        this.displayName = displayName;
        this.photoUrl = photoUrl;
        this.email=email;
        this.isActive=isActive;
    }

    public User() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
