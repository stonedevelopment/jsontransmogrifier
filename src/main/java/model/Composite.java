package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.Objects;

import static util.Constants.*;

/**
 * {
 * "uuid": "140cddf5-1eba-409d-b7d5-e68a352b20cd",
 * "name": "Absorbent Substrate",
 * "imageFile": "absorbent_substrate.webp",
 * "quantity": 10,
 * "sourceId": "9f9553a9-2a0a-487e-9a7c-174ff59c3788",
 * "compositionId": "1a8d65ae-5c73-406c-8ce3-cbf7132809f6",
 * "gameId": "4673f69f-2979-4518-ab7c-a1a1fae7c612",
 * "isEngram": true
 * },
 */
public class Composite {
    private final String uuid;
    private final String name;
    private final String imageFile;
    private final int quantity;
    private final String sourceId;
    private final boolean isEngram;
    private final String compositionId;
    private final Date lastUpdated;

    @JsonCreator
    public Composite(@JsonProperty(cUuid) String uuid,
                     @JsonProperty(cName) String name,
                     @JsonProperty(cImageFile) String imageFile,
                     @JsonProperty(cQuantity) int quantity,
                     @JsonProperty(cSourceId) String sourceId,
                     @JsonProperty(cIsEngram) boolean isEngram,
                     @JsonProperty(cCompositionId) String compositionId,
                     @JsonProperty(cLastUpdated) Date lastUpdated) {
        this.uuid = uuid;
        this.name = name;
        this.imageFile = imageFile;
        this.quantity = quantity;
        this.sourceId = sourceId;
        this.isEngram = isEngram;
        this.compositionId = compositionId;
        this.lastUpdated = lastUpdated;
    }

    public static Composite fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, Composite.class);
    }

    public static Composite comparable(String name, String imageFile, int quantity, String sourceId, boolean isEngram, String compositionId) {
        return new Composite(null, name, imageFile, quantity, sourceId, isEngram, compositionId, null);
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

    public int getQuantity() {
        return quantity;
    }

    public String getSourceId() {
        return sourceId;
    }

    @JsonProperty(cIsEngram)
    public boolean isEngram() {
        return isEngram;
    }

    public String getCompositionId() {
        return compositionId;
    }

    public Date getLastUpdated() {
        return lastUpdated == null ? new Date() : lastUpdated;
    }

    public boolean equals(Composite composite) {
        return quantity == composite.quantity &&
                isEngram == composite.isEngram &&
                name.equals(composite.name);
    }

    @Override
    public String toString() {
        return "Composite{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", imageFile='" + imageFile + '\'' +
                ", quantity=" + quantity +
                ", sourceId='" + sourceId + '\'' +
                ", isEngram=" + isEngram +
                ", compositionId='" + compositionId + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}