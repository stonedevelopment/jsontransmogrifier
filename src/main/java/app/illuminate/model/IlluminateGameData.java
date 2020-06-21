package app.illuminate.model;

import model.Folder;
import controller.GameData;

import java.util.Map;
import java.util.TreeMap;

public class IlluminateGameData extends GameData {

    private final Map<String, String> folderIdMap = new TreeMap<>();

    public String getFolderUUIDByName(String name) {
        return folderIdMap.get(name);
    }

    private boolean isFolderUnique(String name) {
        //  test name for uuid
        String uuid = folderIdMap.get(name);
        if (uuid == null) return true;

        //  test uuid for object
        Folder folder = getFolder(uuid);
        if (folder == null) return true;

        //  return if incoming object equals mapped object
        return folder.equals(name);
    }

    private void addFolderToMap(String name, Folder folder) {
        folderIdMap.put(name, folder.getUuid());
        folderMap.put(folder.getUuid(), folder);
    }
}
