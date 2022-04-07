package com.switchfully.eurder.customer.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CustomerRepository {
    private final Logger repositoryLogger = LoggerFactory.getLogger(CustomerRepository.class);
    private final Map<String, Customer> customersById;

    public CustomerRepository() {
        this.customersById = new HashMap<>();
    }

    public void save(Customer customer) {
        customersById.put(customer.getCustomerId(),customer);
        repositoryLogger.info("Customer has been saved with id: "+customer.getCustomerId());
    }
}
