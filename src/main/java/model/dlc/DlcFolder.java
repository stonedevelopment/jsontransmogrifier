package model.dlc;

import model.Folder;

public class DlcFolder extends Folder {
    private final String dlcId;

    public DlcFolder(String uuid, String name, String gameId, String dlcId) {
        super(uuid, name, gameId);
        this.dlcId = dlcId;
    }

    public String getDlcId() {
        return dlcId;
    }
}