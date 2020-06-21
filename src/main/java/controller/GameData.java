package controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.*;
import model.details.Details;

import java.util.*;

/**
 * Base controller that will import an injected json file, map the data, and export to a suggested json file
 */
public abstract class GameData {
    protected static final boolean cDebug = false; // TODO: 4/26/2020 Set to FALSE when finalizing

    protected final ObjectMapper mapper;

    protected final List<String> nullifiedResources = new ArrayList<>();

    protected final JsonNode inObject;

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

    public abstract Details getDetailsObject();

    protected Resource getResource(String uuid) {
        return resourceMap.get(uuid);
    }

    protected String getResourceUUIDByName(String name) {
        return resourceIdMap.get(name);
    }

    protected String getResourceImageFileByUUID(String uuid) {
        Resource resource = getResource(uuid);
        if (resource == null) return null;
        return resource.getImageFile();
    }

    protected Engram getEngram(String uuid) {
        return engramMap.get(uuid);
    }

    protected String getEngramUUIDByName(String name) {
        return engramIdMap.get(name);
    }

    protected String getEngramImageFileByUUID(String uuid) {
        Engram engram = getEngram(uuid);
        if (engram == null) return null;
        return engram.getImageFile();
    }

    protected Engram getEngramByName(String name) {
        String uuid = getEngramUUIDByName(name);
        return getEngram(uuid);
    }

    protected Station getStation(String uuid) {
        return stationMap.get(uuid);
    }

    protected String getStationUUIDByName(String name) {
        return stationIdMap.get(name);
    }

    protected Station getStationByName(String name) {
        String uuid = getStationUUIDByName(name);
        return getStation(uuid);
    }

    protected Folder getFolder(String uuid) {
        return folderMap.get(uuid);
    }

    protected Composition getComposition(String uuid) {
        return compositionMap.get(uuid);
    }

    protected String getCompositionUUIDByName(String name) {
        return compositionIdMap.get(name);
    }

    protected Composite getComposite(String uuid) {
        return compositeMap.get(uuid);
    }

    protected List<String> getCompositeUUIDListByName(String name) {
        List<String> uuidList = compositeIdMap.get(name);
        if (uuidList == null) return new ArrayList<>();
        return uuidList;
    }

    protected void addNullifiedResource(String name) {
        if (!nullifiedResources.contains(name)) {
            nullifiedResources.add(name);
        }
    }

    protected void removeNullifiedResource(String name) {
        nullifiedResources.remove(name);
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

    protected abstract void mapCompositesFromJson();

    protected abstract void mapDirectoryFromJson();

    protected abstract void mapStationDirectoryItem(Station station);

    protected abstract void mapEngramDirectoryItem(Engram engram, String parentId);

    protected abstract int mapFolderDirectoryItem(Station station, Folder folder, String parentId);

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

    @Deprecated
    private List<String> generateSubsForResource(String in) {
        //  add in substitutions
        String[] substitutes = new String[0];
        if (in.equals("Leech Blood or Horns")) {
            //  "Because of the current crafting system, some recipes can use either Leech Blood or Woolly Rhino Horn or Deathworm Horn."
            substitutes = new String[]{"Leech Blood", "Deathworm Horn", "Woolly Rhino Horn"};
        } else if (in.equals("Cooked Meat or Fish Meat")) {
            substitutes = new String[]{"Cooked Meat", "Cooked Fish Meat"};
        } else if (in.equals("Cooked Meat or Meat Jerky")) {
            substitutes = new String[]{"Cooked Meat", "Cooked Meat Jerky"};
        } else if (in.equals("Pelt, Hair, or Wool")) {
            substitutes = new String[]{"Pelt", "Human Hair", "Wool"};
        } else if (in.equals("Berries (Any Kind)")) {
            //  Berries
            substitutes = new String[]{"Amarberry", "Azulberry", "Mejoberry", "Narcoberry", "Stimberry", "Tintoberry"};
        } else if (in.equals("Feces (Any Size)")) {
            //  Poops
            substitutes = new String[]{"Human Feces", "Small Animal Feces", "Medium Animal Feces", "Large Animal Feces", "Massive Animal Feces"};
        } else if (in.equals("Extra Small Egg")) {
            substitutes = new String[]{"Dilo Egg", "Dodo Egg", "Featherlight Egg", "Kairuku Egg", "Lystrosaur Egg", "Parasaur Egg", "Tek Parasaur Egg", "Vulture Egg"};
        } else if (in.contains(",")) {
            //  remove "or", convert to list for each ","
            String newName = in.replace(", or ", ",");
            newName = newName.replace(", ", ",");
            substitutes = newName.split(",");
        } else if (in.contains(" or ")) {
            //  replace " or " with ","
            String newName = in.replace(" or ", ",");
            substitutes = newName.split(",");
        }

        return new ArrayList<>(Arrays.asList(substitutes));
    }

    protected abstract String buildFilePathForJSONExport();

    protected abstract JsonNode generateJson();
}