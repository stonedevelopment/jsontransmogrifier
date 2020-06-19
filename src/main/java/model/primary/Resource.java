package model.primary;

import java.util.Date;

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

    public Resource(String uuid, String name, String description, String imageFile, Date lastUpdated, String gameId) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.imageFile = imageFile;
        this.lastUpdated = lastUpdated;
        this.gameId = gameId;
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
