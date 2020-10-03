package app.illuminate.controller;

import app.illuminate.model.*;
import app.illuminate.model.details.IlluminateDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controller.GameData;
import model.*;

import java.util.*;
import java.util.stream.Collectors;

import static util.Constants.*;

public abstract class IlluminateGameData extends GameData {
    //  uuid, list<object>
    private final Map<String, List<Composite>> compositeIdMapBySourceId = new HashMap<>();

    protected IlluminateGameData(JsonNode inNode) {
        super(inNode);
    }

    @Override
    public Composition getComposition(String engramId) {
        String uuid = getCompositionUUIDByEngramId(engramId);
        return super.getComposition(uuid);
    }

    protected String getCompositionUUIDByEngramId(String engramId) {
        return getCompositionUUIDByName(engramId);
    }

    protected List<String> getCompositeUUIDListByCompositionId(String compositionId) {
        return getCompositeUUIDListByName(compositionId);
    }

    protected List<Composite> getCompositeListByCompositionId(String compositionId) {
        return getCompositeUUIDListByCompositionId(compositionId)
                .stream().map(this::getComposite).collect(Collectors.toList());
    }

    public List<Composite> getCompositeListBySourceId(String sourceId) {
        if (compositeIdMapBySourceId.containsKey(sourceId)) return compositeIdMapBySourceId.get(sourceId);
        return new ArrayList<>();
    }

    //  collects a list of composition uuids, iterating through all composites to find uuid as source id
    protected List<String> getCompositionIdListFromSourceId(String sourceId) {
        return getCompositeListBySourceId(sourceId)
                .stream().map(Composite::getCompositionId).collect(Collectors.toList());
    }

    @Override
    public IlluminateDetails getDetails() {
        return (IlluminateDetails) super.getDetails();
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
        JsonNode foldersNode = getInNode().get(cFolders);
        for (JsonNode folderNode : foldersNode) {
            IlluminateFolder folder = IlluminateFolder.fromJson(folderNode);
            addFolder(folder);
        }
    }

    @Override
    protected void mapResourcesFromJson() {
        JsonNode resourcesNode = getInNode().get(cResources);
        for (JsonNode resourceNode : resourcesNode) {
            IlluminateResource resource = IlluminateResource.fromJson(resourceNode);
            addResource(resource);
        }
    }

    @Override
    protected void mapEngramsFromJson() {
        JsonNode engramsNode = getInNode().get(cEngrams);
        for (JsonNode engramNode : engramsNode) {
            IlluminateEngram engram = IlluminateEngram.fromJson(engramNode);
            addEngram(engram);
        }
    }

    @Override
    protected void mapStationsFromJson() {
        JsonNode stationsNode = getInNode().get(cStations);
        for (JsonNode stationNode : stationsNode) {
            IlluminateStation station = IlluminateStation.fromJson(stationNode);
            addStation(station);
        }
    }

    @Override
    protected void mapCompositionFromJson() {
        JsonNode compositionsNode = getInNode().get(cComposition);
        for (JsonNode compositionNode : compositionsNode) {
            IlluminateComposition composition = IlluminateComposition.fromJson(compositionNode);
            addComposition(composition);
        }
    }

    protected void mapCompositesFromJson() {
        JsonNode compositesNode = getInNode().get(cComposites);
        for (JsonNode compositeNode : compositesNode) {
            IlluminateComposite composite = IlluminateComposite.fromJson(compositeNode);
            addComposite(composite);
        }
    }

    @Override
    protected void mapDirectoryFromJson() {
        JsonNode directoryNode = getInNode().get(cDirectory);
        for (JsonNode directoryItemNode : directoryNode) {
            DirectoryItem directoryItem = DirectoryItem.fromJson(directoryItemNode);
            addDirectoryItem(directoryItem);
        }
    }

    public void addComposition(Composition composition) {
        addComposition(composition.getEngramId(), composition);
    }

    @Override
    protected void addComposition(String engramId, Composition composition) {
        //  tie elements to their engram (source) id
        String uuid = composition.getUuid();

        addCompositionToIdMap(uuid, engramId);
        addCompositionToMap(uuid, composition);
    }

    @Override
    protected void addComposite(Composite composite) {
        //  tie elements to their composition id
        String uuid = composite.getUuid();
        String compositionId = composite.getCompositionId();
        String sourceId = composite.getSourceId();

        addCompositeToSourceIdMap(sourceId, composite);
        addCompositeToIdMap(uuid, compositionId);
        addCompositeToMap(uuid, composite);
    }

    protected void addCompositeToSourceIdMap(String sourceId, Composite composite) {
        List<Composite> compositeList = getCompositeListBySourceId(sourceId);
        compositeList.add(composite);
        compositeIdMapBySourceId.put(sourceId, compositeList);
    }

    public JsonNode getIlluminatedFiles() {
        return getDetails().getIlluminatedFiles();
    }

    public void addIlluminatedFile(String type, String illuminatedFile) {
        getDetails().addIlluminatedFile(type, illuminatedFile);
    }

    public String generateIlluminatedFilePath(String type) {
        String filePath = String.format(cIlluminatedFileNameFormat, type);
        return getDetails().getFilePath().concat(filePath);
    }

    protected JsonNode resolveResourceNode() {
        ArrayNode arrayNode = mapper.createArrayNode();

        for (String uuid : getResourceIdMap().values()) {
            //  get object using given uuid
            Resource resource = getResource(uuid);
            //  convert object to json
            JsonNode resourceNode = convertJsonNode(resource.toJson());
            //  add to json array
            arrayNode.add(resourceNode);
        }

        return arrayNode;
    }

    protected JsonNode resolveEngramNode() {
        ArrayNode arrayNode = mapper.createArrayNode();

        for (String uuid : getEngramIdMap().values()) {
            //  get object using given uuid
            Engram engram = getEngram(uuid);
            //  convert object to json
            ObjectNode node = convertJsonNode(engram.toJson());

            //  add composition to node
            node.set(cComposition, resolveComposition(uuid));

            //  add to json array
            arrayNode.add(node);
        }

        return arrayNode;
    }

    protected JsonNode resolveFolderNode() {
        ArrayNode arrayNode = mapper.createArrayNode();

        for (String uuid : getFolderIdMap().values()) {
            //  get object using given uuid
            Folder folder = getFolder(uuid);
            //  convert object to json
            JsonNode node = folder.toJson();
            //  add to json array
            arrayNode.add(node);
        }

        return arrayNode;
    }

    protected JsonNode resolveStationNode() {
        ArrayNode arrayNode = mapper.createArrayNode();

        for (String uuid : getStationIdMap().values()) {
            //  get object using given uuid
            Station station = getStation(uuid);
            //  convert object to json
            JsonNode node = station.toJson();
            //  add to json array
            arrayNode.add(node);
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
            //  get station object using given uuid
            Station station = getStation(directoryItem.getSourceId());

            //  convert object to json
            ObjectNode rootNode = mapper.createObjectNode();

            rootNode.put(cName, station.getName());

            //  crawl through directory, recursively return hierarchical data
            rootNode.setAll(resolveDirectoryChildren(directoryItem.getUuid()));

            //  add to json array
            arrayNode.add(rootNode);
        }

        return arrayNode;
    }

    protected ObjectNode resolveDirectoryChildren(String parentId) {
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
                ObjectNode folderNode = mapper.createObjectNode();

                folderNode.put(cName, folder.getName());

                //  crawl through directory, recursively return hierarchical data
                folderNode.setAll(resolveDirectoryChildren(directoryItem.getUuid()));

                //  add to json array
                folders.add(folderNode);
            } else {
                //  get engram object from given uuid
                Engram engram = getEngram(sourceId);

                //  add engram to json array
                engrams.add(engram.getName());
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
        Composition composition = getComposition(engramId);
        ObjectNode outNode = mapper.valueToTree(composition);

        List<Composite> compositeList = getCompositeListByCompositionId(composition.getUuid());
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
    public ObjectNode resolveToJson() {
        //  resolve maps to separate json files for easy editing
        ObjectNode outNode = mapper.createObjectNode();

        //  details
        outNode.set(cDetails, getDetails().toJson());

        //  resolve resources
        outNode.set(cResources, resolveResourceNode());

        //  resolve engrams
        outNode.set(cEngrams, resolveEngramNode());

        //  resolve folders
        outNode.set(cFolders, resolveFolderNode());

        //  resolve stations
        outNode.set(cStations, resolveStationNode());

        //  resolve directory in a hierarchy, separated by stations as parent node
        outNode.set(cDirectory, resolveDirectory());

        return outNode;
    }
}
