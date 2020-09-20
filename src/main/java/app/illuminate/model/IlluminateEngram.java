package app.illuminate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Engram;

import java.util.Date;

import static util.Constants.*;

public class IlluminateEngram extends Engram {

    @JsonCreator
    public IlluminateEngram(@JsonProperty(cUuid) String uuid,
                            @JsonProperty(cName) String name,
                            @JsonProperty(cDescription) String description,
                            @JsonProperty(cImageFile) String imageFile,
                            @JsonProperty(cLevel) int level,
                            @JsonProperty(cYield) int yield,
                            @JsonProperty(cPoints) int points,
                            @JsonProperty(cXp) int xp,
                            @JsonProperty(cCraftingTime) int craftingTime,
                            @JsonProperty(cLastUpdated) Date lastUpdated) {
        super(uuid, name, description, imageFile, level, yield, points, xp, craftingTime, lastUpdated);
    }

    public static IlluminateEngram fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, IlluminateEngram.class);
    }

    @JsonIgnore
    @Override
    public String getUuid() {
        return super.getUuid();
    }

    @JsonIgnore
    @Override
    public Date getLastUpdated() {
        return super.getLastUpdated();
    }
}