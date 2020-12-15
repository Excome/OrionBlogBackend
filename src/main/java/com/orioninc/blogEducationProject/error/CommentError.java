package com.orioninc.blogEducationProject.error;

public enum CommentError implements Error {
    UNAUTHORIZED_USER(2101, "Unauthorized user"),
    POST_NOT_FOUND(2102, "Post with this ID is not found"),
    COMMENT_NOT_FOUND(2103, "Comment with this ID is not found"),
    USER_IN_NOT_AUTHOR(2104, "Current user not is author this comment");

    private int subCode;
    private String message;

    CommentError(int errorCode, String message) {
        this.subCode = errorCode;
        this.message = message;
    }

    public int getSubCode() {
        return subCode;
    }

    public void setSubCode(int subCode) {
        this.subCode = subCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
