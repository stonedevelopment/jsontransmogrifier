package app.illuminate.model;

import app.illuminate.controller.IlluminateGameData;
import app.illuminate.model.details.IlluminateDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.*;
import util.Log;

import static util.Constants.*;

public class PrimaryIlluminateGameData extends IlluminateGameData {
    public PrimaryIlluminateGameData(JsonNode inNode) {
        super(inNode);

        mapGameDataFromJson();
    }

    @Override
    public IlluminateDetails getDetailsObject() {
        return (IlluminateDetails) super.getDetailsObject();
    }

    @Override
    protected void createDetailsObject() {
        this.details = IlluminateDetails.from(inNode.get(cDetails));
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

        for (String uuid : getResourceIdMap().values()) {
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

    JsonNode resolveDirectoryChildren(String parentId) {
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

    boolean isFolder(String sourceId) {
        return getFolderMap().containsKey(sourceId);
    }
}