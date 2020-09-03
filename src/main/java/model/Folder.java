package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

import static util.Constants.cName;
import static util.Constants.cUuid;

public class Folder {
    private final String uuid;
    private final String name;

    @JsonCreator
    public Folder(@JsonProperty(cUuid) String uuid,
                  @JsonProperty(cName) String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public static Folder fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, Folder.class);
    }

    public static Folder comparable(String name) {
        return new Folder(null, name);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Folder folder = (Folder) o;
        return name.equals(folder.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Folder{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}