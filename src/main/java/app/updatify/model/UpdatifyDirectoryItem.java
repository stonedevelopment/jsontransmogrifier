package app.updatify.model;

import model.DirectoryItem;
import model.Engram;
import model.Folder;
import model.Station;

import java.util.UUID;

import static util.Constants.cFolderFileName;

public class UpdatifyDirectoryItem extends DirectoryItem {
    public UpdatifyDirectoryItem(String uuid, String name, String imageFile, int viewType, String parentId, String sourceId) {
        super(uuid, name, imageFile, viewType, parentId, sourceId);
    }

    public static DirectoryItem createFromStation(Station station, String parentId) {
        return new DirectoryItem(UUID.randomUUID().toString(),
                station.getName(),
                station.getImageFile(),
                0,
                parentId,
                station.getSourceId());
    }

    public static DirectoryItem createFromFolder(Folder folder, String parentId) {
        return new DirectoryItem(UUID.randomUUID().toString(),
                folder.getName(),
                cFolderFileName,
                1,
                parentId,
                folder.getUuid());
    }

    public static DirectoryItem createFromEngram(Engram engram, String parentId) {
        return new DirectoryItem(UUID.randomUUID().toString(),
                engram.getName(),
                engram.getImageFile(),
                2,
                parentId,
                engram.getUuid());
    }
}
