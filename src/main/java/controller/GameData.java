package controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import model.*;
import model.details.Details;
import util.Log;

import java.util.*;

/**
 * Base controller that will import an injected json object, map the data, and export to a suggested json file
 */
public abstract class GameData {
    protected static final boolean cDebug = false; // TODO: 4/26/2020 Set to FALSE when finalizing

    protected final ObjectMapper mapper;
    private final JsonNode inNode;

    //  name, uuid
    private final Map<String, String> resourceIdMap = new TreeMap<>();
    private final Map<String, String> engramIdMap = new TreeMap<>();
    private final Map<String, String> stationIdMap = new TreeMap<>();
    private final Map<String, String> folderIdMap = new TreeMap<>();
    private final Map<String, String> compositionIdMap = new TreeMap<>();

    //  name, list<uuid>
    private final Map<String, List<String>> compositeIdMap = new TreeMap<>();
    private final Map<String, List<String>> directoryIdMap = new TreeMap<>();

    //  uuid, object
    private final Map<String, Resource> resourceMap = new HashMap<>();
    private final Map<String, Folder> folderMap = new HashMap<>();
    private final Map<String, Engram> engramMap = new HashMap<>();
    private final Map<String, Station> stationMap = new HashMap<>();
    private final Map<String, Composition> compositionMap = new HashMap<>();
    private final Map<String, Composite> compositeMap = new HashMap<>();
    private final Map<String, DirectoryItem> directoryMap = new HashMap<>();

    //  parent uuid, object
    private final Map<String, List<DirectoryItem>> directoryMapByParent = new HashMap<>();
    protected Details details;

    protected GameData(JsonNode inNode) {
        this.inNode = inNode;
        this.mapper = new ObjectMapper();
    }

    protected static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    protected abstract void createDetailsObject();

    public JsonNode getInNode() {
        return inNode;
    }

    public Details getDetails() {
        return details;
    }

    protected void setDetails(Details details) {
        this.details = details;
    }

    protected void updateDetails(Details details) {
        setDetails(details);
    }

    public Map<String, String> getResourceIdMap() {
        return resourceIdMap;
    }

    public Map<String, String> getEngramIdMap() {
        return engramIdMap;
    }

    public Map<String, String> getFolderIdMap() {
        return folderIdMap;
    }

    public Map<String, String> getStationIdMap() {
        return stationIdMap;
    }

    public Map<String, Folder> getFolderMap() {
        return folderMap;
    }

    public Resource getResource(String uuid) {
        return resourceMap.get(uuid);
    }

    public Resource getResourceByName(String name) {
        String uuid = getResourceUUIDByName(name);
        return getResource(uuid);
    }

    public String getResourceUUIDByName(String name) {
        return resourceIdMap.get(name);
    }

    public String getResourceNameByUUID(String uuid) {
        return getResource(uuid).getName();
    }

    public String getResourceImageFileByUUID(String uuid) {
        Resource resource = getResource(uuid);
        if (resource == null) return null;
        return resource.getImageFile();
    }

    public String getResourceImageFileByName(String name) {
        return getResourceImageFileByUUID(getResourceUUIDByName(name));
    }

    public Engram getEngram(String uuid) {
        return engramMap.get(uuid);
    }

    public Engram getEngramByName(String name) {
        String uuid = getEngramUUIDByName(name);
        return getEngram(uuid);
    }

    public String getEngramUUIDByName(String name) {
        return engramIdMap.get(name);
    }

    public String getEngramNameByUUID(String uuid) {
        return getEngram(uuid).getName();
    }

    public String getEngramImageFileByUUID(String uuid) {
        Engram engram = getEngram(uuid);
        if (engram == null) return null;
        return engram.getImageFile();
    }

    public String getEngramImageFileByName(String name) {
        return getEngramImageFileByUUID(getEngramUUIDByName(name));
    }

    public String getImageFileByName(String name) {
        String imageFile = getResourceImageFileByName(name);
        return imageFile != null ? imageFile : getEngramImageFileByName(name);
    }

    public Station getStation(String uuid) {
        return stationMap.get(uuid);
    }

    public Station getStationByName(String name) {
        String uuid = getStationUUIDByName(name);
        return getStation(uuid);
    }

    public String getStationUUIDByName(String name) {
        return stationIdMap.get(name);
    }

    public Folder getFolder(String uuid) {
        return folderMap.get(uuid);
    }

    public Folder getFolderByName(String name) {
        String uuid = getFolderUUIDByName(name);
        return getFolder(uuid);
    }

    public String getFolderUUIDByName(String name) {
        return folderIdMap.get(name);
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

    public String getCompositeName(String uuid) {
        Resource resource = getResource(uuid);
        if (resource != null) return resource.getName();
        return getEngram(uuid).getName();
    }

    public List<String> getCompositeUUIDListByName(String name) {
        List<String> uuidList = compositeIdMap.get(name);
        if (uuidList == null) return new ArrayList<>();
        return uuidList;
    }

    public DirectoryItem getDirectoryItem(String uuid) {
        return directoryMap.get(uuid);
    }

    public List<String> getDirectoryItemUUIDListByName(String name) {
        List<String> uuidList = directoryIdMap.get(name);
        if (uuidList == null) return new ArrayList<>();
        return uuidList;
    }

    public List<DirectoryItem> getDirectoryItemListByParentUUID(String parentUUID) {
        List<DirectoryItem> directoryItemList = directoryMapByParent.get(parentUUID);
        if (directoryItemList == null) return new ArrayList<>();
        return directoryItemList;
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

    /**
     * Add objects to maps
     */
    protected void addResource(Resource resource) {
        String uuid = resource.getUuid();
        String name = resource.getName();

        addResourceToIdMap(uuid, name);
        addResourceToMap(uuid, resource);
    }

    private void addResourceToIdMap(String uuid, String name) {
        resourceIdMap.put(name, uuid);
    }

    protected void addResourceToMap(String uuid, Resource resource) {
        resourceMap.put(uuid, resource);
    }

    protected void addEngram(Engram engram) {
        String uuid = engram.getUuid();
        String name = engram.getName();

        addEngramToIdMap(uuid, name);
        addEngramToMap(uuid, engram);
    }

    private void addEngramToIdMap(String uuid, String name) {
        engramIdMap.put(name, uuid);
    }

    protected void addEngramToMap(String uuid, Engram engram) {
        engramMap.put(uuid, engram);
    }

    protected void addFolder(Folder folder) {
        String uuid = folder.getUuid();
        String name = folder.getName();

        addFolderToIdMap(uuid, name);
        addFolderToMap(uuid, folder);
    }

    private void addFolderToIdMap(String uuid, String name) {
        folderIdMap.put(name, uuid);
    }

    protected void addFolderToMap(String uuid, Folder folder) {
        folderMap.put(uuid, folder);
    }

    protected void addStation(Station station) {
        String uuid = station.getUuid();
        String name = station.getName();

        addStationToIdMap(uuid, name);
        addStationToMap(uuid, station);
    }

    private void addStationToIdMap(String uuid, String name) {
        stationIdMap.put(name, uuid);
    }

    protected void addStationToMap(String uuid, Station station) {
        stationMap.put(uuid, station);
    }

    protected void addComposition(String name, Composition composition) {
        String uuid = composition.getUuid();

        addCompositionToIdMap(uuid, name);
        addCompositionToMap(uuid, composition);
    }

    protected void addCompositionToIdMap(String uuid, String name) {
        compositionIdMap.put(name, uuid);
    }

    protected void addCompositionToMap(String uuid, Composition composition) {
        if (compositionMap.containsKey(uuid)) {
            Log.debug("addCompositionToMap: already exists!", composition.toString());
        }
        compositionMap.put(uuid, composition);
    }

    protected void addComposite(Composite composite) {
        String uuid = composite.getUuid();
        String name = composite.getName();

        addCompositeToIdMap(uuid, name);
        addCompositeToMap(uuid, composite);
    }

    protected void addCompositeToIdMap(String uuid, String name) {
        List<String> uuidList = getCompositeUUIDListByName(name);
        uuidList.add(uuid);
        compositeIdMap.put(name, uuidList);
    }

    protected void addCompositeToMap(String uuid, Composite composite) {
        compositeMap.put(uuid, composite);
    }

    protected void addDirectoryItem(DirectoryItem directoryItem) {
        String uuid = directoryItem.getUuid();
        String name = directoryItem.getName();
        String parentId = directoryItem.getParentId();

        addDirectoryItemToIdMap(uuid, name);
        addDirectoryItemToMap(uuid, directoryItem);
        addDirectoryItemToParentMap(parentId, directoryItem);
    }

    private void addDirectoryItemToIdMap(String uuid, String name) {
        List<String> uuidList = getDirectoryItemUUIDListByName(name);
        uuidList.add(uuid);
        directoryIdMap.put(name, uuidList);
    }

    private void addDirectoryItemToMap(String uuid, DirectoryItem directoryItem) {
        directoryMap.put(uuid, directoryItem);
    }

    protected void addDirectoryItemToParentMap(String parentId, DirectoryItem directoryItem) {
        List<DirectoryItem> directoryItemList = getDirectoryItemListByParentUUID(parentId);
        directoryItemList.add(directoryItem);
        directoryMapByParent.put(parentId, directoryItemList);
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

    protected Collection<List<Composite>> transformCompositeMap() {
        Map<String, List<Composite>> transformedMap = new TreeMap<>();
        for (Map.Entry<String, List<String>> entry : compositeIdMap.entrySet()) {
            String name = entry.getKey();
            List<String> uuidList = entry.getValue();
            for (String uuid : uuidList) {
                Composite composite = getComposite(uuid);
                List<Composite> compositeList = transformedMap.get(name);
                if (compositeList == null) {
                    compositeList = new ArrayList<>();
                }
                compositeList.add(composite);
                transformedMap.put(name, compositeList);
            }
        }
        return transformedMap.values();
    }

    protected JsonNode flattenCompositeMapToJson(Collection<List<Composite>> collection) {
        ArrayNode flattenedNode = mapper.createArrayNode();

        for (List<Composite> compositeList : collection) {
            for (Composite composite : compositeList) {
                flattenedNode.add(mapper.valueToTree(composite));
            }
        }

        return flattenedNode;
    }

    protected Collection<List<DirectoryItem>> transformDirectory() {
        Map<String, List<DirectoryItem>> transformedMap = new TreeMap<>();
        for (Map.Entry<String, List<String>> entry : directoryIdMap.entrySet()) {
            String name = entry.getKey();
            List<String> uuidList = entry.getValue();
            for (String uuid : uuidList) {
                DirectoryItem directoryItem = getDirectoryItem(uuid);
                List<DirectoryItem> directoryItemList = transformedMap.get(name);
                if (directoryItemList == null) {
                    directoryItemList = new ArrayList<>();
                }
                directoryItemList.add(directoryItem);
                transformedMap.put(name, directoryItemList);
            }
        }

        return transformedMap.values();
    }

    protected JsonNode flattenDirectoryToJson(Collection<List<DirectoryItem>> collection) {
        ArrayNode flattenedNode = mapper.createArrayNode();

        for (List<DirectoryItem> directoryItemList : collection) {
            for (DirectoryItem directoryItem : directoryItemList) {
                flattenedNode.add(mapper.valueToTree(directoryItem));
            }
        }

        return flattenedNode;
    }

    public abstract JsonNode resolveToJson();
}