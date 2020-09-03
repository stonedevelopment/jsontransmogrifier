package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

import static util.Constants.cEngramId;
import static util.Constants.cUuid;

public class Composition {
    private final String uuid;
    private final String engramId;

    @JsonCreator
    public Composition(@JsonProperty(cUuid) String uuid,
                       @JsonProperty(cEngramId) String engramId) {
        this.uuid = uuid;
        this.engramId = engramId;
    }

    public static Composition fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, Composition.class);
    }

    public static Composition comparable(String engramId) {
        return new Composition(null, engramId);
    }

    public String getUuid() {
        return uuid;
    }

    public String getEngramId() {
        return engramId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Composition that = (Composition) o;
        return engramId.equals(that.engramId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(engramId);
    }

    @Override
    public String toString() {
        return "Composition{" +
                "uuid='" + uuid + '\'' +
                ", engramId='" + engramId + '\'' +
                '}';
    }
}
