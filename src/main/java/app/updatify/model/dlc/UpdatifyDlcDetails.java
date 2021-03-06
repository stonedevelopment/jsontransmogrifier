package app.updatify.model.dlc;

import app.illuminate.model.details.IlluminateDlcDetails;
import app.updatify.model.UpdatifyDetails;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

import static util.Constants.*;

public class UpdatifyDlcDetails extends UpdatifyDetails {
    private final Boolean totalConversion;
    private final String gameId;

    @JsonCreator
    public UpdatifyDlcDetails(@JsonProperty(cUuid) String uuid,
                              @JsonProperty(cName) String name,
                              @JsonProperty(cDescription) String description,
                              @JsonProperty(cFilePath) String filePath,
                              @JsonProperty(cLogoFile) String logoFile,
                              @JsonProperty(cFolderFile) String folderFile,
                              @JsonProperty(cBackFolderFile) String backFolderFile,
                              @JsonProperty(cLastUpdated) Date lastUpdated,
                              @JsonProperty(cTotalConversion) boolean totalConversion,
                              @JsonProperty(cGameId) String gameId) {
        super(uuid, name, description, filePath, logoFile, folderFile, backFolderFile, lastUpdated);
        this.totalConversion = totalConversion;
        this.gameId = gameId;
    }

    public static UpdatifyDlcDetails convertToNew(UpdatifyDlcDetails oldDetails, IlluminateDlcDetails newDetails, String gameId) {
        return new UpdatifyDlcDetails(oldDetails.getUuid(),
                newDetails.getName(),
                newDetails.getDescription(),
                newDetails.getFilePath(),
                newDetails.getLogoFile(),
                newDetails.getFolderFile(),
                newDetails.getBackFolderFile(),
                new Date(),
                newDetails.isTotalConversion(),
                gameId);
    }

    public static UpdatifyDlcDetails convertFrom(UpdatifyDlcDetails oldDetails, String gameId) {
        return new UpdatifyDlcDetails(oldDetails.getUuid(),
                oldDetails.getName(),
                oldDetails.getDescription(),
                oldDetails.getFilePath(),
                oldDetails.getLogoFile(),
                oldDetails.getFolderFile(),
                oldDetails.getBackFolderFile(),
                oldDetails.getLastUpdated(),
                oldDetails.isTotalConversion(),
                gameId);
    }

    public static UpdatifyDlcDetails fromJson(JsonNode jsonNode) {
        return new ObjectMapper().convertValue(jsonNode, UpdatifyDlcDetails.class);
    }

    public Boolean isTotalConversion() {
        return totalConversion;
    }

    public String getGameId() {
        return gameId;
    }

    public boolean equals(IlluminateDlcDetails details) {
        return super.equals(details) &&
                isTotalConversion() == details.isTotalConversion();
    }
}