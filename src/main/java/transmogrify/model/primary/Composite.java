package transmogrify.model.primary;

import com.fasterxml.jackson.annotation.JsonProperty;

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
}