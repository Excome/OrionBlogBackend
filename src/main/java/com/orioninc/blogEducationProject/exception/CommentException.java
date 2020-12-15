package com.orioninc.blogEducationProject.exception;

import com.orioninc.blogEducationProject.error.CommentError;

public class CommentException extends Exception {
    private CommentError error;
    private String message;

    public CommentException(CommentError error) {
        this.error = error;
    }

    public CommentException(CommentError error, String message) {
        this.error = error;
        this.message = message;
    }

    public CommentError getError() {
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
