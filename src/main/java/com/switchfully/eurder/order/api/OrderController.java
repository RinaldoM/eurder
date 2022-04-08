package com.switchfully.eurder.order.api;


import com.switchfully.eurder.order.api.dto.CreateOrderDto;
import com.switchfully.eurder.order.domain.Order;
import com.switchfully.eurder.order.domain.OrderReport;
import com.switchfully.eurder.order.service.OrderService;
import com.switchfully.eurder.security.SecurityService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.switchfully.eurder.security.Feature.NEW_ORDER;
import static com.switchfully.eurder.security.Feature.VIEW_REPORT_OF_CUSTOMER;

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
    public Order newOrder(@RequestBody CreateOrderDto createOrderDTO, @RequestHeader String authorization) {
        securityService.validateAuthorization(authorization, NEW_ORDER);
        return orderService.saveNewOrder(createOrderDTO);
    }

    @GetMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public OrderReport getOrderReport(@RequestParam(name = "customer") String customerId, @RequestHeader String authorization) {
        securityService.validateAuthorization(authorization, VIEW_REPORT_OF_CUSTOMER);
        return orderService.getOrderReport(customerId);
    }


}
