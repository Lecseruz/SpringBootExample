package com.magomed.springBootExample.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magomed.springBootExample.api.DataNotFoundException;
import com.magomed.springBootExample.api.IUserManager;
import com.magomed.springBootExample.api.UserBuilder;
import org.springframework.stereotype.Service;

@Service
public class UserManager implements IUserManager {
    private ObjectMapper mapper = new ObjectMapper();

    public UserBuilder getUserData(String body) {
        try {
            UserBuilder userBuilder = new UserBuilder();
            JsonNode node = mapper.readValue(body, JsonNode.class);
            userBuilder.country(node.get("country").asText());
            userBuilder.money(node.get("money").asLong());
            return userBuilder;
        } catch (Exception e) {
            throw new DataNotFoundException("money or country is missing ", e);
        }
    }
}
