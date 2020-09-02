package app.illuminate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Composition;

import java.util.Date;

import static util.Constants.*;

public class IlluminateComposition extends Composition {

    @JsonCreator
    public IlluminateComposition(@JsonProperty(cUuid) String uuid,
                                 @JsonProperty(cEngramId) String engramId,
                                 @JsonProperty(cLastUpdated) Date lastUpdated,
                                 @JsonProperty(cGameId) String gameId) {
        super(uuid, engramId, lastUpdated, gameId);
    }

    public static IlluminateComposition fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, IlluminateComposition.class);
    }

    @JsonIgnore
    @Override
    public String getGameId() {
        return super.getGameId();
    }
}