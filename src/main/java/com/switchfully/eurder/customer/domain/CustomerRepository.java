package com.switchfully.eurder.customer.domain;

import com.switchfully.eurder.customer.exception.CustomerNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByCustomerId(Long id);
//    private final Logger repositoryLogger = LoggerFactory.getLogger(CustomerRepository.class);
//    private final Map<String, Customer> customersById;
//
//    public CustomerRepository() {
//        this.customersById = new HashMap<>();
//    }
//
//    public void save(Customer customer) {
//        customersById.put(customer.getCustomerId(),customer);
//        repositoryLogger.info("Customer has been saved with id: "+customer.getCustomerId());
//    }
//
//    public Collection<Customer> getAll() {
//        return customersById.values();
//    }
//
//    public Customer findById(String customerId) {
//        Customer foundCustomer = customersById.get(customerId);
//        if (foundCustomer == null) {
//            repositoryLogger.error("Customer with customer ID " + customerId + " not found");
//            throw new CustomerNotFoundException(customerId);
//        }
//        return foundCustomer;
//    }
//
//    public boolean checkIfCustomerExist(String customerId) {
//        return customersById.get(customerId) != null;
//    }
}
