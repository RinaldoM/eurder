package com.switchfully.eurder.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotFoundException extends RuntimeException {
    public NotFoundException(String customerId) {
        super("Customer with customer ID " + customerId + " not found");
    }
}