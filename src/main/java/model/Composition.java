package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

public class Composition {
    private final String uuid;
    private final String engramId;
    private final Date lastUpdated;
    private final String gameId;

    public Composition(String uuid, String engramId, Date lastUpdated, String gameId) {
        this.uuid = uuid;
        this.engramId = engramId;
        this.lastUpdated = lastUpdated;
        this.gameId = gameId;
    }

    public static Composition fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, Composition.class);
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

    public String getGameId() {
        return gameId;
    }

    public boolean equals(String engramId) {
        return getEngramId().equals(engramId);
    }
}
