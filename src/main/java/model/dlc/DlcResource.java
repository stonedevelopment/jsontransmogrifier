package model.dlc;

import model.primary.Resource;

import java.util.Date;

public class DlcResource extends Resource {
    private final String dlcId;

    public DlcResource(String uuid, String name, String description, String imageFile, Date lastUpdated, String gameId,
                       String dlcId) {
        super(uuid, name, description, imageFile, lastUpdated, gameId);
        this.dlcId = dlcId;
    }

    public String getDlcId() {
        return dlcId;
    }
}
