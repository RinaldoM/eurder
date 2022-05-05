package com.switchfully.eurder.order.service;

import com.switchfully.eurder.customer.service.CustomerService;
import com.switchfully.eurder.order.api.dto.CreateOrderDetailDto;
import com.switchfully.eurder.order.api.dto.OrderHeaderDto;
import com.switchfully.eurder.order.domain.OrderHeader;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderHeaderMapper {
    private final CustomerService customerService;

    public OrderHeaderMapper( CustomerService customerService) {
        this.customerService = customerService;
    }



    public OrderHeaderDto toOrderDto(OrderHeader order) {
        return new OrderHeaderDto(order.getOrderHeaderId(), customerService.getCustomerById(order.getCustomer().getCustomerId()).getFirstName(), order.getTotalPrice());
    }

    public List<OrderHeaderDto> toOrderDto(Collection<OrderHeader> orderList) {
        return orderList.stream()
                .map(this::toOrderDto)
                .collect(Collectors.toList());
    }


}
