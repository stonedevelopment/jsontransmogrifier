package app.updatify.game_data;

import com.fasterxml.jackson.databind.JsonNode;
import controller.GameData;
import model.*;

import static util.Constants.*;

public class UpdatifyGameData extends GameData {
    protected UpdatifyGameData(JsonNode inNode) {
        super(inNode);
    }

    @Override
    protected void createDetailsObject() {
        //  do nothing
    }

    @Override
    public void mapGameDataFromJson() {
        createDetailsObject();
        mapResourcesFromJson();
        mapStationsFromJson();
        mapFoldersFromJson();
        mapEngramsFromJson();
        mapCompositionFromJson();
        mapCompositesFromJson();
        mapDirectoryFromJson();
    }

    @Override
    protected void mapFoldersFromJson() {
        JsonNode jsonArray = inNode.get(cFolders);
        for (JsonNode jsonNode : jsonArray) {
            addFolder(Folder.fromJson(jsonNode));
        }
    }

    @Override
    protected void mapResourcesFromJson() {
        JsonNode jsonArray = inNode.get(cResources);
        for (JsonNode jsonNode : jsonArray) {
            addResource(Resource.fromJson(jsonNode));
        }
    }

    @Override
    protected void mapEngramsFromJson() {
        JsonNode jsonArray = inNode.get(cEngrams);
        for (JsonNode jsonNode : jsonArray) {
            addEngram(Engram.fromJson(jsonNode));
        }
    }

    @Override
    protected void mapStationsFromJson() {
        JsonNode jsonArray = inNode.get(cStations);
        for (JsonNode jsonNode : jsonArray) {
            addStation(Station.fromJson(jsonNode));
        }
    }

    @Override
    protected void addComposition(Composition composition) {
        String name = getEngramNameByUUID(composition.getEngramId());
        addComposition(name, composition);
    }

    @Override
    protected void mapCompositionFromJson() {
        JsonNode jsonArray = inNode.get(cComposition);
        for (JsonNode jsonNode : jsonArray) {
            addComposition(Composition.fromJson(jsonNode));
        }
    }

    protected void mapCompositesFromJson() {
        JsonNode jsonArray = inNode.get(cComposites);
        for (JsonNode jsonNode : jsonArray) {
            addComposite(Composite.fromJson(jsonNode));
        }
    }

    @Override
    protected void mapDirectoryFromJson() {
        JsonNode jsonArray = inNode.get(cDirectory);
        for (JsonNode jsonNode : jsonArray) {
            addDirectoryItem(DirectoryItem.fromJson(jsonNode));
        }
    }

    @Override
    public JsonNode resolveToJson() {
        return null;
    }
}
