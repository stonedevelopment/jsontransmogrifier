package app.transmogrify.model.details;

import app.transmogrify.model.json.JsonDlc;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.details.Details;

import java.util.UUID;

import static util.Constants.*;

public class TransmogDetails extends Details {
    private final String transmogFile;

    @JsonCreator
    public TransmogDetails(@JsonProperty(cUuid) String uuid,
                           @JsonProperty(cName) String name,
                           @JsonProperty(cDescription) String description,
                           @JsonProperty(cFilePath) String filePath,
                           @JsonProperty(cLogoFile) String logoFile,
                           @JsonProperty(cFolderFile) String folderFile,
                           @JsonProperty(cBackFolderFile) String backFolderFile,
                           @JsonProperty(cTransmogFile) String transmogFile) {
        super(uuid, name, description, filePath, logoFile, folderFile, backFolderFile);
        this.transmogFile = transmogFile;
    }

    public static TransmogDetails with(JsonDlc jsonDlc) {
        String uuid = UUID.randomUUID().toString();
        String name = jsonDlc.name;
        String description = jsonDlc.description;
        String filePath = jsonDlc.filePath;
        return new TransmogDetails(uuid, name, description, filePath, cLogoFileName, cFolderFileName,
                cBackFolderFileName, cTransmogrifiedFileName);
    }

    public static TransmogDetails from(JsonNode jsonNode) {
        return new ObjectMapper().convertValue(jsonNode, TransmogDetails.class);
    }

    public String getTransmogFile() {
        return transmogFile;
    }
}
