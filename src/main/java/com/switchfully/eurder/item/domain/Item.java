package com.switchfully.eurder.item.domain;

import java.util.UUID;

public class Item {
    private final String itemId;
    private final String name;
    private final String description;
    private final double price;
    private final int amount;

    public Item(String name, String description, double price, int amount) {
        this.itemId = UUID.randomUUID().toString();
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
