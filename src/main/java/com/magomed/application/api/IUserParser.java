package com.magomed.application.api;

public interface IUserParser {
    UserBuilder getUserData(String bidy) throws DataNotFoundException;
}
