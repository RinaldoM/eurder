package com.switchfully.eurder.order.api;

import com.switchfully.eurder.order.domain.Order;
import com.switchfully.eurder.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Order newOrder(@RequestBody Order order){
        return orderService.saveNewOrder(order);
    }
}
