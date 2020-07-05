package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

public class Station {
    private final String uuid;
    private final String name;
    private final String imageFile;
    private final String engramId;
    private final Date lastUpdated;
    private final String gameId;

    public Station(String uuid, String name, String imageFile, String engramId, Date lastUpdated, String gameId) {
        this.uuid = uuid;
        this.name = name;
        this.imageFile = imageFile;
        this.engramId = engramId;
        this.lastUpdated = lastUpdated;
        this.gameId = gameId;
    }

    public static Station fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, Station.class);
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getImageFile() {
        return imageFile;
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

    public boolean equals(String name, String imageFile, String engramId) {
        return this.name.equals(name) &&
                this.name.equals(imageFile) &&
                this.engramId.equals(engramId);
    }
}
