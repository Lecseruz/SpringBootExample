package com.magomed.application.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magomed.application.api.DataNotFoundException;
import com.magomed.application.api.IUserParser;
import com.magomed.application.api.UserBuilder;
import org.springframework.stereotype.Service;

@Service
public class UserParser implements IUserParser {
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
