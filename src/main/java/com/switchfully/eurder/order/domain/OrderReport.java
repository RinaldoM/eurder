package com.switchfully.eurder.order.domain;

import com.switchfully.eurder.customer.domain.Customer;

import java.util.HashMap;
import java.util.List;

public class OrderReport {
    private Customer customer;
    private HashMap<Long, OrderDetail> orderDetails;
    private int totalPrice;

    public OrderReport(Customer customer, HashMap<Long, OrderDetail> orderDetails, int totalPrice) {
        this.customer = customer;
        this.orderDetails = orderDetails;
        this.totalPrice = totalPrice;
    }
}