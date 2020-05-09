package transmogrify.model.type;

import java.util.Date;

public class PrimaryGameType extends GameType {
    public PrimaryGameType(String uuid, String name, String description, String filePath, String logoFile, String folderFile, String backFolderFile) {
        super(uuid, name, description, filePath, logoFile, folderFile, backFolderFile);
    }
}
