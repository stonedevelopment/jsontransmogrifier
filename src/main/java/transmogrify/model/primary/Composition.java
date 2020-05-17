package transmogrify.model.primary;

import java.util.Date;
import java.util.List;

public class Composition {
    private final String uuid;
    private final String engramId;
    private final List<Composite> compositeList;
    private final Date lastUpdated;
    private final String gameId;

    public Composition(String uuid, String engramId, List<Composite> compositeList, Date lastUpdated, String gameId) {
        this.uuid = uuid;
        this.engramId = engramId;
        this.compositeList = compositeList;
        this.lastUpdated = lastUpdated;
        this.gameId = gameId;
    }

    public String getUuid() {
        return uuid;
    }

    public String getEngramId() {
        return engramId;
    }

    public List<Composite> getCompositeList() {
        return compositeList;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public String getGameId() {
        return gameId;
    }
}
