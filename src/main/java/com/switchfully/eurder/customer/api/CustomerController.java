package com.switchfully.eurder.customer.api;

import com.switchfully.eurder.customer.api.dto.CreateCustomerDto;
import com.switchfully.eurder.customer.api.dto.CustomerDto;
import com.switchfully.eurder.customer.domain.Customer;
import com.switchfully.eurder.customer.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto createNewCustomer(@RequestBody CreateCustomerDto createCustomerDto){
        return customerService.createNewCustomer(createCustomerDto);
    }
    @GetMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerDto> getAllCustomers(){
        return customerService.getAllCustomers();
    }
    @GetMapping(path="{customerId}",consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto getAllCustomers(@PathVariable String customerId){
        return customerService.getCustomerById(customerId);
    }



}
