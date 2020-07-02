package com.psguide.uttam.Models;

import androidx.annotation.Keep;

import java.sql.Timestamp;
@Keep
public class View {
    public Timestamp timestamp;
    public String userName;
    public String userId;

    public View() {
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
