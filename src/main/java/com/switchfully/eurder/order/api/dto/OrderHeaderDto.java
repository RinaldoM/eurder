package com.switchfully.eurder.order.api.dto;

public class OrderHeaderDto {
    private final Long orderId;
    private final String customerId;
    private final double totalOrderPrice;

    public OrderHeaderDto(Long orderId, String customerId, double totalOrderPrice) {
        this.orderId = orderId;

        this.customerId = customerId;
        this.totalOrderPrice = totalOrderPrice;
    }

    public Long getOrderId() {
        return orderId;
    }



    public String getCustomerId() {
        return customerId;
    }

    public double getTotalOrderPrice() {
        return totalOrderPrice;
    }



}
