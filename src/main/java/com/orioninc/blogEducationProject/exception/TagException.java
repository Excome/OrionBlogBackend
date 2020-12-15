package com.orioninc.blogEducationProject.exception;

import com.orioninc.blogEducationProject.error.TagError;

public class TagException extends Exception {
    private TagError error;
    private String message;

    public TagException(TagError error) {
        this.error = error;
    }

    public TagException(TagError error, String message) {
        this.error = error;
        this.message = message;
    }

    public TagError getError() {
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
