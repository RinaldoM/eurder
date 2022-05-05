package com.switchfully.eurder.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderHeaderNotFoundException extends RuntimeException {
    public OrderHeaderNotFoundException(Long orderheader) {
        super("Order with order ID " + orderheader + " not found");
    }
}