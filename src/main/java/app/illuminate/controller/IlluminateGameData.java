package app.illuminate.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controller.GameData;
import model.*;
import util.Log;

import static util.Constants.*;

public abstract class IlluminateGameData extends GameData {

    protected IlluminateGameData(JsonNode inNode) {
        super(inNode);
    }

    @Override
    protected void mapGameDataFromJson() {
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
        JsonNode foldersNode = inNode.get(cFolders);
        for (JsonNode folderNode : foldersNode) {
            Folder folder = Folder.fromJson(folderNode);
            addFolder(folder);
        }
    }

    @Override
    protected void mapResourcesFromJson() {
        JsonNode resourcesNode = inNode.get(cResources);
        for (JsonNode resourceNode : resourcesNode) {
            Resource resource = Resource.fromJson(resourceNode);
            addResource(resource);
        }
    }

    @Override
    protected void mapEngramsFromJson() {
        JsonNode engramsNode = inNode.get(cEngrams);
        for (JsonNode engramNode : engramsNode) {
            Engram engram = Engram.fromJson(engramNode);
            addEngram(engram);
        }
    }

    @Override
    protected void mapStationsFromJson() {
        JsonNode stationsNode = inNode.get(cStations);
        for (JsonNode stationNode : stationsNode) {
            Station station = Station.fromJson(stationNode);
            addStation(station);
        }
    }

    @Override
    protected void mapCompositionFromJson() {
        JsonNode compositionsNode = inNode.get(cComposition);
        for (JsonNode compositionNode : compositionsNode) {
            Composition composition = Composition.fromJson(compositionNode);
            String compositionName = getEngramNameByUUID(composition.getEngramId());
            addComposition(compositionName, composition);
        }
    }

    protected void mapCompositesFromJson() {
        JsonNode compositesNode = inNode.get(cComposites);
        for (JsonNode compositeNode : compositesNode) {
            Composite composite = Composite.fromJson(compositeNode);
            addComposite(composite);
        }
    }

    @Override
    protected void mapDirectoryFromJson() {
        JsonNode directoryNode = inNode.get(cDirectory);
        for (JsonNode directoryItemNode : directoryNode) {
            DirectoryItem directoryItem = DirectoryItem.fromJson(directoryItemNode);
            addDirectoryItem(directoryItem);
        }
    }

    public String getFilePathForResources() {
        return getDetailsObject().getFilePath().concat(cIlluminatedResourcesFileName);
    }

    public String getFilePathForDirectory() {
        return getDetailsObject().getFilePath().concat(cIlluminatedDirectoryFileName);
    }

    protected JsonNode resolveResourceNode() {
        ArrayNode arrayNode = mapper.createArrayNode();

        for (String uuid : getResourceIdMap().values()) {
            Resource resource = getResource(uuid);
            arrayNode.add(Resource.toJson(resource));
        }

        return arrayNode;
    }

    /**
     * Each array entry to have its own output file, with the station as the root
     */
    protected JsonNode resolveDirectory() {
        ArrayNode arrayNode = mapper.createArrayNode();

        //  iterate through station uuids, in alphabetical order via TreeMap
        for (DirectoryItem directoryItem : getDirectoryItemListByParentUUID(null)) {
            ObjectNode rootNode = mapper.createObjectNode();

            //  get station object using given uuid
            Station station = getStation(directoryItem.getSourceId());

            //  convert POJO to json object todo tie in engram info?
            rootNode.set(cJsonStation, Station.toJson(station));

            //  crawl through directory, recursively return hierarchical data
            rootNode.set(cDirectory, resolveDirectoryChildren(directoryItem.getUuid()));

            arrayNode.add(rootNode);
        }

        return arrayNode;
    }

    private JsonNode resolveDirectoryChildren(String parentId) {
        ObjectNode outNode = mapper.createObjectNode();

        //  create array nodes to hold engrams and folders
        ArrayNode engrams = mapper.createArrayNode();
        ArrayNode folders = mapper.createArrayNode();

        //  iterate through directory map
        for (DirectoryItem directoryItem : getDirectoryItemListByParentUUID(parentId)) {
            //  collect uuid of source
            String sourceId = directoryItem.getSourceId();

            //  determine if station, engram or folder
            if (isFolder(sourceId)) {
                ObjectNode folderNode = mapper.createObjectNode();
                Folder folder = getFolder(sourceId);
                if (folder != null) {
                    folderNode.set(cFolder, folder.toJson());
                    folderNode.set(cDirectory, resolveDirectoryChildren(directoryItem.getUuid()));
                    folders.add(folderNode);
                } else {
                    Log.d("getFolder returned null: " + sourceId);
                }
            } else {
                Engram engram = getEngram(sourceId);
                if (engram != null) {
                    engrams.add(engram.toJson());
                } else {
                    Log.d("getEngram returned null: " + sourceId);
                }
            }
        }

        outNode.set(cFolders, folders);
        outNode.set(cEngrams, engrams);

        return outNode;
    }

    private boolean isFolder(String sourceId) {
        return getFolderMap().containsKey(sourceId);
    }

    @Override
    public JsonNode resolveToJson() {
        //  resolve maps to separate json files for easy editing
        ObjectNode outNode = mapper.createObjectNode();

        //  details
        outNode.set(cDetails, mapper.valueToTree(getDetailsObject()));

        //  resolve resources
        outNode.set(cResources, resolveResourceNode());

        //  resolve directory in a hierarchy, separated by stations as parent node
        outNode.set(cDirectory, resolveDirectory());

        return outNode;
    }
}
