package com.switchfully.eurder.order.api.dto;

import java.util.Objects;

public class GroupItemDto {
    private final String itemName;
    private final int amount;
    private final double groupItemPrice;

    public GroupItemDto(String itemName, int amount, double groupItemPrice) {
        this.itemName = itemName;
        this.amount = amount;
        this.groupItemPrice = groupItemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public int getAmount() {
        return amount;
    }

    public double getGroupItemPrice() {
        return groupItemPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupItemDto that = (GroupItemDto) o;
        return amount == that.amount && Double.compare(that.groupItemPrice, groupItemPrice) == 0 && Objects.equals(itemName, that.itemName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemName, amount, groupItemPrice);
    }

  }
