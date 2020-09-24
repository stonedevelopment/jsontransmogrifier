package app.updatify.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public static UpdatifyDetails convertToNew(Details oldDetails, Details newDetails) {
        return new UpdatifyDetails(oldDetails.getUuid(),
                newDetails.getName(),
                newDetails.getDescription(),
                newDetails.getFilePath(),
                newDetails.getLogoFile(),
                newDetails.getFolderFile(),
                newDetails.getBackFolderFile());
    }

    public static UpdatifyDetails fromJson(JsonNode jsonNode) {
        return new ObjectMapper().convertValue(jsonNode, UpdatifyDetails.class);
    }

    @JsonIgnore
    public String getUpdatifiedFilePath() {
        return getFilePath().concat(cUpdatifiedFileName);
    }
}