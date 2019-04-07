package com.github.julywind.auth.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
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
}
