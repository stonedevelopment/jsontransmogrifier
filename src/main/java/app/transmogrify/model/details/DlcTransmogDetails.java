package app.transmogrify.model.details;

import app.transmogrify.model.game_data.PrimaryTransmogGameData;
import app.transmogrify.model.json.JsonDlc;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static util.Constants.*;

public class DlcTransmogDetails extends TransmogDetails {
    private final Boolean totalConversion;
    private final String gameId;

    @JsonCreator
    public DlcTransmogDetails(@JsonProperty(cUuid) String uuid,
                              @JsonProperty(cName) String name,
                              @JsonProperty(cDescription) String description,
                              @JsonProperty(cFilePath) String filePath,
                              @JsonProperty(cLogoFile) String logoFile,
                              @JsonProperty(cFolderFile) String folderFile,
                              @JsonProperty(cBackFolderFile) String backFolderFile,
                              @JsonProperty(cTransmogFile) String transmogFile,
                              @JsonProperty(cTotalConversion) boolean totalConversion,
                              @JsonProperty(cGameId) String gameId) {
        super(uuid, name, description, filePath, logoFile, folderFile, backFolderFile, transmogFile);
        this.totalConversion = totalConversion;
        this.gameId = gameId;
    }

    public static DlcTransmogDetails with(JsonDlc jsonDlc, PrimaryTransmogGameData primaryGameData) {
        TransmogDetails details = TransmogDetails.with(jsonDlc);

        String uuid = details.getUuid();
        String name = details.getName();
        String description = details.getDescription();
        String filePath = details.getFilePath();
        String logoFile = details.getLogoFile();
        String folderFile = details.getFolderFile();
        String backFolderFile = details.getBackFolderFile();
        String transmogFile = details.getTransmogFile();
        boolean totalConversion = isTotalConversion(jsonDlc.type);
        String gameId = primaryGameData.getUuid();

        return new DlcTransmogDetails(uuid, name, description, filePath, logoFile, folderFile, backFolderFile,
                transmogFile, totalConversion, gameId);
    }

    private static boolean isTotalConversion(String type) {
        return type.equals(cDlcTypeTotalConversion);
    }

    public Boolean getTotalConversion() {
        return totalConversion;
    }

    public String getGameId() {
        return gameId;
    }
}