package com.switchfully.eurder.item.api.dto;

public class ItemDto {
    private final String itemId;
    private final String name;
    private final String description;
    private final double price;
    private final int amount;

    public ItemDto(String itemId, String name, String description, double price, int amount) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
    }

    public String getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }
}
