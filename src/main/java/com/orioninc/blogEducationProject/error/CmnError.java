package com.orioninc.blogEducationProject.error;

public enum CmnError implements Error {
    ACCESS_DENIED(900, "User access is denied to this resource"),
    NOT_FOUND(904, "Not found handler for this mapping"),
    METHOD_ARGUMENTS_NOT_VALID(905, "Method arguments not valid"),
    HTTP_REQUEST_METHOD_NOT_SUPPORTED(910, "This HTTP method to this url not supported"),
    UNEXPECTED_SERVER_ERROR(990, "Unexpected server error");

    private int subCode;
    private String message;


    CmnError(int subCode, String message) {
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
