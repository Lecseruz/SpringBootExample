package com.magomed.springBootExample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magomed.springBootExample.api.IUserManager;
import com.magomed.springBootExample.api.IUserStatBusinessService;
import com.magomed.springBootExample.internal.ServerUtils;
import com.magomed.springBootExample.api.UpdateUserException;
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

@Controller
public class RestController {
    public static final Logger logger = LoggerFactory.getLogger(RestController.class);
    @Autowired
    private IUserStatBusinessService service;

    @Autowired
    private IUserManager manager;

    @GetMapping(path = ServerUtils.GET_USER)
    public ResponseEntity<?> getUser(@RequestParam(value = "id") int id) {
        try {
            User newUser = service.syncUserAndGet(id);
            logger.info("Get user information with id = " + id + " completed successfully");
            return ResponseEntity.ok(new ObjectMapper().writeValueAsString(newUser));
        } catch (UpdateUserException | JsonProcessingException e) {
            logger.error("Get user information  with id = " + id + " failed", e);
            return ResponseEntity.badRequest().body(ServerUtils.FAILED);
        }
    }

    @PostMapping(path = ServerUtils.SYNC_USER)
    public ResponseEntity<?> syncUser(@RequestParam(value = "id") int id, @RequestBody String body) {
        try {
            User user = manager.getUserData(body).id(id).build();
            service.updateUSer(user);
            logger.info("Sync user information with id = " + id + " completed successfully");
            return ResponseEntity.ok().build();
        } catch (UpdateUserException e) {
            logger.error("Sync user information with id = " + id + "failed", e);
            return ResponseEntity.badRequest().body(ServerUtils.FAILED);
        }
    }

    @GetMapping(path = ServerUtils.RECEIVE_STATS)
    public ResponseEntity<?> receiveStats(@RequestParam(value = "id") int id, @RequestParam(value = "activity") int activity) {
        try {
            service.updateStats(id, activity);
            logger.info("Add statistics  with id = " + id + " completed successfully");
            return ResponseEntity.ok().build();
        } catch (UpdateUserException e) {
            logger.error("Add statistics  with id = " + id + "  failed", e);
            return ResponseEntity.badRequest().body(ServerUtils.FAILED);
        }
    }
}
