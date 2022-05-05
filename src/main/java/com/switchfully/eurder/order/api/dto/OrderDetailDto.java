package com.switchfully.eurder.order.api.dto;

import java.time.LocalDate;
import java.util.Objects;

public class OrderDetailDto {
    private final String itemName;
    private final int amount;
    private final double groupItemPrice;
    private final LocalDate shippingDate;

    public OrderDetailDto(String itemName, int amount, double groupItemPrice, LocalDate shippingDate) {
        this.itemName = itemName;
        this.amount = amount;
        this.groupItemPrice = groupItemPrice;
        this.shippingDate = shippingDate;
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

    public LocalDate getShippingDate() {
        return shippingDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDetailDto that = (OrderDetailDto) o;
        return amount == that.amount && Double.compare(that.groupItemPrice, groupItemPrice) == 0 && Objects.equals(itemName, that.itemName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemName, amount, groupItemPrice);
    }

  }
