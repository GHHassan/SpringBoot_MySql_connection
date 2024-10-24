package com.example.sqlconnect.model.response;

public class UserError extends Exception {

    private String errorCode;

    // Default constructor
    public UserError() {
        super();
    }

    // Constructor with a custom error message
    public UserError(String message) {
        super(message);
    }

    // Constructor with custom error message and cause
    public UserError(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor with custom error message, cause, and error code
    public UserError(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    // Constructor with custom error message and error code
    public UserError(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    // Get the error code
    public String getErrorCode() {
        return errorCode;
    }

    // Set the error code
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}

