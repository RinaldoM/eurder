package com.switchfully.eurder.order.api.dto;

public class CreateGroupItemDto {
    private final String itemId;
    private final int amount;

    public CreateGroupItemDto(String itemId, int amount) {
        this.itemId = itemId;
        this.amount = amount;
    }

    public String getItemId() {
        return itemId;
    }

    public int getAmount() {
        return amount;
    }
}
