package com.switchfully.eurder.customer.service;

import com.switchfully.eurder.customer.api.dto.CreateCustomerDto;
import com.switchfully.eurder.customer.api.dto.CustomerDto;
import com.switchfully.eurder.customer.domain.Customer;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerMapper {
    public CustomerDto toCustomerDto(Customer customer) {
        return new CustomerDto(customer.getCustomerId(), customer.getFirstName(), customer.getLastName(), customer.getEmail(), customer.getPhoneNumber(), customer.getAddress());
    }

    public Customer toCustomer(CreateCustomerDto createCustomerDto) {
        return new Customer(createCustomerDto.getFirstName(),createCustomerDto.getLastName(),createCustomerDto.getEmail(),createCustomerDto.getPhoneNumber(),createCustomerDto.getAddress());
    }

    public List<CustomerDto> toCustomerDto(Collection<Customer> customerList) {
        return customerList.stream()
                .map(this::toCustomerDto)
                .collect(Collectors.toList());
    }


}
