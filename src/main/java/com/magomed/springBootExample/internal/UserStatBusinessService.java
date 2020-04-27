package com.magomed.springBootExample.internal;

import com.magomed.springBootExample.api.IUserService;
import com.magomed.springBootExample.api.IUserStatBusinessService;
import com.magomed.springBootExample.api.UpdateUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserStatBusinessService implements IUserStatBusinessService {
    private static final ConcurrentHashMap<Integer, User> map = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, List<ActivityTime>> activityMap = new ConcurrentHashMap<>();
    public static final int COUNT_STATS_PER_DAY = 10000;

    @Autowired
    private IUserService daoService;

    @Override
    public User syncUserAndGet(int id) {
        try {
            User user = map.get(id);
            if (user != null) {
                daoService.syncUser(map.get(id));
                map.remove(id);
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
            User oldUser = map.get(id);
            if (oldUser == null) {
                oldUser = daoService.getUser(id);
                map.put(id, oldUser);
            }
            if (!oldUser.getCountry().equals(user.getCountry())) {
                throw new UpdateUserException(ServerUtils.COUNTRY_IS_WRONG);
            }
            map.computeIfPresent(id, (key, oldUserInformation) -> user);
        } catch (SQLException e) {
            throw new UpdateUserException(e.getMessage(), e);
        }
    }

    @Override
    public void updateStats(int id, int activity) {
        try {
            User user = map.get(id);
            if (user == null) {
                user = daoService.getUser(id);
                map.put(id, user);
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
