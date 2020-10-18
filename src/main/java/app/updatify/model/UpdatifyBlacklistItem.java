package app.updatify.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

import static util.Constants.*;

public class UpdatifyBlacklistItem {
    private final String uuid;
    private final String sourceId;
    private final String gameId;

    @JsonCreator
    public UpdatifyBlacklistItem(@JsonProperty(cUuid) String uuid,
                                 @JsonProperty(cSourceId) String sourceId,
                                 @JsonProperty(cGameId) String gameId) {
        this.uuid = uuid;
        this.sourceId = sourceId;
        this.gameId = gameId;
    }

    public static UpdatifyBlacklistItem createFrom(String sourceId, String gameId) {
        return new UpdatifyBlacklistItem(UUID.randomUUID().toString(),
                sourceId,
                gameId);
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
}