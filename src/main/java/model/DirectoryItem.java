package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static util.Constants.*;

/**
 * "uuid": "",
 * "stationId": "67553474-6941-4c64-88d0-d4b4eb7d29fb",
 * "name": "Self",
 * "imageFile": "self.webp",
 * "viewType: 0,
 * "gameId": "",
 * "engrams": [],
 * "folders": []
 */
public class DirectoryItem {
    private final String uuid;
    private final String name;
    private final String imageFile;
    private final int viewType;
    private final String parentId;
    private final String sourceId;
    private final String gameId;

    @JsonCreator
    public DirectoryItem(@JsonProperty(cUuid) String uuid,
                         @JsonProperty(cName) String name,
                         @JsonProperty(cImageFile) String imageFile,
                         @JsonProperty(cViewType) int viewType,
                         @JsonProperty(cParentId) String parentId,
                         @JsonProperty(cSourceId) String sourceId,
                         @JsonProperty(cGameId) String gameId) {
        this.uuid = uuid;
        this.name = name;
        this.imageFile = imageFile;
        this.viewType = viewType;
        this.parentId = parentId;
        this.sourceId = sourceId;
        this.gameId = gameId;
    }

    public static DirectoryItem fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, DirectoryItem.class);
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getImageFile() {
        return imageFile;
    }

    public int getViewType() {
        return viewType;
    }

    public String getParentId() {
        return parentId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getGameId() {
        return gameId;
    }
}