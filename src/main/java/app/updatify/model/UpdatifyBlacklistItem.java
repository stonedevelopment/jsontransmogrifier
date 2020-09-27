package app.updatify.model;

import java.util.UUID;

public class UpdatifyBlacklistItem {
    private final String uuid;
    private final String name;
    private final String sourceId;

    public UpdatifyBlacklistItem(String uuid, String name, String sourceId) {
        this.uuid = uuid;
        this.name = name;
        this.sourceId = sourceId;
    }

    public static UpdatifyBlacklistItem createFrom(String name, String sourceId) {
        return new UpdatifyBlacklistItem(UUID.randomUUID().toString(),
                name,
                sourceId);
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }
}
