package model.details;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.ir.annotations.Ignore;

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

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Details(@JsonProperty(cUuid) String uuid,
                   @JsonProperty(cName) String name,
                   @JsonProperty(cDescription) String description,
                   @JsonProperty(cFilePath) String filePath,
                   @JsonProperty(cLogoFileName) String logoFile,
                   @JsonProperty(cFolderFileName) String folderFile,
                   @JsonProperty(cBackFolderFileName) String backFolderFile) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.filePath = filePath;
        this.logoFile = logoFile;
        this.folderFile = folderFile;
        this.backFolderFile = backFolderFile;
        this.lastUpdated = new Date();
    }

    @JsonIgnore
    public static Details from(JsonNode jsonNode) {
        try {
            return new ObjectMapper().treeToValue(jsonNode, Details.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
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
