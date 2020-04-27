package com.magomed.application.api;

public interface IUserManager {
    UserBuilder getUserData(String bidy) throws DataNotFoundException;
}
