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
                             @JsonProperty(cEngramId) String sourceId,
                             @JsonProperty(cLastUpdated) Date lastUpdated) {
        super(uuid, name, imageFile, sourceId, lastUpdated);
    }

    public static IlluminateStation fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, IlluminateStation.class);
    }

    @JsonIgnore
    @Override
    public String getSourceId() {
        return super.getSourceId();
    }

    @JsonIgnore
    @Override
    public Date getLastUpdated() {
        return super.getLastUpdated();
    }
}