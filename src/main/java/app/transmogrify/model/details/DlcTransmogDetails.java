package app.transmogrify.model.details;

import app.transmogrify.model.json.JsonDlc;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static util.Constants.*;

public class DlcTransmogDetails extends TransmogDetails {
    private final Boolean totalConversion;

    @JsonCreator
    public DlcTransmogDetails(@JsonProperty(cUuid) String uuid,
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

    public static DlcTransmogDetails with(JsonDlc jsonDlc) {
        TransmogDetails details = TransmogDetails.with(jsonDlc);

        String uuid = details.getUuid();
        String name = details.getName();
        String description = details.getDescription();
        String filePath = details.getFilePath();
        String logoFile = details.getLogoFile();
        String folderFile = details.getFolderFile();
        String backFolderFile = details.getBackFolderFile();
        boolean totalConversion = isTotalConversion(jsonDlc.type);

        return new DlcTransmogDetails(uuid,
                name,
                description,
                filePath,
                logoFile,
                folderFile,
                backFolderFile,
                totalConversion);
    }

    private static boolean isTotalConversion(String type) {
        return type.equals(cDlcTypeTotalConversion);
    }

    public Boolean getTotalConversion() {
        return totalConversion;
    }
}