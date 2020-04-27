package com.magomed.springBootExample.api;

import com.magomed.springBootExample.internal.User;

public interface IUserStatBusinessService {
    User syncUserAndGet(int id) throws UpdateUserException;

    void updateUSer(User user) throws UpdateUserException;

    void updateStats(int id, int activity) throws UpdateUserException;
}
