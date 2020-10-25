package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

import static util.Constants.*;

public class Station {
    private final String uuid;
    private final String name;
    private final String imageFile;
    private final String sourceId;
    private final Date lastUpdated;

    @JsonCreator
    public Station(@JsonProperty(cUuid) String uuid,
                   @JsonProperty(cName) String name,
                   @JsonProperty(cImageFile) String imageFile,
                   @JsonProperty(cSourceId) String sourceId,
                   @JsonProperty(cLastUpdated) Date lastUpdated) {
        this.uuid = uuid;
        this.name = name;
        this.imageFile = imageFile;
        this.sourceId = sourceId;
        this.lastUpdated = lastUpdated;
    }

    public static Station fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, Station.class);
    }

    public static Station comparable(String name, String imageFile, String sourceId) {
        return new Station(null, name, imageFile, sourceId, null);
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

    public String getImageFile() {
        return imageFile;
    }

    public String getSourceId() {
        return sourceId == null ? "" : sourceId;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public boolean equals(Station station) {
        return name.equals(station.name) &&
                imageFile.equals(station.imageFile);
    }

    @Override
    public String toString() {
        return "Station{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", imageFile='" + imageFile + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}