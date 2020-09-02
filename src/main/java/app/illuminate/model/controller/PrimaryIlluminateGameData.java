package app.illuminate.model.controller;

import app.illuminate.controller.IlluminateGameData;
import app.illuminate.model.IlluminateEngram;
import app.illuminate.model.IlluminateFolder;
import app.illuminate.model.details.IlluminateDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.Composition;
import model.DirectoryItem;

import static util.Constants.*;

public class PrimaryIlluminateGameData extends IlluminateGameData {
    private PrimaryIlluminateGameData(JsonNode inNode) {
        super(inNode);

        mapGameDataFromJson();
    }

    public static PrimaryIlluminateGameData with(JsonNode inNode) {
        return new PrimaryIlluminateGameData(inNode);
    }

    @Override
    public Composition getComposition(String engramId) {
        String uuid = getCompositionUUIDByEngramId(engramId);
        return super.getComposition(uuid);
    }

    @Override
    public IlluminateDetails getDetailsObject() {
        return (IlluminateDetails) super.getDetailsObject();
    }

    @Override
    protected JsonNode resolveDirectoryChildren(String parentId) {
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
                //  get folder object using given uuid
                IlluminateFolder folder = (IlluminateFolder) getFolder(sourceId);

                //  convert object to json
                ObjectNode folderNode = convertJsonNode(folder.toJson());

                //  crawl through directory, recursively return hierarchical data
                folderNode.set(cDirectory, resolveDirectoryChildren(directoryItem.getUuid()));

                //  add to json array
                folders.add(folderNode);
            } else {
                //  get engram object from given uuid
                IlluminateEngram engram = (IlluminateEngram) getEngram(sourceId);

                //  convert object to json
                ObjectNode engramNode = convertJsonNode(engram.toJson());

                //  add engram to json array
                engrams.add(engramNode);

                // TODO: 8/29/2020 add in composition here?
                engramNode.set(cComposition, resolveComposition(sourceId));
            }
        }

        //  add folder array node to parent node
        outNode.set(cFolders, folders);

        //  add engram array node to parent node
        outNode.set(cEngrams, engrams);

        //  return parent node
        return outNode;
    }

    @Override
    protected void createDetailsObject() {
        this.details = IlluminateDetails.from(inNode.get(cDetails));
    }
}