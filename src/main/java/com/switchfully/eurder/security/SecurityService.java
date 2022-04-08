package com.switchfully.eurder.security;


import com.switchfully.eurder.customer.domain.Customer;
import com.switchfully.eurder.security.exception.UnauthorizatedException;
import com.switchfully.eurder.security.exception.UnknownUserException;
import com.switchfully.eurder.security.exception.WrongPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class SecurityService {
    private final Logger logger = LoggerFactory.getLogger(SecurityService.class);
    private final UserRepository userRepository;

    public SecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateAuthorization(String authorization, Feature feature) {
        UsernamePassword usernamePassword = getUsernamePassword(authorization);
        User user = userRepository.getUser(usernamePassword.getUsername());
        if(user == null) {
            logger.error("Unknown user" + usernamePassword.getUsername());
            throw new UnknownUserException(usernamePassword.getUsername());
        }
        if(!user.doesPasswordMatch(usernamePassword.getPassword())) {
            logger.error("Password does not match for user " + usernamePassword.getUsername());
            throw new WrongPasswordException(usernamePassword.getUsername());
        }
        if(!user.canHaveAccessTo(feature)) {
            logger.error("User " + usernamePassword.getUsername() + " does not have access to feature " + feature);
            throw new UnauthorizatedException(usernamePassword.getUsername(), feature.toString());
        }
        logger.info("Welcome " + usernamePassword.getUsername());
    }

    private UsernamePassword getUsernamePassword(String authorization) {
        String decodedUsernameAndPassword = new String(Base64.getDecoder().decode(authorization.substring("Basic ".length())));
        String username = decodedUsernameAndPassword.substring(0, decodedUsernameAndPassword.indexOf(":"));
        String password = decodedUsernameAndPassword.substring(decodedUsernameAndPassword.indexOf(":") + 1);
        return new UsernamePassword(username, password);
    }

    public void newLogin(Customer customer){
        userRepository.addNewCustomer(customer);
    }

}
