package app.updatify.model;

import model.Composite;
import model.Resource;

import java.util.UUID;

public class UpdatifyTotalConversionItem {
    private final String uuid;
    private final String fromCompositeId;
    private final String toCompositeId;

    public UpdatifyTotalConversionItem(String uuid, String fromCompositeId, String toCompositeId) {
        this.uuid = uuid;
        this.fromCompositeId = fromCompositeId;
        this.toCompositeId = toCompositeId;
    }

    public static UpdatifyTotalConversionItem createFrom(Composite fromComposite, Composite toComposite) {
        return new UpdatifyTotalConversionItem(UUID.randomUUID().toString(),
                fromComposite.getUuid(),
                toComposite.getUuid());
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
}
