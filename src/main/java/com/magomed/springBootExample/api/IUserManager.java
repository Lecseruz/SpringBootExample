package com.magomed.springBootExample.api;

public interface IUserManager {
    UserBuilder getUserData(String bidy) throws DataNotFoundException;
}
