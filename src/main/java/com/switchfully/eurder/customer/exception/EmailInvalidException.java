package com.switchfully.eurder.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailInvalidException extends RuntimeException {
    public EmailInvalidException() {
        super("Email is invalid!");
    }
}