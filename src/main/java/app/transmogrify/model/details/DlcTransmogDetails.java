package app.transmogrify.model.details;

import app.transmogrify.model.game_data.PrimaryTransmogGameData;
import app.transmogrify.model.json.JsonDlc;

import static util.Constants.*;

public class DlcTransmogDetails extends TransmogDetails {
    private final Boolean totalConversion;
    private final String gameId;

    public DlcTransmogDetails(String uuid, String name, String description, boolean totalConversion, String filePath,
                              String logoFile, String folderFile, String backFolderFile, String gameId) {
        super(uuid, name, description, filePath, logoFile, folderFile, backFolderFile, cTransmogrifiedFileName);
        this.totalConversion = totalConversion;
        this.gameId = gameId;
    }

    public static DlcTransmogDetails from(JsonDlc jsonDlc, PrimaryTransmogGameData primaryGameData) {
        TransmogDetails details = TransmogDetails.from(jsonDlc);
        boolean totalConversion = isTotalConversion(jsonDlc.type);
        String gameId = primaryGameData.getUuid();
        return new DlcTransmogDetails(details.getUuid(), details.getName(), details.getDescription(), totalConversion,
                details.getFilePath(), cLogoFileName, cFolderFileName, cBackFolderFileName, gameId);

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
