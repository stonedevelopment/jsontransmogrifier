package app.updatify.model;

import app.illuminate.model.details.IlluminateDlcDetails;
import app.transmogrify.model.details.TransmogDlcDetails;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static util.Constants.*;

public class UpdatifyDlcDetails extends UpdatifyDetails {
    private final Boolean totalConversion;

    @JsonCreator
    public UpdatifyDlcDetails(@JsonProperty(cUuid) String uuid,
                              @JsonProperty(cName) String name,
                              @JsonProperty(cDescription) String description,
                              @JsonProperty(cFilePath) String filePath,
                              @JsonProperty(cLogoFile) String logoFile,
                              @JsonProperty(cFolderFile) String folderFile,
                              @JsonProperty(cBackFolderFile) String backFolderFile,
                              @JsonProperty(cTotalConversion) boolean totalConversion) {
        super(uuid, name, description, filePath, logoFile, folderFile, backFolderFile);
        this.totalConversion = totalConversion;
    }

    public static UpdatifyDlcDetails convertToNew(TransmogDlcDetails oldDetails, TransmogDlcDetails newDetails) {
        return new UpdatifyDlcDetails(oldDetails.getUuid(),
                newDetails.getName(),
                newDetails.getDescription(),
                newDetails.getFilePath(),
                newDetails.getLogoFile(),
                newDetails.getFolderFile(),
                newDetails.getBackFolderFile(),
                newDetails.isTotalConversion());
    }

    public static UpdatifyDlcDetails fromJson(JsonNode jsonNode) {
        return new ObjectMapper().convertValue(jsonNode, UpdatifyDlcDetails.class);
    }

    public Boolean isTotalConversion() {
        return totalConversion;
    }

    @JsonIgnore
    public String getUpdatifiedFilePath() {
        return getFilePath().concat(cUpdatifiedFileName);
    }

    public boolean equals(IlluminateDlcDetails details) {
        return super.equals(details) &&
                isTotalConversion() == details.isTotalConversion();
    }
}