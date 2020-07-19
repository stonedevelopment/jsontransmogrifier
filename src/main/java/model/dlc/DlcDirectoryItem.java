package model.dlc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.DirectoryItem;

import static util.Constants.*;

/**
 * "uuid": "",
 * "stationId": "67553474-6941-4c64-88d0-d4b4eb7d29fb",
 * "name": "Self",
 * "imageFile": "self.webp",
 * "gameId": "",
 * "engrams": [],
 * "folders": []
 */
public class DlcDirectoryItem extends DirectoryItem {
    private final String dlcId;

    @JsonCreator
    public DlcDirectoryItem(@JsonProperty(cUuid) String uuid,
                            @JsonProperty(cName) String name,
                            @JsonProperty(cImageFile) String imageFile,
                            @JsonProperty(cViewType) int viewType,
                            @JsonProperty(cParentId) String parentId,
                            @JsonProperty(cSourceId) String sourceId,
                            @JsonProperty(cGameId) String gameId,
                            @JsonProperty(cDlcId) String dlcId) {
        super(uuid, name, imageFile, viewType, parentId, sourceId, gameId);
        this.dlcId = dlcId;
    }

    public static DlcDirectoryItem fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, DlcDirectoryItem.class);
    }

    public String getDlcId() {
        return dlcId;
    }
}
