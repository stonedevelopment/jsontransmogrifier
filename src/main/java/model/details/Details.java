package model.details;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

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
                   @JsonProperty(cBackFolderFile) String backFolderFile,
                   @JsonProperty(cLastUpdated) Date lastUpdated) {
        this.uuid = uuid;
        this.name = name;
        this.description = description == null ? "" : description;
        this.filePath = filePath;
        this.logoFile = logoFile;
        this.folderFile = folderFile;
        this.backFolderFile = backFolderFile;
        this.lastUpdated = lastUpdated;
    }

    public static Details fromJson(JsonNode jsonNode) {
        return new ObjectMapper().convertValue(jsonNode, Details.class);
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

    public boolean equals(Details details) {
        return name.equals(details.name) &&
                description.equals(details.description) &&
                filePath.equals(details.filePath) &&
                logoFile.equals(details.logoFile) &&
                folderFile.equals(details.folderFile) &&
                backFolderFile.equals(details.backFolderFile);
    }
}
