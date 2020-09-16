package app.illuminate.model.controller;

import app.illuminate.controller.IlluminateGameData;
import app.illuminate.model.*;
import app.illuminate.model.details.DlcIlluminateDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.*;

import java.util.HashMap;
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

    private final Map<String, String> replacementIdMap = new TreeMap<>();

    //  uuid, object
    private final Map<String, IlluminateReplacement> replacementMap = new HashMap<>();

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
        JsonNode removalsNode = inNode.get(cRemove);
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

    private void mapReplacementsFromJson() {
        JsonNode replacementsNode = inNode.get(cReplace);
        //  TODO: 9/13/2020 Collect composition ids to that contain composites matching replacement names below
        //      create new composite for each old composite
        //      create new composition with new composite replacement
        //      add new replacement to map with new and old composition id
        mapReplacementResources(replacementsNode.get(cResources));
    }

    private void mapReplacementResources(JsonNode replacementNode) {
        for (JsonNode childNode : replacementNode) {
            //  convert node into base object
            Replacement replacement = Replacement.fromJson(childNode);
            //  break down fields
            String uuid = replacement.getUuid();
            Resource from = getResource((String) replacement.getFrom());
            Resource to = getResource((String) replacement.getTo());
            //  add to map
            addReplacementResource(new IlluminateReplacementResource(uuid, from, to));
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

    private void addReplacementResource(IlluminateReplacementResource resource) {
        String uuid = resource.getUuid();
        String name = resource.getFrom().getName();

        addReplacement(resource);
        addReplacementResourceToIdMap(uuid, name);
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
        replacementIdMap.put(name, uuid);
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
        ObjectNode outNode = mapper.createObjectNode();

        outNode.set(cResources, resolveReplaceResources());

        return outNode;
    }

    private JsonNode resolveReplaceResources() {
        ArrayNode outNode = mapper.createArrayNode();

        for (String uuid : replacementIdMap.values()) {
            ObjectNode replacementNode = mapper.createObjectNode();
            IlluminateReplacementResource replacement = (IlluminateReplacementResource) getReplacement(uuid);
            replacementNode.put(cFrom, replacement.getFrom().getName());
            replacementNode.put(cTo, replacement.getTo().getName());
            outNode.add(replacementNode);
        }

        return outNode;
    }
}