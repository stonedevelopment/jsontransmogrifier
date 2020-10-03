package app.illuminate.model.controller;

import app.illuminate.controller.IlluminateGameData;
import app.illuminate.model.IlluminateReplacement;
import app.illuminate.model.details.IlluminateDlcDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static util.Constants.*;

public class IlluminateDlcGameData extends IlluminateGameData {

    private final PrimaryIlluminateGameData primaryGameData;

    //  name, uuid
    private final Map<String, String> removeResourcesIdMap = new TreeMap<>();
    private final Map<String, String> removeStationsIdMap = new TreeMap<>();
    private final Map<String, String> removeFoldersIdMap = new TreeMap<>();
    private final Map<String, String> removeEngramsIdMap = new TreeMap<>();

    //  uuid, object
    private final Map<String, IlluminateReplacement> replacementMap = new HashMap<>();

    private IlluminateDlcGameData(JsonNode inNode, PrimaryIlluminateGameData primaryGameData) {
        super(inNode);

        this.primaryGameData = primaryGameData;

        mapGameDataFromJson();
    }

    public static IlluminateDlcGameData with(JsonNode inNode, PrimaryIlluminateGameData primaryGameData) {
        return new IlluminateDlcGameData(inNode, primaryGameData);
    }

    @Override
    public IlluminateDlcDetails getDetails() {
        return (IlluminateDlcDetails) super.getDetails();
    }

    @Override
    protected void createDetailsObject() {
        this.details = IlluminateDlcDetails.fromJson(getInNode().get(cDetails));
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
    public String getCompositeName(String uuid) {
        String name = super.getCompositeName(uuid);
        if (name == null) return primaryGameData.getCompositeName(uuid);
        return name;
    }

    @Override
    public List<Composite> getCompositeListBySourceId(String sourceId) {
        List<Composite> compositeList = super.getCompositeListBySourceId(sourceId);
        compositeList.addAll(primaryGameData.getCompositeListBySourceId(sourceId));
        return compositeList;
    }

    @Override
    public DirectoryItem getDirectoryItem(String uuid) {
        DirectoryItem directoryItem = super.getDirectoryItem(uuid);
        if (directoryItem == null) return primaryGameData.getDirectoryItem(uuid);
        return directoryItem;
    }

    public Replacement getReplacement(String uuid) {
        return replacementMap.get(uuid);
    }

    @Override
    protected void mapGameDataFromJson() {
        super.mapGameDataFromJson();
        mapRemovalsFromJson();
        mapReplacementsFromJson();
    }

    private void mapRemovalsFromJson() {
        JsonNode removalsNode = getInNode().get(cRemove);
        mapRemovalResources(removalsNode.get(cResources));
        mapRemovalStations(removalsNode.get(cStations));
        mapRemovalFolders(removalsNode.get(cFolders));
        mapRemovalEngrams(removalsNode.get(cEngrams));
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

    /**
     * Converts a list of composite uuids into a list of composite names
     */
    private void mapReplacementsFromJson() {
        JsonNode replacementsNode = getInNode().get(cReplace);
        //  TODO: 9/13/2020 Collect composition ids to that contain composites matching replacement names below
        //      create new composite for each old composite
        //      create new composition with new composite replacement
        //      add new replacement to map with new and old composition id
        for (JsonNode childNode : replacementsNode) {
            //  convert node into base object
            Replacement replacement = Replacement.fromJson(childNode);

            //  break down fields
            String uuid = replacement.getUuid();
            String fromId = (String) replacement.getFrom();
            String fromName = getCompositeName(fromId);
            String toId = (String) replacement.getTo();
            String toName = getCompositeName(toId);

            addReplacement(new IlluminateReplacement(uuid, fromName, toName));
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

    private void addReplacement(IlluminateReplacement replacement) {
        String uuid = replacement.getUuid();
        replacementMap.put(uuid, replacement);
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

    @Override
    public boolean isFolder(String sourceId) {
        return super.isFolder(sourceId) || primaryGameData.isFolder(sourceId);
    }

    private boolean isTotalConversion() {
        return getDetails().isTotalConversion();
    }

    @Override
    public ObjectNode resolveToJson() {
        //  resolve maps to separate json files for easy editing
        ObjectNode outNode = super.resolveToJson();

        outNode.set(cBlacklist, resolveRemovalsToBlacklist());
        if (isTotalConversion()) {
            outNode.set(cTotalConversion, resolveReplacementsToTotalConversion());
        }

        return outNode;
    }

    private JsonNode resolveRemovalsToBlacklist() {
        ObjectNode outNode = mapper.createObjectNode();

        outNode.set(cStations, resolveRemoveStations());
        outNode.set(cFolders, resolveRemoveFolders());
        outNode.set(cEngrams, resolveRemoveEngrams());

        return outNode;
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
        ArrayNode arrayNode = mapper.createArrayNode();

        for (Map.Entry<String, IlluminateReplacement> entry : replacementMap.entrySet()) {
            IlluminateReplacement replacement = entry.getValue();
            arrayNode.add(mapper.valueToTree(replacement));
        }

        return arrayNode;
    }
}