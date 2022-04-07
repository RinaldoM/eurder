package com.switchfully.eurder.customer.api;

import com.switchfully.eurder.customer.api.dto.CreateCustomerDto;
import com.switchfully.eurder.customer.api.dto.CustomerDto;
import com.switchfully.eurder.customer.domain.Customer;
import com.switchfully.eurder.customer.service.CustomerService;
import com.switchfully.eurder.security.Feature;
import com.switchfully.eurder.security.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.switchfully.eurder.security.Feature.*;

@RestController
@RequestMapping("customers")
public class CustomerController {
    private final CustomerService customerService;
    private final SecurityService securityService;

    public CustomerController(CustomerService customerService, SecurityService securityService) {
        this.customerService = customerService;
        this.securityService = securityService;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto createNewCustomer(@RequestBody CreateCustomerDto createCustomerDto, @RequestHeader String authorization){
        securityService.validateAuthorization(authorization, CREATE_CUSTOMER);
        return customerService.createNewCustomer(createCustomerDto);
    }
    @GetMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerDto> getAllCustomers(@RequestHeader String authorization){
        securityService.validateAuthorization(authorization, VIEW_ALL_CUSTOMER);
        return customerService.getAllCustomers();
    }
    @GetMapping(path="{customerId}",consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto getAllCustomers(@PathVariable String customerId, @RequestHeader String authorization){
        securityService.validateAuthorization(authorization, VIEW_ONE_CUSTOMER);
        return customerService.getCustomerById(customerId);
    }



}
