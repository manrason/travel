package com.blog.travel.exception;

public class PostAlreadyExistException extends Exception {
    public PostAlreadyExistException(String message) {
        super(message);
    }
}
