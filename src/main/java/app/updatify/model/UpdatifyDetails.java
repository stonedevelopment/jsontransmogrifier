package app.updatify.model;

import app.illuminate.model.details.IlluminateDetails;
import app.transmogrify.model.details.TransmogDetails;
import model.details.Details;

public class UpdatifyDetails extends Details {

    public UpdatifyDetails(String uuid, String name, String description, String filePath, String logoFile, String folderFile, String backFolderFile) {
        super(uuid, name, description, filePath, logoFile, folderFile, backFolderFile);
    }

    public static Details convertToNew(TransmogDetails oldDetails, IlluminateDetails newDetails) {
        return new Details(oldDetails.getUuid(),
                newDetails.getName(),
                newDetails.getDescription(),
                newDetails.getFilePath(),
                newDetails.getLogoFile(),
                newDetails.getFolderFile(),
                newDetails.getBackFolderFile());
    }
}