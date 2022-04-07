package com.switchfully.eurder.security;

import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Repository;

import java.util.Map;

import static com.switchfully.eurder.security.Role.*;


@Repository
public class UserRepository {
    private final Map<String, User> userMap = ImmutableMap.<String, User>builder()
            .put("Admin", new User("Admin", "pwd", ADMIN))
            .put("Customer", new User("Customer", "pwd", CUSTOMER))
            .build();


    public User getUser(String username) {
        return userMap.get(username);
    }

}
