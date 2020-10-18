package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

import static util.Constants.*;

public class Composition {
    private final String uuid;
    private final String engramId;
    private final Date lastUpdated;

    @JsonCreator
    public Composition(@JsonProperty(cUuid) String uuid,
                       @JsonProperty(cEngramId) String engramId,
                       @JsonProperty(cLastUpdated) Date lastUpdated) {
        this.uuid = uuid;
        this.engramId = engramId;
        this.lastUpdated = lastUpdated;
    }

    public static Composition fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, Composition.class);
    }

    public static Composition comparable(String engramId) {
        return new Composition(null, engramId, new Date());
    }

    public String getUuid() {
        return uuid;
    }

    public String getEngramId() {
        return engramId;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public boolean equals(Composition composition) {
        return engramId.equals(composition.engramId);
    }

    @Override
    public String toString() {
        return "Composition{" +
                "uuid='" + uuid + '\'' +
                ", engramId='" + engramId + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
