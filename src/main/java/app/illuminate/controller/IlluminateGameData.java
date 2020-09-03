package app.illuminate.controller;

import app.illuminate.model.*;
import app.illuminate.model.details.IlluminateDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controller.GameData;
import model.*;
import util.Log;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static util.Constants.*;

public abstract class IlluminateGameData extends GameData {

    protected IlluminateGameData(JsonNode inNode) {
        super(inNode);
    }

    protected String getCompositionUUIDByEngramId(String engramId) {
        return getCompositionUUIDByName(engramId);
    }

    protected List<String> getCompositeUUIDListByCompositionId(String compositionId) {
        return getCompositeUUIDListByName(compositionId);
    }

    protected List<Composite> getCompositeList(String compositionId) {
        return getCompositeUUIDListByCompositionId(compositionId)
                .stream().map(this::getComposite).collect(Collectors.toList());
    }

    @Override
    public IlluminateDetails getDetailsObject() {
        return (IlluminateDetails) super.getDetailsObject();
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
            IlluminateFolder folder = IlluminateFolder.fromJson(folderNode);
            addFolder(folder);
        }
    }

    @Override
    protected void mapResourcesFromJson() {
        JsonNode resourcesNode = inNode.get(cResources);
        for (JsonNode resourceNode : resourcesNode) {
            IlluminateResource resource = IlluminateResource.fromJson(resourceNode);
            addResource(resource);
        }
    }

    @Override
    protected void mapEngramsFromJson() {
        JsonNode engramsNode = inNode.get(cEngrams);
        for (JsonNode engramNode : engramsNode) {
            IlluminateEngram engram = IlluminateEngram.fromJson(engramNode);
            addEngram(engram);
        }
    }

    @Override
    protected void mapStationsFromJson() {
        JsonNode stationsNode = inNode.get(cStations);
        for (JsonNode stationNode : stationsNode) {
            IlluminateStation station = IlluminateStation.fromJson(stationNode);
            addStation(station);
        }
    }

    @Override
    protected void mapCompositionFromJson() {
        JsonNode compositionsNode = inNode.get(cComposition);
        for (JsonNode compositionNode : compositionsNode) {
            Composition composition = Composition.fromJson(compositionNode);
            addComposition(composition);
        }
    }

    protected void mapCompositesFromJson() {
        JsonNode compositesNode = inNode.get(cComposites);
        for (JsonNode compositeNode : compositesNode) {
            IlluminateComposite composite = IlluminateComposite.fromJson(compositeNode);
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

    private void addComposition(Composition composition) {
        addComposition(composition.getEngramId(), composition);
    }

    @Override
    protected void addComposition(String engramId, Composition composition) {
        //  tie elements to their engram (source) id
        String uuid = composition.getUuid();

        addCompositionToIdMap(engramId, uuid);
        addCompositionToMap(uuid, composition);
    }

    @Override
    protected void addComposite(Composite composite) {
        //  tie elements to their composition id
        String uuid = composite.getUuid();
        String compositionId = composite.getCompositionId();

        addCompositeToIdMap(compositionId, uuid);
        addCompositeToMap(uuid, composite);
    }

    public JsonNode getIlluminatedFiles() {
        return getDetailsObject().getIlluminatedFiles();
    }

    public void addIlluminatedFile(String illuminatedFile) {
        getDetailsObject().addIlluminatedFile(illuminatedFile);
    }

    public String getFilePathForResources() {
        return getDetailsObject().getFilePath().concat(cIlluminatedResourcesFileName);
    }

    public String generateIlluminatedFilePath(JsonNode resolvedNode) {
        String filePath = getDetailsObject().getFilePath();
        String name = resolvedNode.get(cName).asText();
        return filePath.concat(generateIlluminatedFileName(name));
    }

    private String generateIlluminatedFileName(String name) {
        return String.format(cIlluminatedFileNamePrefix, name.toLowerCase().replace(' ', '_'));
    }

    protected JsonNode resolveResourceNode() {
        ArrayNode arrayNode = mapper.createArrayNode();

        for (String uuid : getResourceIdMap().values()) {
            //  get resource object using given uuid
            Resource resource = getResource(uuid);

            //  convert object to json
            JsonNode resourceNode = convertJsonNode(resource.toJson());

            //  add to json array
            arrayNode.add(resourceNode);
        }

        return arrayNode;
    }

    /**
     * Each array entry to have its own output file, with the station as the root
     */
    protected JsonNode resolveDirectory() {
        Log.d(getDetailsObject().getName());
        ArrayNode arrayNode = mapper.createArrayNode();

        //  iterate through station uuids, in alphabetical order via TreeMap
        for (DirectoryItem directoryItem : getDirectoryItemListByParentUUID(null)) {
            //  get station object using given uuid
            Station station = getStation(directoryItem.getSourceId());

            //  convert object to json
            ObjectNode rootNode = convertJsonNode(station.toJson());

            //  crawl through directory, recursively return hierarchical data
            rootNode.set(cDirectory, resolveDirectoryChildren(directoryItem.getUuid()));

            //  add to json array
            arrayNode.add(rootNode);
        }

        return arrayNode;
    }

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
                Folder folder = getFolder(sourceId);

                //  convert object to json
                ObjectNode folderNode = convertJsonNode(folder.toJson());

                //  crawl through directory, recursively return hierarchical data
                folderNode.set(cDirectory, resolveDirectoryChildren(directoryItem.getUuid()));

                //  add to json array
                folders.add(folderNode);
            } else {
                //  get engram object from given uuid
                Engram engram = getEngram(sourceId);

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

    public JsonNode resolveComposition(String engramId) {
        Log.d(engramId);
        Composition composition = getComposition(engramId);
        ObjectNode outNode = mapper.valueToTree(composition);

        List<Composite> compositeList = getCompositeList(composition.getUuid());
        outNode.set(cComposites, mapper.valueToTree(compositeList));

        return outNode;
    }

    /**
     * Take in a JsonNode, convert into editable ObjectNode filled with public elements
     *
     * @param inNode JsonNode to convert
     * @return editable ObjectNode
     */
    public ObjectNode convertJsonNode(JsonNode inNode) {
        ObjectNode outNode = mapper.createObjectNode();
        Iterator<JsonNode> elements = inNode.elements();
        Iterator<String> fieldNames = inNode.fieldNames();

        //  iterate elements to build output node
        while (elements.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode element = elements.next();

            outNode.set(fieldName, element);
        }

        return outNode;
    }

    public boolean isFolder(String sourceId) {
        return getFolder(sourceId) != null;
    }

    public boolean isEngram(String sourceId) {
        return getEngram(sourceId) != null;
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
