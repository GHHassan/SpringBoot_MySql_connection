package com.example.sqlconnect.model.classes.requests;

import com.example.sqlconnect.model.classes.User;

public class UserRequest extends User {

    private String userId = null;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
