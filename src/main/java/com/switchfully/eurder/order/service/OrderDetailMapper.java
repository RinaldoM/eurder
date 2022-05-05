package com.switchfully.eurder.order.service;

import com.switchfully.eurder.item.domain.Item;
import com.switchfully.eurder.item.service.ItemService;
import com.switchfully.eurder.order.api.dto.OrderDetailDto;
import com.switchfully.eurder.order.domain.OrderDetail;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDetailMapper {
    private final ItemService itemService;

    public OrderDetailMapper(ItemService itemService) {
        this.itemService = itemService;
    }


//    public OrderDetail toOrderDetail(CreateOrderDetailDto createOrderDetailDto) {
//        return new OrderDetail(createOrderDetailDto.getItemId(), createOrderDetailDto.getAmount());
//    }

//    public List<OrderDetail> toOrderDetails(List<CreateOrderDetailDto> createGroupItemDtoList) {
//        return createGroupItemDtoList
//                .stream()
//                .map(this::toOrderDetail)
//                .collect(Collectors.toList());
//    }

    public OrderDetailDto toDto(OrderDetail orderDetail){
        Item item = itemService.findItemById(orderDetail.getItem().getItemId());
        return new OrderDetailDto(item.getName(), orderDetail.getAmount(), (orderDetail.getAmount())*item.getPrice(), orderDetail.getShippingDate());
    }

    public List<OrderDetailDto> toDto(List<OrderDetail> orderDetails){
        return orderDetails
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
