package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Composite {
    private final String uuid;
    private final String name;
    private final String imageFile;
    private final int quantity;
    private final String sourceId;
    private final boolean isEngram;
    private final String compositionId;
    private final String gameId;

    public Composite(String uuid,
                     String name,
                     String imageFile,
                     int quantity,
                     String sourceId,
                     boolean isEngram,
                     String compositionId,
                     String gameId) {
        this.uuid = uuid;
        this.name = name;
        this.imageFile = imageFile;
        this.quantity = quantity;
        this.sourceId = sourceId;
        this.isEngram = isEngram;
        this.compositionId = compositionId;
        this.gameId = gameId;
    }

    public static Composite fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, Composite.class);
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

    @JsonProperty("isEngram")
    public boolean isEngram() {
        return isEngram;
    }

    public String getCompositionId() {
        return compositionId;
    }

    public String getGameId() {
        return gameId;
    }

    public boolean equals(String name, String imageFile, int quantity, String sourceId, boolean isEngram, String compositionId) {
        return getName().equals(name) &&
                getImageFile().equals(imageFile) &&
                getQuantity() == quantity &&
                getSourceId().equals(sourceId) &&
                isEngram() == isEngram &&
                getCompositionId().equals(compositionId);
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
                ", gameId='" + gameId + '\'' +
                '}';
    }
}