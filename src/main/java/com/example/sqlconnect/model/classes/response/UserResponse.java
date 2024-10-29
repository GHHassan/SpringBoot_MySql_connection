package com.example.sqlconnect.model.classes.response;

import com.example.sqlconnect.model.classes.User;

public class UserResponse extends User {
    private String userId;
    private String encryptedPassword;

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
