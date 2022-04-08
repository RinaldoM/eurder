package com.switchfully.eurder.order.domain;

import com.switchfully.eurder.item.domain.Item;

import java.time.LocalDate;
import java.util.Objects;

public class GroupItem {
    private final String itemId;
    private final int amount;
    private LocalDate shippingDate;
    private double pricePerGroup;

    public GroupItem(String itemId, int amount) {
        this.itemId = itemId;
        this.amount = amount;
        this.shippingDate = LocalDate.now();
        this.pricePerGroup = 0;
    }

    public String getItemId() {
        return itemId;
    }

    public int getAmount() {
        return amount;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(LocalDate calculatedShippingDate) {
        this.shippingDate = calculatedShippingDate;
    }

    public double getPricePerGroup() {
        return pricePerGroup;
    }

    public void setPricePerGroup(double pricePerGroup) {
        this.pricePerGroup = pricePerGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupItem groupItem = (GroupItem) o;
        return amount == groupItem.amount && Objects.equals(itemId, groupItem.itemId) && Objects.equals(shippingDate, groupItem.shippingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, amount, shippingDate);
    }


}
