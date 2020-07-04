package app.illuminate.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import controller.GameData;
import model.Engram;
import model.Folder;
import model.Station;

public class IlluminateGameData extends GameData {
    public IlluminateGameData(JsonNode inObject) throws JsonProcessingException {
        super(inObject);
    }

    @Override
    protected void createDetailsObject() {

    }

    @Override
    protected void mapGameDataFromJson() {

    }

    @Override
    protected void mapFoldersFromJson() {

    }

    @Override
    protected void mapResourcesFromJson() {

    }

    @Override
    protected void mapEngramsFromJson() {

    }

    @Override
    protected void mapStationsFromJson() {

    }

    @Override
    protected void mapCompositionFromJson() {

    }

    @Override
    protected void mapDirectoryFromJson() {

    }

    @Override
    protected void mapStationDirectoryItem(Station station) {

    }

    @Override
    protected void mapEngramDirectoryItem(Engram engram, String parentId) {

    }

    @Override
    public JsonNode resolveToJson() {
        return null;
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
