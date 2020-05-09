package transmogrify.model.type;

import java.util.Date;

public class GameType {
    private final String uuid;
    private final String name;
    private final String description;
    private final String filePath;
    private final String logoFile;
    private final String folderFile;
    private final String backFolderFile;
    private final Date lastUpdated;

    public GameType(String uuid, String name, String description, String filePath, String logoFile, String folderFile, String backFolderFile) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.filePath = filePath;
        this.logoFile = logoFile;
        this.folderFile = folderFile;
        this.backFolderFile = backFolderFile;
        this.lastUpdated = new Date();
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getLogoFile() {
        return logoFile;
    }

    public String getFolderFile() {
        return folderFile;
    }

    public String getBackFolderFile() {
        return backFolderFile;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }
}
