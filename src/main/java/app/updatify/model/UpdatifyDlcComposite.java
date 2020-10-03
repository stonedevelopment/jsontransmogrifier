package app.updatify.model;

import model.Composite;

import java.util.UUID;

public class UpdatifyDlcComposite extends UpdatifyComposite {
    public UpdatifyDlcComposite(String uuid, String name, String imageFile, int quantity, String sourceId, boolean isEngram, String compositionId) {
        super(uuid, name, imageFile, quantity, sourceId, isEngram, compositionId);
    }

    public static Composite convertToNew(Composite fromComposite, String toName, String toSourceId) {
        return new Composite(UUID.randomUUID().toString(),
                toName,
                fromComposite.getImageFile(),
                fromComposite.getQuantity(),
                toSourceId,
                fromComposite.isEngram(),
                fromComposite.getCompositionId());
    }
}