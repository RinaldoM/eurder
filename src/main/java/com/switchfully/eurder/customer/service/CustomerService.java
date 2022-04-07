package com.switchfully.eurder.customer.service;

import com.switchfully.eurder.customer.api.dto.CreateCustomerDto;
import com.switchfully.eurder.customer.api.dto.CustomerDto;
import com.switchfully.eurder.customer.domain.Customer;
import com.switchfully.eurder.customer.domain.CustomerRepository;
import com.switchfully.eurder.customer.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class CustomerService {
    private final Logger serviceLogger = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public CustomerDto createNewCustomer(CreateCustomerDto createCustomerDto) {
        if(createCustomerDto.getFirstName().isEmpty()){
            serviceLogger.error("First name is missing!");
            throw new EmptyInputException("First name");
        }
        if(createCustomerDto.getLastName().isEmpty()){
            serviceLogger.error("last name is missing!");
            throw new EmptyInputException("Last name");
        }
        if(createCustomerDto.getEmail().isEmpty()){
            serviceLogger.error("Email is missing!");
            throw new EmptyInputException("Email");

        }
        if(!isEmailFormValid(createCustomerDto.getEmail())){
            serviceLogger.error("Email is invalid!");
            throw new EmailInvalidException();
        }
        if(createCustomerDto.getAddress().isEmpty()){
            serviceLogger.error("Address is missing!");
            throw new EmptyInputException("Address");
        }
        if(createCustomerDto.getPhoneNumber().isEmpty()){
            serviceLogger.error("Phone number is missing!");
            throw new EmptyInputException("Phone Number");

        }
        Customer customer = customerMapper.toCustomer(createCustomerDto);
        customerRepository.save(customer);
        return customerMapper.toCustomerDto(customer);
    }

    private boolean isEmailFormValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pat.matcher(email).matches();
    }

    public List<CustomerDto> getAllCustomers() {
        serviceLogger.info("All customers are shown.");
        return customerMapper.toCustomerDto(customerRepository.getAll());
    }

    public CustomerDto getCustomerById(String customerId) {
        serviceLogger.info("Customer with ID " + customerId+ " is shown." );
        return customerMapper.toCustomerDto(customerRepository.findById(customerId));
    }
}
