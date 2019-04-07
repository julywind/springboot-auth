package com.my.sample.controller;

import com.github.julywind.auth.exception.AuthenticationFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class MyControllerAdvice{
    @ExceptionHandler(value = AuthenticationFailedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public String unAuthorized(AuthenticationFailedException exception){
        return exception.toString();
    }
}
