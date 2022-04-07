package com.switchfully.eurder.order.domain;

import com.switchfully.eurder.item.domain.Item;

import java.time.LocalDate;
import java.util.Objects;

public class GroupItem {
    private final String itemId;
    private final int amount;
    private LocalDate shippingDate;

    public GroupItem(String itemId, int amount) {
        this.itemId = itemId;
        this.amount = amount;
        this.shippingDate = LocalDate.now();
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

    @Override
    public String toString() {
        return "GroupItem{" +
                "itemId='" + itemId + '\'' +
                ", amount=" + amount +
                ", shippingDate=" + shippingDate +
                '}';
    }
}
