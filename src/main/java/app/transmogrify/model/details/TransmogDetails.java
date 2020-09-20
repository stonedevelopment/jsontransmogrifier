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

    @JsonCreator
    public TransmogDetails(@JsonProperty(cUuid) String uuid,
                           @JsonProperty(cName) String name,
                           @JsonProperty(cDescription) String description,
                           @JsonProperty(cFilePath) String filePath,
                           @JsonProperty(cLogoFile) String logoFile,
                           @JsonProperty(cFolderFile) String folderFile,
                           @JsonProperty(cBackFolderFile) String backFolderFile) {
        super(uuid, name, description, filePath, logoFile, folderFile, backFolderFile);
    }

    public static TransmogDetails with(JsonDlc jsonDlc) {
        String uuid = UUID.randomUUID().toString();
        String name = jsonDlc.name;
        String description = jsonDlc.description;
        String filePath = jsonDlc.filePath.concat(cTransmogrifiedFileName);
        return new TransmogDetails(uuid,
                name,
                description,
                filePath,
                cLogoFileName,
                cFolderFileName,
                cBackFolderFileName);
    }

    public static TransmogDetails from(JsonNode jsonNode) {
        return new ObjectMapper().convertValue(jsonNode, TransmogDetails.class);
    }
}
