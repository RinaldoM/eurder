package com.switchfully.eurder.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizatedException extends RuntimeException {
    public UnauthorizatedException() {
        super("You are not authorized to reach this endpoint!");
    }
}
