package model.primary;

import java.util.Date;

/**
 * "name": "Cloth Boots",
 * "description": "Hide-soled shoes provide some protection from the heat and cold, but only minimal protection from injuries.",
 * "image_file": "cloth_boots.webp",
 * "level": 3,
 * "yield": 1,
 * "points": 0,
 * "xp": 0,
 * "crafting_time": 0,
 * "composition": [
 * {
 * "name": "Fiber",
 * "quantity": 25
 * },
 * {
 * "name": "Hide",
 * "quantity": 6
 * }
 * ]
 */
public class Engram {
    private final String uuid;
    private final String name;
    private final String description;
    private final String imageFile;
    private final int level;
    private final int yield;
    private final int points;
    private final int xp;
    private final int craftingTime;
    private final Date lastUpdated;
    private final String gameId;

    public Engram(String uuid, String name, String description, String imageFile, int level, int yield, int points,
                  int xp, int craftingTime, Date lastUpdated, String gameId) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.imageFile = imageFile;
        this.level = level;
        this.yield = yield;
        this.points = points;
        this.xp = xp;
        this.craftingTime = craftingTime;
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

    public int getLevel() {
        return level;
    }

    public int getYield() {
        return yield;
    }

    public int getPoints() {
        return points;
    }

    public int getXp() {
        return xp;
    }

    public int getCraftingTime() {
        return craftingTime;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public String getGameId() {
        return gameId;
    }

    public boolean equals(String name, String description, String imageFile, int level, int yield, int points, int xp) {
        return getName().equals(name) &&
                getDescription().equals(description) &&
                getImageFile().equals(imageFile) &&
                getLevel() == level &&
                getYield() == yield &&
                getPoints() == points &&
                getXp() == xp;
    }
}