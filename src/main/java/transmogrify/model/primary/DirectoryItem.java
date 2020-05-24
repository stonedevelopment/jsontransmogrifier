package transmogrify.model.primary;

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

    public DirectoryItem(String uuid, String name, String imageFile, int viewType, String parentId, String sourceId, String gameId) {
        this.uuid = uuid;
        this.name = name;
        this.imageFile = imageFile;
        this.viewType = viewType;
        this.parentId = parentId;
        this.sourceId = sourceId;
        this.gameId = gameId;
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