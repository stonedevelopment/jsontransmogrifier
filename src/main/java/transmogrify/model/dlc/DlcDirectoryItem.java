package transmogrify.model.dlc;

import transmogrify.model.primary.DirectoryItem;

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

    public DlcDirectoryItem(String uuid, String name, String imageFile, String parentId, String sourceId, String gameId, String dlcId) {
        super(uuid, name, imageFile, parentId, sourceId, gameId);
        this.dlcId = dlcId;
    }

    public String getDlcId() {
        return dlcId;
    }
}
