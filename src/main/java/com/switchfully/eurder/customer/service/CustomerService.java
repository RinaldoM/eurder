package com.switchfully.eurder.customer.service;

import com.switchfully.eurder.customer.api.dto.CreateCustomerDto;
import com.switchfully.eurder.customer.api.dto.CustomerDto;
import com.switchfully.eurder.customer.domain.Customer;
import com.switchfully.eurder.customer.domain.CustomerRepository;
import com.switchfully.eurder.customer.exception.*;
import com.switchfully.eurder.security.SecurityService;
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
    private final SecurityService securityService;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, SecurityService securityService) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.securityService = securityService;
    }

    public CustomerDto createNewCustomer(CreateCustomerDto createCustomerDto) {
        loggingError(createCustomerDto.getFirstName(), "First name");
        loggingError(createCustomerDto.getLastName(), "Last name");
        loggingError(createCustomerDto.getAddress(), "Address");
        loggingError(createCustomerDto.getPhoneNumber(), "Phone number");
        if (!isEmailFormValid(createCustomerDto.getEmail())) {
            serviceLogger.error("Email is invalid!");
            throw new EmailInvalidException();
        }
        Customer customer = customerMapper.toCustomer(createCustomerDto);
        customerRepository.save(customer);
        securityService.newLogin(customer);
        return customerMapper.toCustomerDto(customer);
    }

    public List<CustomerDto> getAllCustomers() {
        serviceLogger.info("All customers are shown.");
        return customerMapper.toCustomerDto(customerRepository.getAll());
    }

    public CustomerDto getCustomerById(String customerId) {
        return customerMapper.toCustomerDto(customerRepository.findById(customerId));
    }

    private void loggingError(String input, String fieldName) {
        if (input.isEmpty()) {
            serviceLogger.error(fieldName + " is missing!");
            throw new EmptyInputException(fieldName);
        }
    }
    public boolean checkIfCustomerExists(String customerId){
        return customerRepository.checkIfCustomerExist(customerId);
    }
    //source: https://stackoverflow.com/questions/201323/how-can-i-validate-an-email-address-using-a-regular-expression
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

}
