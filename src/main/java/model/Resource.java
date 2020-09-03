package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.Objects;

import static util.Constants.*;

/**
 * "name": "Absorbent Substrate",
 * "description": "",
 * "image_file": "absorbent_substrate.webp"
 */
public class Resource {
    private final String uuid;
    private final String name;
    private final String description;
    private final String imageFile;
    private final Date lastUpdated;

    @JsonCreator
    public Resource(@JsonProperty(cUuid) String uuid,
                    @JsonProperty(cName) String name,
                    @JsonProperty(cDescription) String description,
                    @JsonProperty(cImageFile) String imageFile,
                    @JsonProperty(cLastUpdated) Date lastUpdated) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.imageFile = imageFile;
        this.lastUpdated = lastUpdated;
    }

    public static Resource fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, Resource.class);
    }

    public static Resource comparable(String name, String description, String imageFile) {
        return new Resource(null, name, description, imageFile, null);
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

    public Date getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return name.equals(resource.name) &&
                description.equals(resource.description) &&
                imageFile.equals(resource.imageFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, imageFile);
    }

    @Override
    public String toString() {
        return "Resource{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageFile='" + imageFile + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}