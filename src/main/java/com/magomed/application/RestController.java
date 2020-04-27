package com.magomed.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magomed.application.api.IUserParser;
import com.magomed.application.api.IUserStatBusinessService;
import com.magomed.application.api.UpdateUserException;
import com.magomed.application.internal.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RestController {
    public static final Logger logger = LoggerFactory.getLogger(RestController.class);

    private static final String FAILED = "FAILED";
    private static final String GET_USER = "/getUser";
    private static final String SYNC_USER = "/syncUser";
    private static final String RECEIVE_STATS = "/receiveStats";

    @Autowired
    private IUserStatBusinessService userStatBusinessService;

    @Autowired
    private IUserParser manager;

    @GetMapping(path = GET_USER)
    public ResponseEntity<?> getUser(@RequestParam(value = "id") int id) {
        try {
            User newUser = userStatBusinessService.syncUserAndGet(id);
            logger.info("Get user information with id = " + id + " completed successfully");
            return ResponseEntity.ok(new ObjectMapper().writeValueAsString(newUser));
        } catch (UpdateUserException | JsonProcessingException e) {
            logger.error("Get user information  with id = " + id + " failed", e);
            return ResponseEntity.badRequest().body(FAILED);
        }
    }

    @PostMapping(path = SYNC_USER)
    public ResponseEntity<?> syncUser(@RequestParam(value = "id") int id, @RequestBody String body) {
        try {
            User user = manager.getUserData(body).id(id).build();
            userStatBusinessService.updateUSer(user);
            logger.info("Sync user information with id = " + id + " completed successfully");
            return ResponseEntity.ok().build();
        } catch (UpdateUserException e) {
            logger.error("Sync user information with id = " + id + "failed", e);
            return ResponseEntity.badRequest().body(FAILED);
        }
    }

    @GetMapping(path = RECEIVE_STATS)
    public ResponseEntity<?> receiveStats(@RequestParam(value = "id") int id, @RequestParam(value = "activity") int activity) {
        try {
            userStatBusinessService.updateStats(id, activity);
            logger.info("Add statistics  with id = " + id + " completed successfully");
            return ResponseEntity.ok().build();
        } catch (UpdateUserException e) {
            logger.error("Add statistics  with id = " + id + "  failed", e);
            return ResponseEntity.badRequest().body(FAILED);
        }
    }
}
