package com.switchfully.eurder.order.domain;

import com.switchfully.eurder.order.api.dto.OrderDto;

import java.util.List;
import java.util.Objects;

public class OrderReport {
    private final List<OrderDto> orderList;
    private final double totalPrice;

    public OrderReport(List<OrderDto> orderList, double totalOrderPrice) {
        this.orderList = orderList;
        this.totalPrice = totalOrderPrice;
    }

    public List<OrderDto> getOrderList() {
        return orderList;
    }

    public double getTotalOrderPrice() {
        return totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderReport that = (OrderReport) o;
        return Double.compare(that.totalPrice, totalPrice) == 0 && Objects.equals(orderList, that.orderList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderList, totalPrice);
    }


}
