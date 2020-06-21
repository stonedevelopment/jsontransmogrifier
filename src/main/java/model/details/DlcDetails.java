package model.details;

import model.details.Details;

public class DlcDetails extends Details {
    private final Boolean totalConversion;
    private final String gameId;

    public DlcDetails(String uuid, String name, String description, Boolean totalConversion, String filePath,
                      String logoFile, String folderFile, String backFolderFile, String gameId) {
        super(uuid, name, description, filePath, logoFile, folderFile, backFolderFile);
        this.totalConversion = totalConversion;
        this.gameId = gameId;
    }

    public Boolean isTotalConversion() {
        return totalConversion;
    }

    public String getGameId() {
        return gameId;
    }
}