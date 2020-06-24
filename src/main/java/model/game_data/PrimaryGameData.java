package model.game_data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controller.GameData;
import model.Engram;
import model.Folder;
import model.Resource;
import model.Station;
import model.details.Details;
import model.details.PrimaryDetails;

import static util.Constants.cFolders;
import static util.Constants.cResources;

public class PrimaryGameData extends GameData {
    private PrimaryDetails details;

    public PrimaryGameData(JsonNode inObject) throws JsonProcessingException {
        super(inObject);

        this.details = (PrimaryDetails) Details.from(inObject);

        mapGameDataFromJson();
    }

    public String getUuid() {
        return details.getUuid();
    }

    @Override
    public PrimaryDetails getDetailsObject() {
        return details;
    }

    @Override
    protected void mapGameDataFromJson() {
        mapFoldersFromJson();
        mapResourcesFromJson();
        mapEngramsFromJson();
    }

    @Override
    protected void mapFoldersFromJson() {
        JsonNode foldersNode = inObject.get(cFolders);
        for (JsonNode folderNode : foldersNode) {
            addFolderToMap(Folder.fromJson(folderNode));
        }
    }

    @Override
    protected void mapResourcesFromJson() {
        JsonNode resourcesNode = inObject.get(cResources);
        for (JsonNode resourceNode : resourcesNode) {
            addResourceToMap(Resource.fromJson(resourceNode));
        }
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
    protected void mapCompositesFromJson() {

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
    protected int mapFolderDirectoryItem(Station station, Folder folder, String parentId) {
        return 0;
    }

    @Override
    public JsonNode resolveToJson() {
        //  create dlc json object
        ObjectNode gameDataObject = mapper.createObjectNode();

        gameDataObject.set("details", mapper.valueToTree(details));

        //  add resources, without complex resources
        gameDataObject.set("resources", mapper.valueToTree(transformResourceMap()));

        //  add stations
        gameDataObject.set("stations", mapper.valueToTree(transformStationMap()));

        //  add folders
        gameDataObject.set("folders", mapper.valueToTree(transformFolderMap()));

        //  add engrams
        gameDataObject.set("engrams", mapper.valueToTree(transformEngramMap()));

        //  add composition
        gameDataObject.set("composition", mapper.valueToTree(transformCompositionMap()));

        //  add composites
        gameDataObject.set("composites", mapper.valueToTree(transformCompositeMap()));

        //  add directory, traverse through tree, fill with uuids
        gameDataObject.set("directory", mapper.valueToTree(directory));

        return gameDataObject;
    }
}
