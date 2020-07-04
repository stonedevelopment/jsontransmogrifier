package app.transmogrify.model.details;

import app.transmogrify.model.json.JsonDlc;
import model.details.Details;

import java.util.UUID;

import static util.Constants.*;

public class TransmogDetails extends Details {
    public TransmogDetails(String uuid, String name, String description, String filePath, String logoFile, String folderFile, String backFolderFile) {
        super(uuid, name, description, filePath, logoFile, folderFile, backFolderFile);
    }

    public static TransmogDetails from(JsonDlc jsonDlc) {
        String uuid = UUID.randomUUID().toString();
        String name = jsonDlc.name;
        String description = jsonDlc.description;
        String filePath = jsonDlc.filePath;
        return new TransmogDetails(uuid, name, description, filePath, cLogoFileName, cFolderFileName, cBackFolderFileName);
    }

    public String getTransmogFile() {
        return getFilePath().concat(cTransmogrifiedFileName);
    }
}
