package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.Objects;

import static util.Constants.*;

public class Station {
    private final String uuid;
    private final String name;
    private final String imageFile;
    private final String engramId;
    private final Date lastUpdated;

    @JsonCreator
    public Station(@JsonProperty(cUuid) String uuid,
                   @JsonProperty(cName) String name,
                   @JsonProperty(cImageFile) String imageFile,
                   @JsonProperty(cEngramId) String engramId,
                   @JsonProperty(cLastUpdated) Date lastUpdated) {
        this.uuid = uuid;
        this.name = name;
        this.imageFile = imageFile;
        this.engramId = engramId;
        this.lastUpdated = lastUpdated;
    }

    public static Station fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, Station.class);
    }

    public static Station comparable(String name, String imageFile, String engramId) {
        return new Station(null, name, imageFile, engramId, null);
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

    public String getEngramId() {
        return engramId;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return name.equals(station.name) &&
                imageFile.equals(station.imageFile) &&
                engramId.equals(station.engramId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, imageFile, engramId);
    }

    @Override
    public String toString() {
        return "Station{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", imageFile='" + imageFile + '\'' +
                ", engramId='" + engramId + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}