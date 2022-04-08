package com.switchfully.eurder.order.service;

import com.switchfully.eurder.customer.service.CustomerService;
import com.switchfully.eurder.order.api.dto.CreateOrderDto;
import com.switchfully.eurder.order.api.dto.OrderDto;
import com.switchfully.eurder.order.domain.Order;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {
    private final GroupItemMapper groupItemMapper;
    private final CustomerService customerService;

    public OrderMapper(GroupItemMapper groupItemMapper, CustomerService customerService) {
        this.groupItemMapper = groupItemMapper;
        this.customerService = customerService;
    }

    public Order toOrder(CreateOrderDto createOrderDTO) {
        return new Order(createOrderDTO.getCustomerId(), groupItemMapper.toGroupitem(createOrderDTO.getItemGroup()));
    }

    public OrderDto toOrderDto(Order order) {
        return new OrderDto(order.getOrderId(), groupItemMapper.toDto(order.getItemGroup()), customerService.getCustomerById(order.getCustomerId()).getFirstName(), order.getTotalPrice());
    }

    public List<OrderDto> toOrderDto(Collection<Order> orderList) {
        return orderList.stream()
                .map(this::toOrderDto)
                .collect(Collectors.toList());
    }

}
