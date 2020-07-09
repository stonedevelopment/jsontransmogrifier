package app.illuminate.model.details;

import app.transmogrify.model.details.TransmogDetails;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static util.Constants.*;

public class IlluminateDetails extends TransmogDetails {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public IlluminateDetails(@JsonProperty(cUuid) String uuid,
                             @JsonProperty(cName) String name,
                             @JsonProperty(cDescription) String description,
                             @JsonProperty(cFilePath) String filePath,
                             @JsonProperty(cLogoFile) String logoFile,
                             @JsonProperty(cFolderFile) String folderFile,
                             @JsonProperty(cBackFolderFile) String backFolderFile,
                             @JsonProperty(cTransmogFile) String transmogFile) {
        super(uuid, name, description, filePath, logoFile, folderFile, backFolderFile, transmogFile);
    }

    public static IlluminateDetails from(JsonNode jsonNode) {
        return new ObjectMapper().convertValue(jsonNode, IlluminateDetails.class);
    }

    public String buildTransmogFilePath() {
        return getFilePath().concat(getTransmogFile());
    }
}