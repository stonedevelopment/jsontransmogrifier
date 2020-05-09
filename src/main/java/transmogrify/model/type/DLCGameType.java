package transmogrify.model.type;

public class DLCGameType extends GameType {
    private final Boolean totalConversion;

    public DLCGameType(String uuid, String name, String description, Boolean totalConversion, String filePath, String logoFile, String folderFile, String backFolderFile) {
        super(uuid, name, description, filePath, logoFile, folderFile, backFolderFile);
        this.totalConversion = totalConversion;
    }

    public Boolean isTotalConversion() {
        return totalConversion;
    }
}
