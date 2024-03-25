package com.example.greenrover.data;

import java.util.List;

public class UserSubmission {

    private String UserID;
    private List<UploadData> data;

    public UserSubmission() {
    }

    public UserSubmission(String UserID, List<UploadData> data) {
        this.UserID = UserID;
        this.data = data;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public List<UploadData> getData() {
        return data;
    }

    public void setData(List<UploadData> data) {
        this.data = data;
    }
}
