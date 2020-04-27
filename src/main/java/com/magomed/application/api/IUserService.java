package com.magomed.application.api;

import com.magomed.application.internal.ActivityTime;
import com.magomed.application.internal.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserService {
    User getUser(int id) throws SQLException;

    void syncUser(User user) throws SQLException;

    void addUserActivities(List<ActivityTime> activityTime, int id) throws SQLException;
}
