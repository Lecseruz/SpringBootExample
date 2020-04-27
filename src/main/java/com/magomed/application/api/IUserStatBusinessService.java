package com.magomed.application.api;

import com.magomed.application.internal.models.User;

public interface IUserStatBusinessService {
    User syncUserAndGet(int id) throws UpdateUserException;

    void updateUSer(User user) throws UpdateUserException;

    void updateStats(int id, int activity) throws UpdateUserException;
}
