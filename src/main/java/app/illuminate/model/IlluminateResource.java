package app.illuminate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Resource;

import java.util.Date;

import static util.Constants.*;

public class IlluminateResource extends Resource {

    @JsonCreator
    public IlluminateResource(@JsonProperty(cUuid) String uuid,
                              @JsonProperty(cName) String name,
                              @JsonProperty(cDescription) String description,
                              @JsonProperty(cImageFile) String imageFile,
                              @JsonProperty(cLastUpdated) Date lastUpdated) {
        super(uuid, name, description, imageFile, lastUpdated);
    }

    public static IlluminateResource fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, IlluminateResource.class);
    }
}
