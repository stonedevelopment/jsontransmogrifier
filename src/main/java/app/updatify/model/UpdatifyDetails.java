package app.updatify.model;

import app.illuminate.model.details.IlluminateDetails;
import app.transmogrify.model.details.TransmogDetails;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.details.Details;

import static util.Constants.*;

public class UpdatifyDetails extends Details {

    @JsonCreator
    public UpdatifyDetails(@JsonProperty(cUuid) String uuid,
                           @JsonProperty(cName) String name,
                           @JsonProperty(cDescription) String description,
                           @JsonProperty(cFilePath) String filePath,
                           @JsonProperty(cLogoFile) String logoFile,
                           @JsonProperty(cFolderFile) String folderFile,
                           @JsonProperty(cBackFolderFile) String backFolderFile) {
        super(uuid, name, description, filePath, logoFile, folderFile, backFolderFile);
    }

    public static UpdatifyDetails convertToNew(TransmogDetails oldDetails, IlluminateDetails newDetails) {
        return new UpdatifyDetails(oldDetails.getUuid(),
                newDetails.getName(),
                newDetails.getDescription(),
                newDetails.getFilePath(),
                newDetails.getLogoFile(),
                newDetails.getFolderFile(),
                newDetails.getBackFolderFile());
    }

    public String getUpdatifiedFilePath() {
        return getFilePath().concat(cUpdatifiedFileName);
    }
}