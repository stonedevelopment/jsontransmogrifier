package app.updatify.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

import static util.Constants.*;

public class UpdatifyBlacklistItem {
    private final String uuid;
    private final String sourceId;
    private final String gameId;
    private final String dlcId;

    @JsonCreator
    public UpdatifyBlacklistItem(@JsonProperty(cUuid) String uuid,
                                 @JsonProperty(cSourceId) String sourceId,
                                 @JsonProperty(cGameId) String gameId,
                                 @JsonProperty(cDlcId) String dlcId) {
        this.uuid = uuid;
        this.sourceId = sourceId;
        this.gameId = gameId;
        this.dlcId = dlcId;
    }

    public static UpdatifyBlacklistItem createFrom(String sourceId,
                                                   String gameId,
                                                   String dlcId) {
        return new UpdatifyBlacklistItem(UUID.randomUUID().toString(),
                sourceId,
                gameId,
                dlcId);
    }

    public String getUuid() {
        return uuid;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getGameId() {
        return gameId;
    }

    public String getDlcId() {
        return dlcId;
    }
}