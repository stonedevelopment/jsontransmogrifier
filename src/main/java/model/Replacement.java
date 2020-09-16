package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static util.Constants.*;

public class Replacement {
    private final String uuid;
    private final Object from;
    private final Object to;

    @JsonCreator
    public Replacement(@JsonProperty(cUuid) String uuid,
                       @JsonProperty(cFrom) Object from,
                       @JsonProperty(cTo) Object to) {
        this.uuid = uuid;
        this.from = from;
        this.to = to;
    }

    public static Replacement fromJson(JsonNode jsonNode) {
        return new ObjectMapper().convertValue(jsonNode, Replacement.class);
    }

    public String getUuid() {
        return uuid;
    }

    public Object getFrom() {
        return from;
    }

    public Object getTo() {
        return to;
    }
}