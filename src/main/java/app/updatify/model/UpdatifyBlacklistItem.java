package app.updatify.model;

import java.util.UUID;

public class UpdatifyBlacklistItem {
    private final String uuid;
    private final String sourceId;

    public UpdatifyBlacklistItem(String uuid, String sourceId) {
        this.uuid = uuid;
        this.sourceId = sourceId;
    }

    public static UpdatifyBlacklistItem createFrom(String sourceId) {
        return new UpdatifyBlacklistItem(UUID.randomUUID().toString(),
                sourceId);
    }

    public String getUuid() {
        return uuid;
    }

    public String getSourceId() {
        return sourceId;
    }
}
