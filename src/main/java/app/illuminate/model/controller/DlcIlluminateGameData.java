package app.illuminate.model.controller;

import app.illuminate.controller.IlluminateGameData;
import app.illuminate.model.IlluminateResource;
import app.illuminate.model.details.DlcIlluminateDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.*;

import java.util.HashMap;
import java.util.Map;

import static util.Constants.*;

public class DlcIlluminateGameData extends IlluminateGameData {

    private final PrimaryIlluminateGameData primaryGameData;

    //  uuid, object
    private final Map<String, IlluminateResource> removeResourcesMap = new HashMap<>();

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
        Resource resource = super.getResource(uuid);
        if (resource == null) return primaryGameData.getResource(uuid);
        return resource;
    }

    @Override
    public Folder getFolder(String uuid) {
        Folder folder = super.getFolder(uuid);
        if (folder == null) return primaryGameData.getFolder(uuid);
        return folder;
    }

    @Override
    public Engram getEngram(String uuid) {
        Engram engram = super.getEngram(uuid);
        if (engram == null) return primaryGameData.getEngram(uuid);
        return engram;
    }

    @Override
    public Station getStation(String uuid) {
        Station station = super.getStation(uuid);
        if (station == null) return primaryGameData.getStation(uuid);
        return station;
    }

    @Override
    public Composition getComposition(String uuid) {
        Composition composition = super.getComposition(uuid);
        if (composition == null) return primaryGameData.getComposition(uuid);
        return composition;
    }

    @Override
    public Composite getComposite(String uuid) {
        Composite composite = super.getComposite(uuid);
        if (composite == null) return primaryGameData.getComposite(uuid);
        return composite;
    }

    @Override
    public DirectoryItem getDirectoryItem(String uuid) {
        DirectoryItem directoryItem = super.getDirectoryItem(uuid);
        if (directoryItem == null) return primaryGameData.getDirectoryItem(uuid);
        return directoryItem;
    }

    @Override
    protected void mapGameDataFromJson() {
        super.mapGameDataFromJson();
        mapRemovalsFromJson();
    }

    private void mapRemovalsFromJson() {
        JsonNode removalsNode = inNode.get(cRemove);
        mapRemoveResources(removalsNode.get(cResources));
    }

    private void mapRemoveResources(JsonNode resourcesNode) {
        for (JsonNode uuidNode : resourcesNode) {
            String uuid = mapper.convertValue(uuidNode, String.class);
            IlluminateResource resource = (IlluminateResource) getResource(uuid);
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

        for (Map.Entry<String, IlluminateResource> resourceEntry : removeResourcesMap.entrySet()) {
            String uuid = resourceEntry.getKey();
            Resource resource = resourceEntry.getValue();
            outNode.add(mapper.valueToTree(resource));
        }

        return outNode;
    }
}