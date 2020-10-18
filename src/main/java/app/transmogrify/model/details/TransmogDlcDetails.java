package app.transmogrify.model.details;

import app.transmogrify.model.json.JsonDlc;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

import static util.Constants.*;

public class TransmogDlcDetails extends TransmogDetails {
    private final Boolean totalConversion;

    @JsonCreator
    public TransmogDlcDetails(@JsonProperty(cUuid) String uuid,
                              @JsonProperty(cName) String name,
                              @JsonProperty(cDescription) String description,
                              @JsonProperty(cFilePath) String filePath,
                              @JsonProperty(cLogoFile) String logoFile,
                              @JsonProperty(cFolderFile) String folderFile,
                              @JsonProperty(cBackFolderFile) String backFolderFile,
                              @JsonProperty(cLastUpdated) Date lastUpdated,
                              @JsonProperty(cTotalConversion) boolean totalConversion) {
        super(uuid, name, description, filePath, logoFile, folderFile, backFolderFile, lastUpdated);
        this.totalConversion = totalConversion;
    }

    public static TransmogDlcDetails with(JsonDlc jsonDlc) {
        TransmogDetails details = TransmogDetails.createFrom(jsonDlc);

        String uuid = details.getUuid();
        String name = details.getName();
        String description = details.getDescription();
        String filePath = details.getFilePath();
        String logoFile = details.getLogoFile();
        String folderFile = details.getFolderFile();
        String backFolderFile = details.getBackFolderFile();
        boolean totalConversion = isTotalConversion(jsonDlc.type);

        return new TransmogDlcDetails(uuid,
                name,
                description,
                filePath,
                logoFile,
                folderFile,
                backFolderFile,
                new Date(),
                totalConversion);
    }

    private static boolean isTotalConversion(String type) {
        return type.equals(cDlcTypeTotalConversion);
    }

    public Boolean isTotalConversion() {
        return totalConversion;
    }
}