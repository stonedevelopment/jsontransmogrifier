package app.illuminate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Composition;

import java.util.UUID;

import static util.Constants.cEngramId;
import static util.Constants.cUuid;

public class IlluminateComposition extends Composition {

    @JsonCreator
    public IlluminateComposition(@JsonProperty(cUuid) String uuid,
                                 @JsonProperty(cEngramId) String engramId) {
        super(uuid, engramId);
    }

    public static IlluminateComposition fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, IlluminateComposition.class);
    }

    public static IlluminateComposition with(String engramId) {
        return new IlluminateComposition(UUID.randomUUID().toString(),
                engramId);
    }

    @JsonIgnore
    @Override
    public String getEngramId() {
        return super.getEngramId();
    }
}