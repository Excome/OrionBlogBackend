package com.orioninc.blogEducationProject.error;

public enum PostError implements Error {
    UNAUTHORIZED_USER(2001, "Unauthorized user"),
    POST_NOT_FOUND(2002, "Post with this ID is not found"),
    INVALID_SEARCH_ARGUMENT(2003, "Invalid search argument"),
    USER_IS_NOT_AUTHOR(2004, "Authorized user is not the author of the post");

    private int subCode;
    private String message;

    PostError(int subCode, String message) {
        this.subCode = subCode;
        this.message = message;
    }

    public int getSubCode() {
        return subCode;
    }

    public String getMessage() {
        return message;
    }
}
