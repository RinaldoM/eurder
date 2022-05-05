package com.switchfully.eurder.order.api;


import com.switchfully.eurder.order.api.dto.CreateOrderHeaderDto;
import com.switchfully.eurder.order.api.dto.OrderHeaderDto;
import com.switchfully.eurder.order.domain.OrderReport;
import com.switchfully.eurder.order.service.OrderHeaderMapper;
import com.switchfully.eurder.order.service.OrderHeaderService;
import com.switchfully.eurder.security.SecurityService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.switchfully.eurder.security.Feature.NEW_ORDER;
import static com.switchfully.eurder.security.Feature.VIEW_REPORT_OF_CUSTOMER;

@RestController
@RequestMapping("orders")
public class OrderHeaderController {
    private final OrderHeaderService orderService;
    private final SecurityService securityService;
    private final OrderHeaderMapper orderHeaderMapper;


    public OrderHeaderController(OrderHeaderService orderService, SecurityService securityService, OrderHeaderMapper orderHeaderMapper) {
        this.orderService = orderService;
        this.securityService = securityService;
        this.orderHeaderMapper = orderHeaderMapper;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderHeaderDto newOrder(@RequestBody CreateOrderHeaderDto createOrderDTO, @RequestHeader String authorization) {
        securityService.validateAuthorization(authorization, NEW_ORDER);
        return orderHeaderMapper.toOrderDto(orderService.saveNewOrder(createOrderDTO)) ;
    }

    @GetMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public OrderReport getOrderReport(@RequestParam(name = "customer") Long customerId, @RequestHeader String authorization) {
        securityService.validateAuthorization(authorization, VIEW_REPORT_OF_CUSTOMER);
        return orderService.getOrderReport(customerId);
    }


}
