package com.switchfully.eurder.security;

public class User {
    private final String name;
    private final String password;
    private final Role role;

    public User(String name, String password, Role role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }


    public boolean canHaveAccessTo(Feature feature) {
        return role.containsFeature(feature);
    }

    public boolean doesPasswordMatch(String password) {
        return this.password.equals(password);
    }


}
