package com.magomed.application.internal;

import com.magomed.application.api.IUserDaoService;
import com.magomed.application.api.IUserStatBusinessService;
import com.magomed.application.api.UpdateUserException;
import com.magomed.application.internal.models.ActivityTime;
import com.magomed.application.internal.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserStatBusinessService implements IUserStatBusinessService {
    public static final int COUNT_STATS_PER_DAY = 10000;

    public static final String COUNTRY_IS_WRONG = "country is wrong for user";

    private final ConcurrentHashMap<Integer, User> userMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, List<ActivityTime>> activityMap = new ConcurrentHashMap<>();

    @Autowired
    private IUserDaoService daoService;

    @Override
    public User syncUserAndGet(int id) {
        try {
            User user = userMap.get(id);
            if (user != null) {
                daoService.syncUser(userMap.get(id));
                userMap.remove(id);
            }
            List<ActivityTime> listActivity = activityMap.get(id);
            if (listActivity != null && listActivity.size() > 0) {
                daoService.addUserActivities(listActivity, id);
                activityMap.get(id).clear();
            }
            return daoService.getUser(id);
        } catch (SQLException e) {
            throw new UpdateUserException(e.getMessage(), e);
        }
    }

    @Override
    public void updateUSer(User user) {
        try {
            int id = user.getId();
            User oldUser = userMap.get(id);
            if (oldUser == null) {
                oldUser = daoService.getUser(id);
                userMap.put(id, oldUser);
            }
            if (!oldUser.getCountry().equals(user.getCountry())) {
                throw new UpdateUserException(COUNTRY_IS_WRONG);
            }
            userMap.computeIfPresent(id, (key, oldUserInformation) -> user);
        } catch (SQLException e) {
            throw new UpdateUserException(e.getMessage(), e);
        }
    }

    @Override
    public void updateStats(int id, int activity) {
        try {
            User user = userMap.get(id);
            if (user == null) {
                user = daoService.getUser(id);
                userMap.put(id, user);
            }
            activityMap.putIfAbsent(id, new LinkedList<>());
            List<ActivityTime> activityTimes = activityMap.get(id);
            activityTimes.add(new ActivityTime(activity, Calendar.getInstance().getTime().getTime()));
            if (activityTimes.size() == COUNT_STATS_PER_DAY) {
                daoService.addUserActivities(activityTimes, id);
                activityTimes.clear();
            }
        } catch (SQLException e) {
            throw new UpdateUserException(e.getMessage(), e);
        }
    }
}
