package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.Objects;

import static util.Constants.*;

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

    @JsonCreator
    public Engram(@JsonProperty(cUuid) String uuid,
                  @JsonProperty(cName) String name,
                  @JsonProperty(cDescription) String description,
                  @JsonProperty(cImageFile) String imageFile,
                  @JsonProperty(cLevel) int level,
                  @JsonProperty(cYield) int yield,
                  @JsonProperty(cPoints) int points,
                  @JsonProperty(cXp) int xp,
                  @JsonProperty(cCraftingTime) int craftingTime,
                  @JsonProperty(cLastUpdated) Date lastUpdated) {
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
    }

    public static Engram fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, Engram.class);
    }

    public static Engram comparable(String name, String description, String imageFile, int level, int yield, int points, int xp) {
        return new Engram(null, name, description, imageFile, level, yield, points, xp, 0, null);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Engram engram = (Engram) o;
        return level == engram.level &&
                yield == engram.yield &&
                points == engram.points &&
                xp == engram.xp &&
                name.equals(engram.name) &&
                description.equals(engram.description) &&
                imageFile.equals(engram.imageFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, imageFile, level, yield, points, xp);
    }

    @Override
    public String toString() {
        return "Engram{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageFile='" + imageFile + '\'' +
                ", level=" + level +
                ", yield=" + yield +
                ", points=" + points +
                ", xp=" + xp +
                ", craftingTime=" + craftingTime +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}