package model.details;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Objects;

import static util.Constants.*;

public class Details {
    private final String uuid;
    private final String name;
    private final String description;
    private final String filePath;
    private final String logoFile;
    private final String folderFile;
    private final String backFolderFile;
    private final Date lastUpdated;

    @JsonCreator
    public Details(@JsonProperty(cUuid) String uuid,
                   @JsonProperty(cName) String name,
                   @JsonProperty(cDescription) String description,
                   @JsonProperty(cFilePath) String filePath,
                   @JsonProperty(cLogoFile) String logoFile,
                   @JsonProperty(cFolderFile) String folderFile,
                   @JsonProperty(cBackFolderFile) String backFolderFile) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Details details = (Details) o;
        return name.equals(details.name) &&
                description.equals(details.description) &&
                filePath.equals(details.filePath) &&
                logoFile.equals(details.logoFile) &&
                folderFile.equals(details.folderFile) &&
                backFolderFile.equals(details.backFolderFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, filePath, logoFile, folderFile, backFolderFile);
    }
}
