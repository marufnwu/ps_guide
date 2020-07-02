package com.psguide.uttam.Models;

import androidx.annotation.Keep;

import java.sql.Timestamp;
@Keep
public class Like {
    public String userId, userName;
    public Timestamp timestamp;

    public Like() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}




