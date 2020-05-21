package transmogrify.model.primary;

public class Composite {
    private final String uuid;
    private final String sourceId;
    private final int quantity;
    private final boolean isEngram;
    private final String compositionId;
    private final String gameId;

    public Composite(String uuid, String sourceId, int quantity, boolean isEngram, String compositionId, String gameId) {
        this.uuid = uuid;
        this.sourceId = sourceId;
        this.quantity = quantity;
        this.isEngram = isEngram;
        this.compositionId = compositionId;
        this.gameId = gameId;
    }

    public String getUuid() {
        return uuid;
    }

    public String getSourceId() {
        return sourceId;
    }

    public int getQuantity() {
        return quantity;
    }

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