package com.switchfully.eurder.order.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class OrderRepository {

    private final Logger repositoryLogger = LoggerFactory.getLogger(OrderRepository.class);
    private final Map<String, Order> ordersById;

    public OrderRepository() {
        this.ordersById = new HashMap<>();
    }

    public void save(Order order) {
        ordersById.put(order.getOrderId(), order);
        repositoryLogger.info("New order has been saved with order number: " + order.getOrderId()+".");
    }

    public Order findById(String orderId){
        return ordersById.get(orderId);
    }


}
