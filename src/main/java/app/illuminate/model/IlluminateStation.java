package app.illuminate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Station;

import java.util.Date;

import static util.Constants.*;

public class IlluminateStation extends Station {

    @JsonCreator
    public IlluminateStation(@JsonProperty(cUuid) String uuid,
                             @JsonProperty(cName) String name,
                             @JsonProperty(cImageFile) String imageFile,
                             @JsonProperty(cEngramId) String engramId,
                             @JsonProperty(cLastUpdated) Date lastUpdated,
                             @JsonProperty(cGameId) String gameId) {
        super(uuid, name, imageFile, engramId, lastUpdated, gameId);
    }

    public static IlluminateStation fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, IlluminateStation.class);
    }

    @JsonIgnore
    @Override
    public String getEngramId() {
        return super.getEngramId();
    }

    @JsonIgnore
    @Override
    public String getGameId() {
        return super.getGameId();
    }
}