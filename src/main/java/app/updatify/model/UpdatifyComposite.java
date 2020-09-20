package app.updatify.model;

import app.illuminate.model.IlluminateComposite;
import model.Composite;

import java.util.UUID;

public class UpdatifyComposite extends Composite {

    public UpdatifyComposite(String uuid, String name, String imageFile, int quantity, String sourceId, boolean isEngram, String compositionId) {
        super(uuid, name, imageFile, quantity, sourceId, isEngram, compositionId);
    }

    public static Composite createFrom(IlluminateComposite iComposite, String imageFile, String sourceId, String compositionId) {
        return new Composite(UUID.randomUUID().toString(),
                iComposite.getName(),
                imageFile,
                iComposite.getQuantity(),
                sourceId,
                iComposite.isEngram(),
                compositionId);
    }

    public static Composite updateToNew(Composite tComposite, IlluminateComposite iComposite) {
        return new Composite(tComposite.getUuid(),
                iComposite.getName(),
                tComposite.getImageFile(),
                iComposite.getQuantity(),
                tComposite.getSourceId(),
                iComposite.isEngram(),
                tComposite.getCompositionId());
    }
}