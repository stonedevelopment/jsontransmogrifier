package app.illuminate.model.details;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.details.Details;

import static util.Constants.*;

public class DlcIlluminateDetails extends IlluminateDetails {
    private final boolean totalConversion;
    private final String gameId;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public DlcIlluminateDetails(@JsonProperty(cUuid) String uuid,
                                @JsonProperty(cName) String name,
                                @JsonProperty(cDescription) String description,
                                @JsonProperty(cFilePath) String filePath,
                                @JsonProperty(cLogoFile) String logoFile,
                                @JsonProperty(cFolderFile) String folderFile,
                                @JsonProperty(cBackFolderFile) String backFolderFile,
                                @JsonProperty(cTotalConversion) Boolean totalConversion,
                                @JsonProperty(cGameId) String gameId) {
        super(uuid, name, description, filePath, logoFile, folderFile, backFolderFile);
        this.totalConversion = totalConversion;
        this.gameId = gameId;
    }

    public static DlcIlluminateDetails fromJson(JsonNode jsonNode) {
        return new ObjectMapper().convertValue(jsonNode, DlcIlluminateDetails.class);
    }

    public boolean isTotalConversion() {
        return totalConversion;
    }

    @JsonIgnore
    public String getGameId() {
        return gameId;
    }
}