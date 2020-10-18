package app.illuminate.model.details;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

import static util.Constants.*;

public class IlluminateDlcDetails extends IlluminateDetails {
    private final boolean totalConversion;
    private final String gameId;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public IlluminateDlcDetails(@JsonProperty(cUuid) String uuid,
                                @JsonProperty(cName) String name,
                                @JsonProperty(cDescription) String description,
                                @JsonProperty(cFilePath) String filePath,
                                @JsonProperty(cLogoFile) String logoFile,
                                @JsonProperty(cFolderFile) String folderFile,
                                @JsonProperty(cBackFolderFile) String backFolderFile,
                                @JsonProperty(cLastUpdated) Date lastUpdated,
                                @JsonProperty(cTotalConversion) Boolean totalConversion,
                                @JsonProperty(cGameId) String gameId) {
        super(uuid, name, description, filePath, logoFile, folderFile, backFolderFile, lastUpdated);
        this.totalConversion = totalConversion;
        this.gameId = gameId;
    }

    public static IlluminateDlcDetails fromJson(JsonNode jsonNode) {
        return new ObjectMapper().convertValue(jsonNode, IlluminateDlcDetails.class);
    }

    public boolean isTotalConversion() {
        return totalConversion;
    }

    @JsonIgnore
    public String getGameId() {
        return gameId;
    }
}