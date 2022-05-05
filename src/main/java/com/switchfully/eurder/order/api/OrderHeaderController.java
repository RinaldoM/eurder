package com.switchfully.eurder.order.api;


import com.switchfully.eurder.order.api.dto.CreateOrderHeaderDto;
import com.switchfully.eurder.order.domain.OrderHeader;
import com.switchfully.eurder.order.domain.OrderReportDto;
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


    public OrderHeaderController(OrderHeaderService orderService, SecurityService securityService) {
        this.orderService = orderService;
        this.securityService = securityService;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderHeader newOrder(@RequestBody CreateOrderHeaderDto createOrderDTO, @RequestHeader String authorization) {
        securityService.validateAuthorization(authorization, NEW_ORDER);
        return orderService.saveNewOrder(createOrderDTO);
    }

    @GetMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public OrderReportDto getOrderReport(@RequestParam(name = "customer") Long customerId, @RequestHeader String authorization) {
        securityService.validateAuthorization(authorization, VIEW_REPORT_OF_CUSTOMER);
        return orderService.getOrderReport(customerId);
    }


}
