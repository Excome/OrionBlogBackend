package com.orioninc.blogEducationProject.error;

public enum UserError implements Error {
    USER_NOT_FOUND(1001, "User not found"),
    USER_NOT_AUTHORIZED(1002, "User is not authorized"),
    USER_ALREADY_IS_FOLLOW(1003, "You are already following this user"),
    USER_NOT_FOLLOW(1004, "Yoa are not following this user"),
    EMAIL_IS_ALREADY_USED(1005,"User with this email is already registered"),
    USERNAME_IS_ALREADY_USED(1006,"User with this username is already registered"),
    PASSWORDS_ARE_NOT_EQUAL(1007, "Password and password confirm do not match");

    private int subCode;
    private String message;

    UserError(int subCode, String message) {
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
