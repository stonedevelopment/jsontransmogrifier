package model.dlc;

import model.Station;

import java.util.Date;

public class DlcStation extends Station {
    private final String dlcId;

    public DlcStation(String uuid, String name, String imageFile, String engramId, Date lastUpdated,
                      String gameId, String dlcId) {
        super(uuid, name, imageFile, engramId, lastUpdated, gameId);
        this.dlcId = dlcId;
    }

    public String getDlcId() {
        return dlcId;
    }
}
