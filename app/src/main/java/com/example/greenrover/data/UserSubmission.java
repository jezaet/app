package com.example.greenrover.data;

import java.util.List;

public class UserSubmission {

    private String UserID;
    private UploadData data;

    public UserSubmission() {
    }

    public UserSubmission(String UserID, UploadData data) {
        this.UserID = UserID;
        this.data = data;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public UploadData getData() {
        return data;
    }

    public void setData(UploadData data) {
        this.data = data;
    }
}
