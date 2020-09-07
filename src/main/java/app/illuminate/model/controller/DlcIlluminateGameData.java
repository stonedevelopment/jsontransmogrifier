package app.illuminate.model.controller;

import app.illuminate.controller.IlluminateGameData;
import app.illuminate.model.details.DlcIlluminateDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.*;

import java.util.Map;
import java.util.TreeMap;

import static util.Constants.*;

public class DlcIlluminateGameData extends IlluminateGameData {

    private final PrimaryIlluminateGameData primaryGameData;

    //  name, uuid
    private final Map<String, String> removeResourcesIdMap = new TreeMap<>();
    private final Map<String, String> removeStationsIdMap = new TreeMap<>();
    private final Map<String, String> removeFoldersIdMap = new TreeMap<>();
    private final Map<String, String> removeEngramsIdMap = new TreeMap<>();
    private final Map<String, String> replaceResourcesIdMap = new TreeMap<>();
    private final Map<String, String> replaceStationsIdMap = new TreeMap<>();
    private final Map<String, String> replaceFoldersIdMap = new TreeMap<>();
    private final Map<String, String> replaceEngramsIdMap = new TreeMap<>();

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
        mapReplacementsFromJson();
    }

    private void mapRemovalsFromJson() {
        JsonNode removalsNode = inNode.get(cRemove);
        mapRemovalResources(removalsNode.get(cResources));
        mapRemovalStations(removalsNode.get(cStations));
        mapRemovalFolders(removalsNode.get(cFolders));
        mapRemovalEngrams(removalsNode.get(cEngrams));
    }

    private void mapReplacementsFromJson() {
        JsonNode removalsNode = inNode.get(cReplace);
        mapReplacementResources(removalsNode.get(cResources));
    }

    private void mapRemovalResources(JsonNode jsonNode) {
        for (JsonNode uuidNode : jsonNode) {
            String uuid = uuidNode.asText();
            addRemovalResource(getResource(uuid));
        }
    }

    private void mapRemovalStations(JsonNode jsonNode) {
        for (JsonNode uuidNode : jsonNode) {
            String uuid = uuidNode.asText();
            addRemovalStation(getStation(uuid));
        }
    }

    private void mapRemovalFolders(JsonNode jsonNode) {
        for (JsonNode uuidNode : jsonNode) {
            String uuid = uuidNode.asText();
            addRemovalFolder(getFolder(uuid));
        }
    }

    private void mapRemovalEngrams(JsonNode jsonNode) {
        for (JsonNode uuidNode : jsonNode) {
            String uuid = uuidNode.asText();
            addRemovalEngram(getEngram(uuid));
        }
    }

    private void mapReplacementResources(JsonNode jsonNode) {
        for (JsonNode uuidNode : jsonNode) {
            String uuid = uuidNode.asText();
            addReplacementResource(getResource(uuid));
        }
    }

    private void mapReplacementStations(JsonNode jsonNode) {
        for (JsonNode uuidNode : jsonNode) {
            String uuid = uuidNode.asText();
            addReplacementStation(getStation(uuid));
        }
    }

    private void mapReplacementFolders(JsonNode jsonNode) {
        for (JsonNode uuidNode : jsonNode) {
            String uuid = uuidNode.asText();
            addReplacementFolder(getFolder(uuid));
        }
    }

    private void mapReplacementEngrams(JsonNode jsonNode) {
        for (JsonNode uuidNode : jsonNode) {
            String uuid = uuidNode.asText();
            addReplacementEngram(getEngram(uuid));
        }
    }

    private void addRemovalResource(Resource resource) {
        String uuid = resource.getUuid();
        String name = resource.getName();

        addRemovalResourceToIdMap(uuid, name);
    }

    private void addRemovalStation(Station station) {
        String uuid = station.getUuid();
        String name = station.getName();

        addRemovalStationToIdMap(uuid, name);
    }

    private void addRemovalFolder(Folder folder) {
        String uuid = folder.getUuid();
        String name = folder.getName();

        addRemovalFolderToIdMap(uuid, name);
    }

    private void addRemovalEngram(Engram engram) {
        String uuid = engram.getUuid();
        String name = engram.getName();

        addRemovalEngramToIdMap(uuid, name);
    }

    private void addReplacementResource(Resource resource) {
        String uuid = resource.getUuid();
        String name = resource.getName();

        addReplacementResourceToIdMap(uuid, name);
    }

    private void addReplacementStation(Station station) {
        String uuid = station.getUuid();
        String name = station.getName();

        addReplacementStationToIdMap(uuid, name);
    }

    private void addReplacementFolder(Folder folder) {
        String uuid = folder.getUuid();
        String name = folder.getName();

        addReplacementFolderToIdMap(uuid, name);
    }

    private void addReplacementEngram(Engram engram) {
        String uuid = engram.getUuid();
        String name = engram.getName();

        addReplacementEngramToIdMap(uuid, name);
    }

    private void addRemovalResourceToIdMap(String uuid, String name) {
        removeResourcesIdMap.put(name, uuid);
    }

    private void addRemovalStationToIdMap(String uuid, String name) {
        removeStationsIdMap.put(name, uuid);
    }

    private void addRemovalFolderToIdMap(String uuid, String name) {
        removeFoldersIdMap.put(name, uuid);
    }

    private void addRemovalEngramToIdMap(String uuid, String name) {
        removeEngramsIdMap.put(name, uuid);
    }

    private void addReplacementResourceToIdMap(String uuid, String name) {
        replaceResourcesIdMap.put(name, uuid);
    }

    private void addReplacementStationToIdMap(String uuid, String name) {
        replaceStationsIdMap.put(name, uuid);
    }

    private void addReplacementFolderToIdMap(String uuid, String name) {
        replaceFoldersIdMap.put(name, uuid);
    }

    private void addReplacementEngramToIdMap(String uuid, String name) {
        replaceEngramsIdMap.put(name, uuid);
    }

    @Override
    public boolean isFolder(String sourceId) {
        return super.isFolder(sourceId) || primaryGameData.isFolder(sourceId);
    }

    private boolean isTotalConversion() {
        return getDetailsObject().isTotalConversion();
    }

    @Override
    public JsonNode resolveToJson() {
        //  resolve maps to separate json files for easy editing
        ObjectNode outNode = (ObjectNode) super.resolveToJson();

        outNode.set(cBlacklist, resolveRemovalsToBlacklist());
        if (isTotalConversion()) {
            outNode.set(cTotalConversion, resolveReplacementsToTotalConversion());
        }

        return outNode;
    }

    private JsonNode resolveRemovalsToBlacklist() {
        ObjectNode outNode = mapper.createObjectNode();

        outNode.set(cResources, resolveRemoveResources());
        outNode.set(cStations, resolveRemoveStations());
        outNode.set(cFolders, resolveRemoveFolders());
        outNode.set(cEngrams, resolveRemoveEngrams());

        return outNode;
    }

    private JsonNode resolveRemoveResources() {
        return mapper.valueToTree(removeResourcesIdMap.keySet());
    }

    private JsonNode resolveRemoveStations() {
        return mapper.valueToTree(removeStationsIdMap.keySet());
    }

    private JsonNode resolveRemoveFolders() {
        return mapper.valueToTree(removeFoldersIdMap.keySet());
    }

    private JsonNode resolveRemoveEngrams() {
        return mapper.valueToTree(removeEngramsIdMap.keySet());
    }

    private JsonNode resolveReplacementsToTotalConversion() {
        return resolveReplaceResources();
    }

    private JsonNode resolveReplaceResources() {
        return mapper.valueToTree(replaceResourcesIdMap.keySet());
    }
}