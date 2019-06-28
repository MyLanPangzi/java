package com.xiebo.springboot.mvc.controller;

public class UserNotFindException extends RuntimeException {
    public UserNotFindException(String message) {
        super(message);
    }
}
