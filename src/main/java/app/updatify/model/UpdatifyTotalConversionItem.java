package app.updatify.model;

import model.Resource;

import java.util.UUID;

public class UpdatifyTotalConversionItem {
    private final String uuid;
    private final String fromUuid;
    private final String toUuid;

    public UpdatifyTotalConversionItem(String uuid, String fromUuid, String toUuid) {
        this.uuid = uuid;
        this.fromUuid = fromUuid;
        this.toUuid = toUuid;
    }

    public static UpdatifyTotalConversionItem createFrom(Resource fromResource, Resource toResource) {
        return new UpdatifyTotalConversionItem(UUID.randomUUID().toString(),
                fromResource.getUuid(),
                toResource.getUuid());
    }

    public String getUuid() {
        return uuid;
    }

    public String getFromUuid() {
        return fromUuid;
    }

    public String getToUuid() {
        return toUuid;
    }
}
