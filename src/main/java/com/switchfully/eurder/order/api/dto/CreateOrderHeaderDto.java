package com.switchfully.eurder.order.api.dto;

import java.util.List;

public class CreateOrderHeaderDto {
    private  Long customerId;

    public CreateOrderHeaderDto(Long customerId) {
        this.customerId = customerId;
    }

    public CreateOrderHeaderDto() {}

    public Long getCustomerId() {
        return customerId;
    }
}
