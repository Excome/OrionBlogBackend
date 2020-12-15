package com.orioninc.blogEducationProject.exception;

import com.orioninc.blogEducationProject.error.PostError;

public class PostException extends Exception {
    private PostError error;
    private String message;

    public PostException(PostError error) {
        this.error = error;
    }

    public PostException(PostError error, String message) {
        this.error = error;
        this.message = message;
    }

    public PostError getError() {
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
