package com.magomed.application.api;
import com.magomed.application.internal.models.User;

public class UserBuilder {
    private User user;

    public UserBuilder(){
        user = new User();
    }

    public UserBuilder id(int id) {
        user.setId(id);
        return this;
    }

    public UserBuilder money(long money) {
        user.setMoney(money);
        return this;
    }

    public UserBuilder country(String country) {
        user.setCountry(country);
        return this;
    }

    public User build(){
        return user;
    }
}
