package app.updatify.model;

import model.DirectoryItem;
import model.Engram;
import model.Folder;
import model.Station;

import java.util.UUID;

import static util.Constants.cFolderFileName;

public class UpdatifyDirectoryItem extends DirectoryItem {
    private final String gameId;

    public UpdatifyDirectoryItem(String uuid, String name, String imageFile, int viewType, String parentId, String sourceId, String gameId) {
        super(uuid, name, imageFile, viewType, parentId, sourceId);
        this.gameId = gameId;
    }

    public static UpdatifyDirectoryItem createFromStation(Station station, String gameId) {
        return new UpdatifyDirectoryItem(UUID.randomUUID().toString(),
                station.getName(),
                station.getImageFile(),
                0,
                null,
                station.getSourceId(),
                gameId);
    }

    public static UpdatifyDirectoryItem createFromFolder(Folder folder, String parentId, String gameId) {
        return new UpdatifyDirectoryItem(UUID.randomUUID().toString(),
                folder.getName(),
                cFolderFileName,
                1,
                parentId,
                folder.getUuid(),
                gameId);
    }

    public static UpdatifyDirectoryItem createFromEngram(Engram engram, String parentId, String gameId) {
        return new UpdatifyDirectoryItem(UUID.randomUUID().toString(),
                engram.getName(),
                engram.getImageFile(),
                2,
                parentId,
                engram.getUuid(),
                gameId);
    }

    public static UpdatifyDirectoryItem with(DirectoryItem directoryItem, String gameId) {
        return new UpdatifyDirectoryItem(directoryItem.getUuid(),
                directoryItem.getName(),
                directoryItem.getImageFile(),
                directoryItem.getViewType(),
                directoryItem.getParentId(),
                directoryItem.getSourceId(),
                gameId);
    }

    public String getGameId() {
        return gameId;
    }
}
