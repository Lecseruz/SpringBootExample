package com.magomed.springBootExample;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magomed.springBootExample.api.IUserDaoService;
import com.magomed.springBootExample.api.IUserManager;
import com.magomed.springBootExample.internal.ActivityTime;
import com.magomed.springBootExample.internal.ServerUtils;
import com.magomed.springBootExample.internal.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class RestController {
    public static final Logger logger = LoggerFactory.getLogger(RestController.class);
    private static final ConcurrentHashMap<Integer, User> map = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, List<ActivityTime>> activityMap = new ConcurrentHashMap<>();
    public static final int COUNT_STATS_PER_DAY = 10000;

    @Autowired
    private IUserDaoService daoService;

    @Autowired
    private IUserManager manager;

    @GetMapping(path = "/getUser")
    public ResponseEntity<?> getUser(@RequestParam(value = "id") int id) {
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
            User newUser = daoService.getUser(id);
            logger.info("Get user information with id = " + id + " completed successfully");
            return ResponseEntity.ok(new ObjectMapper().writeValueAsString(newUser));
        } catch (Exception e) {
            logger.error("Get user information  with id = " + id + " failed", e);
            return ResponseEntity.badRequest().body(ServerUtils.FAILED);
        }
    }

    @PostMapping(path = "/syncUser")
    public ResponseEntity<?> syncUser(@RequestParam(value = "id") int id, @RequestBody String body) {
        try {
            User oldUser = map.get(id);
            if (oldUser == null) {
                oldUser = daoService.getUser(id);
                map.put(id, oldUser);
            }
            User user = manager.getUserData(body).id(id).build();
            if (!oldUser.getCountry().equals(user.getCountry())) {
                return ResponseEntity.badRequest().body(ServerUtils.COUNTRY_IS_WRONG);
            }
            map.computeIfPresent(id, (key, oldUserInformation) -> user);
            logger.info("Sync user information with id = " + id + " completed successfully");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Sync user information with id = " + id + "failed", e);
            return ResponseEntity.badRequest().body(ServerUtils.FAILED);
        }
    }
    @GetMapping(path = ServerUtils.RECEIVE_STATS)
    public ResponseEntity<?> receiveStats(@RequestParam(value = "id") int id, @RequestParam(value = "activity") int activity) {
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
            logger.info("Add statistics  with id = " + id + " completed successfully");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Add statistics  with id = " + id + "  failed", e);
            return ResponseEntity.badRequest().body(ServerUtils.FAILED);
        }
    }
}
