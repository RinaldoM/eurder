package com.switchfully.eurder.order.domain;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Order {
    private final String orderId;
    private final List<GroupItem> itemGroup;
    private final String customerId;
    private double totalPrice;

    public Order(String customerId, List<GroupItem> itemGroup) {
        this.orderId = UUID.randomUUID().toString();
        this.itemGroup = itemGroup;
        this.customerId = customerId;
        this.totalPrice = 1;
    }

    public String getOrderId() {
        return orderId;
    }

    public List<GroupItem> getItemGroup() {
        return itemGroup;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Double.compare(order.totalPrice, totalPrice) == 0 && Objects.equals(itemGroup, order.itemGroup) && Objects.equals(customerId, order.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemGroup, customerId, totalPrice);
    }



}