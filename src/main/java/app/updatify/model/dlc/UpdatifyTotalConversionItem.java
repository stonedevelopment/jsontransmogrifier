package app.updatify.model.dlc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Composite;

import java.util.UUID;

import static util.Constants.*;

public class UpdatifyTotalConversionItem {
    private final String uuid;
    private final String fromCompositeId;
    private final String toCompositeId;
    private final String gameId;
    private final String dlcId;

    @JsonCreator
    public UpdatifyTotalConversionItem(@JsonProperty(cUuid) String uuid,
                                       @JsonProperty(cFromCompositeId) String fromCompositeId,
                                       @JsonProperty(cToCompositeId) String toCompositeId,
                                       @JsonProperty(cGameId) String gameId,
                                       @JsonProperty(cDlcId) String dlcId) {
        this.uuid = uuid;
        this.fromCompositeId = fromCompositeId;
        this.toCompositeId = toCompositeId;
        this.gameId = gameId;
        this.dlcId = dlcId;
    }

    public static UpdatifyTotalConversionItem createFrom(Composite fromComposite,
                                                         Composite toComposite,
                                                         String gameId,
                                                         String dlcId) {
        return new UpdatifyTotalConversionItem(UUID.randomUUID().toString(),
                fromComposite.getUuid(),
                toComposite.getUuid(),
                gameId,
                dlcId);
    }

    public String getUuid() {
        return uuid;
    }

    public String getFromCompositeId() {
        return fromCompositeId;
    }

    public String getToCompositeId() {
        return toCompositeId;
    }

    public String getGameId() {
        return gameId;
    }

    public String getDlcId() {
        return dlcId;
    }
}