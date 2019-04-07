package com.github.julywind.auth.exception;


import org.springframework.http.HttpStatus;

public class AuthenticationFailedException extends RuntimeException {
    private HttpStatus httpStatus;
    private Object responseData;

    public AuthenticationFailedException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public AuthenticationFailedException() {
    }

    public AuthenticationFailedException(String responseMessage) {
        this.responseData = responseMessage;
    }

    public AuthenticationFailedException(HttpStatus httpStatus, Object responseData) {
        this.httpStatus = httpStatus;
        this.responseData = responseData;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Object getResponseData() {
        return responseData;
    }

    public void setResponseData(Object responseData) {
        this.responseData = responseData;
    }
}
