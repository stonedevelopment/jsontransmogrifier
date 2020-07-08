package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Folder {
    private final String uuid;
    private final String name;
    private final String gameId;

    public Folder(String uuid, String name, String gameId) {
        this.uuid = uuid;
        this.name = name;
        this.gameId = gameId;
    }

    public static Folder fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, Folder.class);
    }

    public JsonNode toJson() {
        return new ObjectMapper().valueToTree(this);
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getGameId() {
        return gameId;
    }

    public boolean equals(String name) {
        return getName().equals(name);
    }
}
