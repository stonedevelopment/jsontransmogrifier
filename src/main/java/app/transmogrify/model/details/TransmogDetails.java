package app.transmogrify.model.details;

import app.transmogrify.model.json.JsonDlc;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.nashorn.internal.ir.annotations.Ignore;
import model.details.Details;

import java.util.UUID;

import static util.Constants.*;

public class TransmogDetails extends Details {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public TransmogDetails(@JsonProperty(cUuid) String uuid,
                           @JsonProperty(cName) String name,
                           @JsonProperty(cDescription) String description,
                           @JsonProperty(cFilePath) String filePath,
                           @JsonProperty(cLogoFileName) String logoFile,
                           @JsonProperty(cFolderFileName) String folderFile,
                           @JsonProperty(cBackFolderFileName) String backFolderFile) {
        super(uuid, name, description, filePath, logoFile, folderFile, backFolderFile);
    }

    @JsonIgnore
    public static TransmogDetails from(JsonDlc jsonDlc) {
        String uuid = UUID.randomUUID().toString();
        String name = jsonDlc.name;
        String description = jsonDlc.description;
        String filePath = jsonDlc.filePath;
        return new TransmogDetails(uuid, name, description, filePath, cLogoFileName, cFolderFileName, cBackFolderFileName);
    }

    @Ignore
    public String getTransmogFile() {
        return getFilePath().concat(cTransmogrifiedFileName);
    }
}
