package app.illuminate.model.details;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static util.Constants.*;

public class DlcIlluminateDetails extends IlluminateDetails {
    private final boolean totalConversion;

    @JsonCreator
    public DlcIlluminateDetails(@JsonProperty(cUuid) String uuid,
                                @JsonProperty(cName) String name,
                                @JsonProperty(cDescription) String description,
                                @JsonProperty(cFilePath) String filePath,
                                @JsonProperty(cLogoFile) String logoFile,
                                @JsonProperty(cFolderFile) String folderFile,
                                @JsonProperty(cBackFolderFile) String backFolderFile,
                                @JsonProperty(cTransmogFile) String transmogFile,
                                @JsonProperty(cTotalConversion) Boolean totalConversion) {
        super(uuid, name, description, filePath, logoFile, folderFile, backFolderFile, transmogFile);
        this.totalConversion = totalConversion;
    }

    public static DlcIlluminateDetails from(JsonNode jsonNode) {
        return new ObjectMapper().convertValue(jsonNode, DlcIlluminateDetails.class);
    }

    public boolean isTotalConversion() {
        return totalConversion;
    }
}