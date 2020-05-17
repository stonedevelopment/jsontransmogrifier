package transmogrify.model.dlc;

import transmogrify.model.primary.Composite;
import transmogrify.model.primary.Composition;

import java.util.Date;
import java.util.List;

public class DlcComposition extends Composition {
    private final String dlcId;

    public DlcComposition(String uuid, String engramId, List<Composite> compositeList, Date lastUpdated,
                          String gameId, String dlcId) {
        super(uuid, engramId, compositeList, lastUpdated, gameId);
        this.dlcId = dlcId;
    }

    public String getDlcId() {
        return dlcId;
    }
}