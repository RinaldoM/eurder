package com.switchfully.eurder.order.domain;

import com.switchfully.eurder.order.api.dto.OrderHeaderDto;

import java.util.List;
import java.util.Objects;

public class OrderReportDto {
    private List<OrderDetail> orderDetails;
    private int totalPrice;

}