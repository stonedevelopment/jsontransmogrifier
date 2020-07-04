package controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.*;
import model.details.Details;

import java.util.*;

/**
 * Base controller that will import an injected json object, map the data, and export to a suggested json file
 */
public abstract class GameData {
    protected static final boolean cDebug = false; // TODO: 4/26/2020 Set to FALSE when finalizing

    protected final ObjectMapper mapper;
    protected final JsonNode inObject;
    protected Details details;

    //  name, uuid
    protected Map<String, String> resourceIdMap = new TreeMap<>();
    protected Map<String, String> engramIdMap = new TreeMap<>();
    protected Map<String, String> stationIdMap = new TreeMap<>();
    protected Map<String, String> folderIdMap = new TreeMap<>();
    protected Map<String, String> compositionIdMap = new TreeMap<>();

    //  name, list<uuid>
    protected Map<String, List<String>> compositeIdMap = new TreeMap<>();

    //  uuid, object
    protected Map<String, Resource> resourceMap = new HashMap<>();
    protected Map<String, Folder> folderMap = new HashMap<>();
    protected Map<String, Engram> engramMap = new HashMap<>();
    protected Map<String, Station> stationMap = new HashMap<>();
    protected Map<String, Composition> compositionMap = new HashMap<>();
    protected Map<String, Composite> compositeMap = new HashMap<>();

    //  list<object>
    protected List<DirectoryItem> directory = new ArrayList<>();

    public GameData(JsonNode inObject) {
        this.inObject = inObject;
        this.mapper = new ObjectMapper();
    }

    protected static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    protected abstract void createDetailsObject();

    public Details getDetailsObject() {
        return details;
    }

    public Resource getResource(String uuid) {
        return resourceMap.get(uuid);
    }

    public String getResourceUUIDByName(String name) {
        return resourceIdMap.get(name);
    }

    public String getResourceImageFileByUUID(String uuid) {
        Resource resource = getResource(uuid);
        if (resource == null) return null;
        return resource.getImageFile();
    }

    public Engram getEngram(String uuid) {
        return engramMap.get(uuid);
    }

    public String getEngramUUIDByName(String name) {
        return engramIdMap.get(name);
    }

    public String getEngramImageFileByUUID(String uuid) {
        Engram engram = getEngram(uuid);
        if (engram == null) return null;
        return engram.getImageFile();
    }

    public Engram getEngramByName(String name) {
        String uuid = getEngramUUIDByName(name);
        return getEngram(uuid);
    }

    public Station getStation(String uuid) {
        return stationMap.get(uuid);
    }

    public String getStationUUIDByName(String name) {
        return stationIdMap.get(name);
    }

    public Station getStationByName(String name) {
        String uuid = getStationUUIDByName(name);
        return getStation(uuid);
    }

    public Folder getFolder(String uuid) {
        return folderMap.get(uuid);
    }

    public String getFolderUUIDByName(String name) {
        return folderIdMap.get(name);
    }

    public Folder getFolderByName(String name) {
        String uuid = getFolderUUIDByName(name);
        return getFolder(uuid);
    }

    public Composition getComposition(String uuid) {
        return compositionMap.get(uuid);
    }

    public String getCompositionUUIDByName(String name) {
        return compositionIdMap.get(name);
    }

    public Composite getComposite(String uuid) {
        return compositeMap.get(uuid);
    }

    public List<String> getCompositeUUIDListByName(String name) {
        List<String> uuidList = compositeIdMap.get(name);
        if (uuidList == null) return new ArrayList<>();
        return uuidList;
    }

    /**
     * Map objects from incoming json file
     */
    protected abstract void mapGameDataFromJson();

    protected abstract void mapFoldersFromJson();

    protected abstract void mapResourcesFromJson();

    protected abstract void mapEngramsFromJson();

    protected abstract void mapStationsFromJson();

    protected abstract void mapCompositionFromJson();

    protected abstract void mapDirectoryFromJson();

    protected abstract void mapStationDirectoryItem(Station station);

    protected abstract void mapEngramDirectoryItem(Engram engram, String parentId);

    /**
     * Add objects to maps
     */
    protected void addResourceToMap(Resource resource) {
        resourceIdMap.put(resource.getName(), resource.getUuid());
        resourceMap.put(resource.getUuid(), resource);
    }

    protected void addEngramToMap(Engram engram) {
        engramIdMap.put(engram.getName(), engram.getUuid());
        engramMap.put(engram.getUuid(), engram);
    }

    protected void addFolderToMap(Folder folder) {
        folderIdMap.put(folder.getName(), folder.getUuid());
        folderMap.put(folder.getUuid(), folder);
    }

    protected void addStationToMap(Station station) {
        stationIdMap.put(station.getName(), station.getUuid());
        stationMap.put(station.getUuid(), station);
    }

    protected void addCompositionToMap(String name, Composition composition) {
        compositionIdMap.put(name, composition.getUuid());
        compositionMap.put(composition.getUuid(), composition);
    }

    protected void addCompositeToMap(Composite composite) {
        addCompositeToIdMap(composite.getName(), composite.getUuid());
        compositeMap.put(composite.getUuid(), composite);
    }

    protected void addCompositeToIdMap(String name, String uuid) {
        List<String> uuids = getCompositeUUIDListByName(name);
        uuids.add(uuid);
        compositeIdMap.put(name, uuids);
    }

    /**
     * Transforms the object map by applying it against the sorted id map.
     * Essentially exports the raw UUID data, sorted by name.
     */
    protected Collection<Resource> transformResourceMap() {
        Map<String, Resource> transformedMap = new TreeMap<>();
        for (Map.Entry<String, String> resourceEntry : resourceIdMap.entrySet()) {
            String name = resourceEntry.getKey();
            String uuid = resourceEntry.getValue();
            Resource resource = getResource(uuid);
            transformedMap.put(name, resource);
        }
        return transformedMap.values();
    }

    protected Collection<Engram> transformEngramMap() {
        Map<String, Engram> transformedMap = new TreeMap<>();
        for (Map.Entry<String, String> entry : engramIdMap.entrySet()) {
            String name = entry.getKey();
            String uuid = entry.getValue();
            Engram engram = getEngram(uuid);
            transformedMap.put(name, engram);
        }
        return transformedMap.values();
    }

    protected Collection<Folder> transformFolderMap() {
        Map<String, Folder> transformedMap = new TreeMap<>();
        for (Map.Entry<String, String> entry : folderIdMap.entrySet()) {
            String name = entry.getKey();
            String uuid = entry.getValue();
            Folder folder = getFolder(uuid);
            transformedMap.put(name, folder);
        }
        return transformedMap.values();
    }

    protected Collection<Station> transformStationMap() {
        Map<String, Station> transformedMap = new TreeMap<>();
        for (Map.Entry<String, String> entry : stationIdMap.entrySet()) {
            String name = entry.getKey();
            String uuid = entry.getValue();
            Station station = getStation(uuid);
            transformedMap.put(name, station);
        }
        return transformedMap.values();
    }

    protected Collection<Composition> transformCompositionMap() {
        Map<String, Composition> transformedMap = new TreeMap<>();
        for (Map.Entry<String, String> entry : compositionIdMap.entrySet()) {
            String name = entry.getKey();
            String uuid = entry.getValue();
            Composition composition = getComposition(uuid);
            transformedMap.put(name, composition);
        }
        return transformedMap.values();
    }

    protected Collection<Composite> transformCompositeMap() {
        Map<String, Composite> transformedMap = new TreeMap<>();
        for (Map.Entry<String, List<String>> entry : compositeIdMap.entrySet()) {
            String name = entry.getKey();
            List<String> uuidList = entry.getValue();
            for (String uuid : uuidList) {
                Composite composite = getComposite(uuid);
                transformedMap.put(name, composite);
            }
        }
        return transformedMap.values();
    }

    public abstract JsonNode resolveToJson();
}