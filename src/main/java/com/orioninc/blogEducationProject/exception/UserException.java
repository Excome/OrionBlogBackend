package com.orioninc.blogEducationProject.exception;

import com.orioninc.blogEducationProject.error.UserError;

public class UserException extends Exception {
    private UserError error;
    private String message;

    public UserException(UserError error, String message) {
        this.error = error;
        this.message = message;
    }

    public UserException(UserError error) {
        this.error = error;
    }

    public UserError getError() {
        return error;
    }

    @Override
    public String getMessage() {
        if(message != null)
            return error.getMessage() + " | "+ this.message;
        else
            return error.getMessage();
    }
}
