package com.magomed.springBootExample.api;

import com.magomed.springBootExample.internal.ActivityTime;
import com.magomed.springBootExample.internal.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDaoService {
    User getUser(int id) throws SQLException;

    void syncUser(User user) throws SQLException;

    void addUserActivities(List<ActivityTime> activityTime, int id) throws SQLException;
}
