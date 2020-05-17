package transmogrify.model.dlc;

import transmogrify.model.primary.Engram;

import java.util.Date;

public class DlcEngram extends Engram {
    private final String dlcId;

    public DlcEngram(String uuid, String name, String description, String imageFile, int level, int yield, int points,
                     int xp, int craftingTime, Date lastUpdated, String gameId, String dlcId) {
        super(uuid, name, description, imageFile, level, yield, points, xp, craftingTime, lastUpdated, gameId);
        this.dlcId = dlcId;
    }

    public String getDlcId() {
        return dlcId;
    }
}