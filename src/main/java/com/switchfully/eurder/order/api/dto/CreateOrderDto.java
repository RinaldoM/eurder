package com.switchfully.eurder.order.api.dto;

import com.switchfully.eurder.order.domain.GroupItem;

import java.util.List;

public class CreateOrderDto {
    private final List<CreateGroupItemDto> itemGroup;
    private final String customerId;

    public CreateOrderDto(String customerId, List<CreateGroupItemDto> itemGroup) {
        this.itemGroup = itemGroup;
        this.customerId = customerId;
    }

    public List<CreateGroupItemDto> getItemGroup() {
        return itemGroup;
    }

    public String getCustomerId() {
        return customerId;
    }
}
