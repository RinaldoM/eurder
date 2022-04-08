package com.switchfully.eurder.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnknownUserException extends RuntimeException {
    public UnknownUserException(String userName) {
        super("Unknown user" + userName);
    }
}
