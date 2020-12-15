package com.orioninc.blogEducationProject.error;

import com.fasterxml.jackson.annotation.JsonView;
import com.orioninc.blogEducationProject.model.JsonView.ErrorView;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

public class ApiError {
    @JsonView({ErrorView.codeMessage.class})
    private HttpStatus status;
    @JsonView(ErrorView.codeMessage.class)
    private int subCode;
    @JsonView(ErrorView.codeMessage.class)
    private String message;
    @JsonView(ErrorView.codeMessage.class)
    private List<String> errors;

    public ApiError(HttpStatus status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }

    public ApiError(HttpStatus status, Error error, String message, String errorMessage){
        super();
        this.status = status;
        this.subCode = error.getSubCode();
        this.message = message;
        errors = Arrays.asList(errorMessage);
    }

    public ApiError(HttpStatus status, Error error, String message, List<String> errors) {
        this.status = status;
        this.subCode = error.getSubCode();
        this.message = message;
        this.errors = errors;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public int getSubCode() {
        return subCode;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "status=" + status +
                ", subCode=" + subCode +
                ", message='" + message + '\'' +
                ", errors=" + errors +
                '}';
    }
}
