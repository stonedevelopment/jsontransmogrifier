package app.illuminate.model;

import app.illuminate.controller.IlluminateGameData;
import app.illuminate.model.details.DlcIlluminateDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.*;
import model.dlc.*;

import java.util.HashMap;
import java.util.Map;

import static util.Constants.*;

public class DlcIlluminateGameData extends IlluminateGameData {

    private final PrimaryIlluminateGameData primaryGameData;

    //  uuid, object
    private Map<String, DlcResource> removeResourcesMap = new HashMap<>();

    private DlcIlluminateGameData(JsonNode inNode, PrimaryIlluminateGameData primaryGameData) {
        super(inNode);

        this.primaryGameData = primaryGameData;

        mapGameDataFromJson();
    }

    public static DlcIlluminateGameData with(JsonNode inNode, PrimaryIlluminateGameData primaryGameData) {
        return new DlcIlluminateGameData(inNode, primaryGameData);
    }

    @Override
    public DlcIlluminateDetails getDetailsObject() {
        return (DlcIlluminateDetails) super.getDetailsObject();
    }

    @Override
    protected void createDetailsObject() {
        this.details = DlcIlluminateDetails.from(inNode.get(cDetails));
    }

    @Override
    public Resource getResource(String uuid) {
        DlcResource resource = (DlcResource) super.getResource(uuid);
        if (resource == null) return primaryGameData.getResource(uuid);
        return resource;
    }

    @Override
    public Folder getFolder(String uuid) {
        DlcFolder folder = (DlcFolder) super.getFolder(uuid);
        if (folder == null) return primaryGameData.getFolder(uuid);
        return folder;
    }

    @Override
    public Engram getEngram(String uuid) {
        DlcEngram engram = (DlcEngram) super.getEngram(uuid);
        if (engram == null) return primaryGameData.getEngram(uuid);
        return engram;
    }

    @Override
    public Station getStation(String uuid) {
        DlcStation station = (DlcStation) super.getStation(uuid);
        if (station == null) return primaryGameData.getStation(uuid);
        return station;
    }

    @Override
    public Composition getComposition(String uuid) {
        DlcComposition composition = (DlcComposition) super.getComposition(uuid);
        if (composition == null) return primaryGameData.getComposition(uuid);
        return composition;
    }

    @Override
    public Composite getComposite(String uuid) {
        DlcComposite composite = (DlcComposite) super.getComposite(uuid);
        if (composite == null) return primaryGameData.getComposite(uuid);
        return composite;
    }

    @Override
    public DirectoryItem getDirectoryItem(String uuid) {
        DlcDirectoryItem directoryItem = (DlcDirectoryItem) super.getDirectoryItem(uuid);
        if (directoryItem == null) return primaryGameData.getDirectoryItem(uuid);
        return directoryItem;
    }

    @Override
    protected void mapGameDataFromJson() {
        super.mapGameDataFromJson();
        mapRemovalsFromJson();
    }

    @Override
    protected void mapResourcesFromJson() {
        JsonNode resourcesNode = inNode.get(cResources);
        for (JsonNode resourceNode : resourcesNode) {
            DlcResource resource = DlcResource.fromJson(resourceNode);
            addResource(resource);
        }
    }

    @Override
    protected void mapFoldersFromJson() {
        JsonNode foldersNode = inNode.get(cFolders);
        for (JsonNode folderNode : foldersNode) {
            DlcFolder folder = DlcFolder.fromJson(folderNode);
            addFolder(folder);
        }
    }

    @Override
    protected void mapEngramsFromJson() {
        JsonNode engramsNode = inNode.get(cEngrams);
        for (JsonNode engramNode : engramsNode) {
            DlcEngram engram = DlcEngram.fromJson(engramNode);
            addEngram(engram);
        }
    }

    @Override
    protected void mapStationsFromJson() {
        JsonNode stationsNode = inNode.get(cStations);
        for (JsonNode stationNode : stationsNode) {
            DlcStation station = DlcStation.fromJson(stationNode);
            addStation(station);
        }
    }

    @Override
    protected void mapCompositionFromJson() {
        JsonNode compositionsNode = inNode.get(cComposition);
        for (JsonNode compositionNode : compositionsNode) {
            DlcComposition composition = DlcComposition.fromJson(compositionNode);
            String compositionName = getEngramNameByUUID(composition.getEngramId());
            addComposition(compositionName, composition);
        }
    }

    protected void mapCompositesFromJson() {
        JsonNode compositesNode = inNode.get(cComposites);
        for (JsonNode compositeNode : compositesNode) {
            DlcComposite composite = DlcComposite.fromJson(compositeNode);
            addComposite(composite);
        }
    }

    @Override
    protected void mapDirectoryFromJson() {
        JsonNode directoryNode = inNode.get(cDirectory);
        for (JsonNode directoryItemNode : directoryNode) {
            DlcDirectoryItem directoryItem = DlcDirectoryItem.fromJson(directoryItemNode);
            addDirectoryItem(directoryItem);
        }
    }

    private void mapRemovalsFromJson() {
        JsonNode removalsNode = inNode.get(cRemove);
        mapRemoveResources(removalsNode.get(cResources));
    }

    private void mapRemoveResources(JsonNode resourcesNode) {
        for (JsonNode uuidNode : resourcesNode) {
            String uuid = mapper.convertValue(uuidNode, String.class);
            DlcResource resource = (DlcResource) getResource(uuid);
            removeResourcesMap.put(uuid, resource);
        }
    }

    @Override
    public boolean isFolder(String sourceId) {
        return super.isFolder(sourceId) || primaryGameData.isFolder(sourceId);
    }

    @Override
    public JsonNode resolveToJson() {
        //  resolve maps to separate json files for easy editing
        ObjectNode outNode = (ObjectNode) super.resolveToJson();

        outNode.set(cRemove, resolveRemovals());

        return outNode;
    }

    private JsonNode resolveRemovals() {
        ObjectNode outNode = mapper.createObjectNode();

        outNode.set(cResources, resolveRemoveResources());

        return outNode;
    }

    private JsonNode resolveRemoveResources() {
        ArrayNode outNode = mapper.createArrayNode();

        for (Map.Entry<String, DlcResource> resourceEntry : removeResourcesMap.entrySet()) {
            String uuid = resourceEntry.getKey();
            Resource resource = resourceEntry.getValue();
            outNode.add(mapper.valueToTree(resource));
        }

        return outNode;
    }
}