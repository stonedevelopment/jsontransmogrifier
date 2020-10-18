package app.updatify.model;

import app.illuminate.model.IlluminateEngram;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.Engram;

import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import static util.Constants.*;

public class UpdatifyEngram extends Engram {
    private final String gameId;

    @JsonCreator
    public UpdatifyEngram(@JsonProperty(cUuid) String uuid,
                          @JsonProperty(cName) String name,
                          @JsonProperty(cDescription) String description,
                          @JsonProperty(cImageFile) String imageFile,
                          @JsonProperty(cLevel) int level,
                          @JsonProperty(cYield) int yield,
                          @JsonProperty(cPoints) int points,
                          @JsonProperty(cXp) int xp,
                          @JsonProperty(cCraftingTime) int craftingTime,
                          @JsonProperty(cLastUpdated) Date lastUpdated,
                          @JsonProperty(cGameId) String gameId) {
        super(uuid, name, description, imageFile, level, yield, points, xp, craftingTime, lastUpdated);
        this.gameId = gameId;
    }

    public static IlluminateEngram fromJson(JsonNode jsonNode) {
        ObjectNode outNode = new ObjectMapper().createObjectNode();

        Iterator<String> fieldNames = jsonNode.fieldNames();
        Iterator<JsonNode> elements = jsonNode.elements();

        while (elements.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode element = elements.next();
            if (fieldName.equals(cComposition)) continue;

            outNode.set(fieldName, element);
        }

        return IlluminateEngram.fromJson(outNode);
    }

    public static UpdatifyEngram createFrom(IlluminateEngram iEngram, String gameId) {
        return new UpdatifyEngram(UUID.randomUUID().toString(),
                iEngram.getName(),
                iEngram.getDescription(),
                iEngram.getImageFile(),
                iEngram.getLevel(),
                iEngram.getYield(),
                iEngram.getPoints(),
                iEngram.getXp(),
                iEngram.getCraftingTime(),
                new Date(),
                gameId);
    }

    public static UpdatifyEngram updateToNew(Engram tEngram, IlluminateEngram iEngram, String gameId) {
        return new UpdatifyEngram(tEngram.getUuid(),
                iEngram.getName(),
                iEngram.getDescription(),
                iEngram.getImageFile(),
                iEngram.getLevel(),
                iEngram.getYield(),
                iEngram.getPoints(),
                iEngram.getXp(),
                iEngram.getCraftingTime(),
                new Date(),
                gameId);
    }

    public static UpdatifyEngram with(Engram engram, String gameId) {
        return new UpdatifyEngram(engram.getUuid(),
                engram.getName(),
                engram.getDescription(),
                engram.getImageFile(),
                engram.getLevel(),
                engram.getYield(),
                engram.getPoints(),
                engram.getXp(),
                engram.getCraftingTime(),
                engram.getLastUpdated(),
                gameId);
    }

    public String getGameId() {
        return gameId;
    }
}