package app.illuminate.model;

import app.illuminate.controller.IlluminateGameData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.*;
import model.details.Details;

import static util.Constants.*;

public class PrimaryIlluminateGameData extends IlluminateGameData {
    public PrimaryIlluminateGameData(JsonNode inNode) {
        super(inNode);

        mapGameDataFromJson();
    }

    @Override
    protected void createDetailsObject() {
        this.details = Details.from(inNode.get(cDetails));
    }

    @Override
    public JsonNode resolveToJson() {
        //  resolve maps to separate json files for easy editing
        ObjectNode outNode = mapper.createObjectNode();

        //  details

        //  resolve resources
        outNode.set(cResources, resolveResourceNode());

        //  resolve directory in a hierarchy, separated by stations as parent node
        outNode.set(cDirectory, resolveDirectory());

        return outNode;
    }

    JsonNode resolveResourceNode() {
        ArrayNode arrayNode = mapper.createArrayNode();

        for (String uuid : resourceIdMap.values()) {
            Resource resource = getResource(uuid);
            arrayNode.add(Resource.toJson(resource));
        }

        return arrayNode;
    }

    /**
     * Each array entry to have its own output file, with the station as the root
     */
    JsonNode resolveDirectory() {
        ArrayNode arrayNode = mapper.createArrayNode();

        //  iterate through station uuids, in alphabetical order via TreeMap
        for (String uuid : stationIdMap.values()) {
            ObjectNode rootNode = mapper.createObjectNode();

            //  get station object using given uuid
            Station station = getStation(uuid);

            //  convert POJO to json object todo tie in engram info?
            rootNode.set(cJsonStation, Station.toJson(station));

            //  crawl through directory, recursively return hierarchical data
            String parentId = station.getUuid();
            rootNode.set(cDirectory, resolveDirectoryChildren(parentId));

            arrayNode.add(rootNode);
        }

        return arrayNode;
    }

    JsonNode resolveDirectoryChildren(String parentId) {
        ObjectNode outNode = mapper.createObjectNode();

        //  create array nodes to hold engrams and folders
        ArrayNode engrams = mapper.createArrayNode();
        ArrayNode folders = mapper.createArrayNode();

        //  iterate through directory map
        for (DirectoryItem directoryItem : getDirectoryItemListByParentUUID(parentId)) {
            //  collect uuid of source
            String sourceId = directoryItem.getSourceId();

            //  determine if engram or folder
            if (isFolder(sourceId)) {
                ObjectNode folderNode = mapper.createObjectNode();
                Folder folder = getFolder(sourceId);
                folderNode.set(cFolder, folder.toJson());
                folderNode.set(cDirectory, resolveDirectoryChildren(sourceId));
                folders.add(folderNode);
            } else {
                Engram engram = getEngram(sourceId);
                engrams.add(engram.toJson());
            }
        }

        outNode.set(cFolders, folders);
        outNode.set(cEngrams, engrams);

        return outNode;
    }

    boolean isFolder(String folderId) {
        return folderMap.containsKey(folderId);
    }
}