package com.switchfully.eurder.security;

import com.switchfully.eurder.customer.domain.Customer;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

import static com.switchfully.eurder.security.Role.*;


@Repository
public class UserRepository {

    private final Map<String, User> usersMap;

    public UserRepository() {
        this.usersMap = new HashMap<>();
        usersMap.put("Admin", new User("Admin", "pwd", ADMIN));
        usersMap.put("Customer", new User("Customer", "pwd", CUSTOMER));
    }


    public User getUser(String username) {
        User foundUser = usersMap.get(username);
        if (foundUser == null) {
            throw new IllegalStateException("no user found");
        }
        return foundUser;
    }

    public void addNewCustomer(Customer customer) {
        usersMap.put(customer.getFirstName(), new User(customer.getFirstName(), "pwd", CUSTOMER));
    }
    public void addNewAdmin(Customer customer) {
        usersMap.put(customer.getFirstName(), new User(customer.getFirstName(), "pwd", ADMIN));
    }

}
