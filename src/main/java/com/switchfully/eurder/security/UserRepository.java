package com.switchfully.eurder.security;

import com.switchfully.eurder.customer.domain.Customer;
import com.switchfully.eurder.security.exception.UnknownUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

import static com.switchfully.eurder.security.Role.*;


@Repository
public class UserRepository {
    private final Logger repositoryLogger = LoggerFactory.getLogger(UserRepository.class);

    private final Map<String, User> usersMap;

    public UserRepository() {
        this.usersMap = new HashMap<>();
        usersMap.put("Admin", new User("Admin", "pwd", ADMIN));
        usersMap.put("Customer", new User("Customer", "pwd", CUSTOMER));
    }


    public User getUser(String userName) {
        User foundUser = usersMap.get(userName);
        if (foundUser == null) {
            repositoryLogger.error("Unknown user " + userName);
            throw new UnknownUserException(userName);
        }
        return foundUser;
    }

    public void addNewCustomer(Customer customer) {
        usersMap.put(customer.getFirstName(), new User(customer.getFirstName(), "pwd", CUSTOMER));
        repositoryLogger.info("New customer account created with username: " + customer.getFirstName());
    }
    public void addNewAdmin(Customer customer) {
        usersMap.put(customer.getFirstName(), new User(customer.getFirstName(), "pwd", ADMIN));
        repositoryLogger.info("New admin account created with username: " + customer.getFirstName());

    }

}
