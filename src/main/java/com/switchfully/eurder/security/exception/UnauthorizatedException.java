package com.switchfully.eurder.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizatedException extends RuntimeException {
    public UnauthorizatedException(String userName, String feature) {
        super("User " + userName + " does not have access to " + feature);
    }
}
