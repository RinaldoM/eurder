package com.switchfully.eurder.item.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidNumberInputException extends RuntimeException {
    public InvalidNumberInputException(String extraInfo) {
        super("Invalid input" + extraInfo);
    }
}