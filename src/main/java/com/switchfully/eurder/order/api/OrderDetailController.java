package com.switchfully.eurder.order.api;

import com.switchfully.eurder.order.api.dto.CreateOrderDetailDto;
import com.switchfully.eurder.order.api.dto.OrderDetailDto;
import com.switchfully.eurder.order.domain.OrderHeader;
import com.switchfully.eurder.order.service.OrderDetailService;
import com.switchfully.eurder.order.service.OrderHeaderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order-details")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;
    private final OrderHeaderService orderHeaderService;

    public OrderDetailController(OrderDetailService orderDetailService, OrderHeaderService orderHeaderService) {
        this.orderDetailService = orderDetailService;
        this.orderHeaderService = orderHeaderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDetailDto saveOrderLine(@RequestBody CreateOrderDetailDto createOrderDetailDto) {
        OrderHeader orderHeader = orderHeaderService.findById(createOrderDetailDto.getOrderHeaderId());
        return orderDetailService.saveOrderDetail(createOrderDetailDto, orderHeader);
    }
}
