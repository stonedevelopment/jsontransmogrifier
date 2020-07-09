package app.illuminate.model.details;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DlcIlluminateDetails extends IlluminateDetails {
    private final boolean totalConversion;
    private final String gameId;

    public DlcIlluminateDetails(String uuid, String name, String description, String filePath, String logoFile,
                                String folderFile, String backFolderFile, String transmogFile, Boolean totalConversion,
                                String gameId) {
        super(uuid, name, description, filePath, logoFile, folderFile, backFolderFile, transmogFile);
        this.totalConversion = totalConversion;
        this.gameId = gameId;
    }

    public static DlcIlluminateDetails from(JsonNode jsonNode) {
        return new ObjectMapper().convertValue(jsonNode, DlcIlluminateDetails.class);
    }

    public boolean isTotalConversion() {
        return totalConversion;
    }

    public String getGameId() {
        return gameId;
    }
}