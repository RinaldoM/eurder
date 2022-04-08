package com.switchfully.eurder.order.api.dto;

import com.switchfully.eurder.order.domain.GroupItem;

import java.util.List;
import java.util.Objects;

public class OrderDto {
    private final String orderId;
    private final List<GroupItemDto> itemGroup;
    private final String customerId;
    private final double totalOrderPrice;

    public OrderDto(String orderId, List<GroupItemDto> itemGroup, String customerId, double totalOrderPrice) {
        this.orderId = orderId;
        this.itemGroup = itemGroup;
        this.customerId = customerId;
        this.totalOrderPrice = totalOrderPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public List<GroupItemDto> getItemGroup() {
        return itemGroup;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getTotalOrderPrice() {
        return totalOrderPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDto orderDto = (OrderDto) o;
        return Double.compare(orderDto.totalOrderPrice, totalOrderPrice) == 0 && Objects.equals(itemGroup, orderDto.itemGroup) && Objects.equals(customerId, orderDto.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemGroup, customerId, totalOrderPrice);
    }


}
