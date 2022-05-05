package com.switchfully.eurder.order.api.dto;

public class CreateOrderDetailDto {
    private final Long itemId;
    private final int amount;
    private final Long orderHeaderId;

    public CreateOrderDetailDto(Long itemId, int amount, Long orderHeader) {
        this.itemId = itemId;
        this.amount = amount;
        this.orderHeaderId = orderHeader;
    }

    public Long getOrderHeaderId() {
        return orderHeaderId;
    }

    public Long getItemId() {
        return itemId;
    }

    public int getAmount() {
        return amount;
    }
}
