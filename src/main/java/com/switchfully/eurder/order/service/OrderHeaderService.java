package com.switchfully.eurder.order.service;

import com.switchfully.eurder.customer.domain.Customer;
import com.switchfully.eurder.customer.exception.CustomerNotFoundException;
import com.switchfully.eurder.customer.service.CustomerService;
import com.switchfully.eurder.order.api.dto.CreateOrderHeaderDto;
import com.switchfully.eurder.order.domain.OrderHeader;
import com.switchfully.eurder.order.domain.OrderReportDto;
import com.switchfully.eurder.order.domain.OrderHeaderRepository;
import com.switchfully.eurder.order.exception.EmptyInputException;
import com.switchfully.eurder.order.exception.OrderHeaderNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderHeaderService {
    private final Logger serviceLogger = LoggerFactory.getLogger(OrderHeaderService.class);

    private final OrderHeaderRepository orderRepository;


    private final CustomerService customerService;

    public OrderHeaderService(OrderHeaderRepository orderRepository, CustomerService customerService) {
        this.orderRepository = orderRepository;

        this.customerService = customerService;
    }

    public OrderHeader saveNewOrder(CreateOrderHeaderDto createOrderDTO) {
        serviceLogger.info("Attempting to create new order");
        loggingError(createOrderDTO.getCustomerId().toString());
        OrderHeader order = new OrderHeader();
        Customer customer =  customerService.getCustomerById(createOrderDTO.getCustomerId());
        order.setCustomer(customer);
        orderRepository.save(order);
        serviceLogger.info("New order created.");
        return order;
    }

    private void loggingError(String input) {
        if (input.isEmpty()) {
            serviceLogger.error("customer" + " is empty!");
            throw new EmptyInputException("customer");
        }
    }

    public OrderHeader findById(Long id){
        if(orderRepository.existsByOrderHeaderId(id)){
            return orderRepository.findById(id).get();
        }
        serviceLogger.error("Order with id " + id+ " does not exist!");
        throw new OrderHeaderNotFoundException(id);
    }

    public OrderReportDto getOrderReport(Long customerId) {
       return new OrderReportDto();
    }



}
