package com.orioninc.blogEducationProject.error;

public enum  TagError implements Error {
    TAG_NOT_FOUND(2201, "Tag with this name not found"),
    INVALID_SEARCH_ARGUMENT(2202, "Invalid tag name"),
    USER_IS_NOT_ADMIN(2203, "Current authorized user haven't role ADMIN");

    private int subCode;
    private String message;

    TagError(int subCode, String message) {
        this.subCode = subCode;
        this.message = message;
    }

    @Override
    public int getSubCode() {
        return this.subCode;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
