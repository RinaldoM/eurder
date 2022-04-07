package com.switchfully.eurder.order.api;

import com.switchfully.eurder.order.domain.Order;
import com.switchfully.eurder.order.service.OrderService;
import com.switchfully.eurder.security.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.switchfully.eurder.security.Feature.NEW_ORDER;

@RestController
@RequestMapping("orders")
public class OrderController {
    private final OrderService orderService;
    private final SecurityService securityService;


    public OrderController(OrderService orderService, SecurityService securityService) {
        this.orderService = orderService;
        this.securityService = securityService;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Order newOrder(@RequestBody Order order, @RequestHeader String authorization){
        securityService.validateAuthorization(authorization, NEW_ORDER);
        return orderService.saveNewOrder(order);
    }
}
