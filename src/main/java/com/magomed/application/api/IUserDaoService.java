package com.magomed.application.api;

import com.magomed.application.internal.models.ActivityTime;
import com.magomed.application.internal.models.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDaoService {
    User getUser(int id) throws SQLException;

    void syncUser(User user) throws SQLException;

    void addUserActivities(List<ActivityTime> activityTime, int id) throws SQLException;
}
