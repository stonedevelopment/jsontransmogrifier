package transmogrify.model.dlc;

import transmogrify.model.primary.Composition;

import java.util.Date;

public class DlcComposition extends Composition {
    private final String dlcId;

    public DlcComposition(String uuid, String engramId, Date lastUpdated,
                          String gameId, String dlcId) {
        super(uuid, engramId, lastUpdated, gameId);
        this.dlcId = dlcId;
    }

    public String getDlcId() {
        return dlcId;
    }
}