package model.dlc;

import model.primary.Composite;

public class DlcComposite extends Composite {
    private final String dlcId;

    public DlcComposite(String uuid,
                        String name,
                        String imageFile,
                        int quantity,
                        String sourceId,
                        boolean isEngram,
                        String compositionId,
                        String gameId,
                        String dlcId) {
        super(uuid, name, imageFile, quantity, sourceId, isEngram, compositionId, gameId);
        this.dlcId = dlcId;
    }

    public String getDlcId() {
        return dlcId;
    }
}