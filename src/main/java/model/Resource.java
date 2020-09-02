package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

import static util.Constants.*;

/**
 * "name": "Absorbent Substrate",
 * "description": "",
 * "image_file": "absorbent_substrate.webp"
 */
public class Resource {
    private final String uuid;
    private final String name;
    private final String description;
    private final String imageFile;
    private final Date lastUpdated;
    private final String gameId;

    @JsonCreator
    public Resource(@JsonProperty(cUuid) String uuid,
                    @JsonProperty(cName) String name,
                    @JsonProperty(cDescription) String description,
                    @JsonProperty(cImageFile) String imageFile,
                    @JsonProperty(cLastUpdated) Date lastUpdated,
                    @JsonProperty(cGameId) String gameId) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.imageFile = imageFile;
        this.lastUpdated = lastUpdated;
        this.gameId = gameId;
    }

    public static Resource fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, Resource.class);
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

    public String getDescription() {
        return description;
    }

    public String getImageFile() {
        return imageFile;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public String getGameId() {
        return gameId;
    }

    public boolean equals(String name, String description, String imageFile) {
        return getName().equals(name) &&
                getDescription().equals(description) &&
                getImageFile().equals(imageFile);
    }
}
