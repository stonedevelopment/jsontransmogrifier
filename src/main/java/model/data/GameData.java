package model.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.details.Details;
import model.json.*;
import model.primary.*;
import util.Log;

import java.util.*;

public abstract class GameData {
    public static final boolean cDebug = false; // TODO: 4/26/2020 Set to FALSE when finalizing
    public static final String cUuid = "uuid";
    public static final String cName = "name";
    public static final String cImageFile = "imageFile";
    public static final String cStationId = "stationId";
    public static final int cStationViewType = 0;
    public static final int cFolderViewType = 1;
    public static final int cEngramViewType = 2;

    public List<String> nullifiedResources = new ArrayList<>();

    public JsonDlc jsonDlc;
    JsonNode inObject;
    ObjectMapper mapper;

    //  name, uuid
    Map<String, String> resourceIdMap = new TreeMap<>();
    Map<String, String> engramIdMap = new TreeMap<>();
    Map<String, String> stationIdMap = new TreeMap<>();
    Map<String, String> compositionIdMap = new TreeMap<>();

    //  name, list<uuid>
    Map<String, List<String>> compositeIdMap = new TreeMap<>();

    //  oldId, uuid
    Map<Long, String> folderIdMap = new TreeMap<>();

    //  uuid, object
    Map<String, Resource> resourceMap = new HashMap<>();
    Map<String, Folder> folderMap = new HashMap<>();
    Map<String, Engram> engramMap = new HashMap<>();
    Map<String, Station> stationMap = new HashMap<>();
    Map<String, Composition> compositionMap = new HashMap<>();
    Map<String, Composite> compositeMap = new HashMap<>();

    //  list<object>
    List<DirectoryItem> directory = new ArrayList<>();

    public GameData(JsonDlc jsonDlc, JsonNode inObject, ObjectMapper mapper) {
        this.jsonDlc = jsonDlc;
        this.inObject = inObject;
        this.mapper = mapper;
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public abstract Details createDetailsObject(JsonDlc jsonDlc);

    public abstract Details getDetailsObject();

    public long getDlcId() {
        return jsonDlc._id;
    }

    public String getResourceUUIDByName(String name) {
        return resourceIdMap.get(name);
    }

    public String getResourceImageFileByUUID(String uuid) {
        Resource resource = getResource(uuid);
        if (resource == null) return null;
        return resource.getImageFile();
    }

    public Resource getResource(String uuid) {
        return resourceMap.get(uuid);
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

    public Engram getEngram(String uuid) {
        return engramMap.get(uuid);
    }

    public String getStationUUIDByName(String name) {
        return stationIdMap.get(name);
    }

    public Station getStationByName(String name) {
        String uuid = getStationUUIDByName(name);
        return getStation(uuid);
    }

    public Station getStation(String uuid) {
        return stationMap.get(uuid);
    }

    public String getFolderUUIDByCategoryId(long id) {
        return folderIdMap.get(id);
    }

    public Folder getFolderByCategoryId(long id) {
        String uuid = getFolderUUIDByCategoryId(id);
        return getFolder(uuid);
    }

    public Folder getFolder(String uuid) {
        return folderMap.get(uuid);
    }

    public String getCompositionUUIDByName(String name) {
        return compositionIdMap.get(name);
    }

    public Composition getComposition(String uuid) {
        return compositionMap.get(uuid);
    }

    public List<String> getCompositeUUIDListByName(String name) {
        List<String> uuidList = compositeIdMap.get(name);
        if (uuidList == null) return new ArrayList<>();
        return uuidList;
    }

    public Composite getComposite(String uuid) {
        return compositeMap.get(uuid);
    }

    public void addNullifiedResource(String name) {
        if (!nullifiedResources.contains(name)) {
            nullifiedResources.add(name);
        }
    }

    public void removeNullifiedResource(String name) {
        nullifiedResources.remove(name);
    }

    public void mapGameData() {
        mapFoldersFromJson();
        mapResourcesFromJson();
        mapEngramsFromJson();
        mapStationsFromJson();
        mapCompositionFromJson();
        mapDirectoryFromJson();
    }

    public abstract JsonNode generateJson();

    void mapFoldersFromJson() {
        JsonNode categoryArray = inObject.get("category");
        for (JsonNode categoryObject : categoryArray) {
            JsonCategory jsonCategory = mapper.convertValue(categoryObject, JsonCategory.class);

            if (isValidDlcId(jsonCategory.dlc_id)) {
                if (isFolderUnique(jsonCategory)) {
                    Folder folder = buildFolder(jsonCategory);
                    addFolderToMap(jsonCategory._id, folder);
                } else {
                    Log.d("Duplicate Folder found: " + jsonCategory.toString());
                }
            }
        }
    }

    private boolean isFolderUnique(JsonCategory jsonCategory) {
        //  test name for uuid
        String uuid = folderIdMap.get(jsonCategory._id);
        if (uuid == null) return true;

        //  test uuid for object
        Folder folder = getFolder(uuid);
        if (folder == null) return true;

        //  build testables for uniqueness
        String name = jsonCategory.name;

        //  return if incoming object equals mapped object
        return folder.equals(name);
    }

    public abstract Folder buildFolder(JsonCategory jsonCategory);

    private void addFolderToMap(long _id, Folder folder) {
        folderIdMap.put(_id, folder.getUuid());
        folderMap.put(folder.getUuid(), folder);
    }

    public Collection<Folder> transformFolderMap() {
        Map<String, Folder> transformedMap = new TreeMap<>();
        for (String uuid : folderIdMap.values()) {
            Folder folder = getFolder(uuid);
            transformedMap.put(folder.getName(), folder);
        }
        return transformedMap.values();
    }

    void mapResourcesFromJson() {
        JsonNode resourceArray = inObject.get("resource");
        for (JsonNode resourceObject : resourceArray) {
            JsonResource jsonResource = mapper.convertValue(resourceObject, JsonResource.class);

            if (isValidDlcId(jsonResource.dlc_id)) {
                //  test if is complex resource, skip if true
                if (jsonResource.complex_resource) continue;

                if (isResourceUnique(jsonResource)) {
                    Resource resource = buildResource(jsonResource);
                    addResourceToMap(resource);
                } else {
                    Log.d("Duplicate Resource found: " + jsonResource.toString());
                }
            }
        }
    }

    private boolean isResourceUnique(JsonResource jsonResource) {
        //  test name for uuid
        String uuid = getResourceUUIDByName(jsonResource.name);
        if (uuid == null) return true;

        //  test uuid for object
        Resource resource = getResource(uuid);
        if (resource == null) return true;

        //  build testables for uniqueness
        String name = jsonResource.name;
        String description = "";
        String imageFile = jsonResource.image_file;

        //  return if incoming object equals mapped object
        return resource.equals(name, description, imageFile);
    }

    public abstract Resource buildResource(JsonResource jsonResource);

    private void addResourceToMap(Resource resource) {
        resourceIdMap.put(resource.getName(), resource.getUuid());
        resourceMap.put(resource.getUuid(), resource);
    }

    public Collection<Resource> transformResourceMap() {
        Map<String, Resource> transformedMap = new TreeMap<>();
        for (Map.Entry<String, String> resourceEntry : resourceIdMap.entrySet()) {
            String name = resourceEntry.getKey();
            String uuid = resourceEntry.getValue();
            Resource resource = getResource(uuid);
            transformedMap.put(name, resource);
        }
        return transformedMap.values();
    }

    void mapEngramsFromJson() {
        JsonNode engramArray = inObject.get("engram");
        for (JsonNode engramObject : engramArray) {
            JsonEngram jsonEngram = mapper.convertValue(engramObject, JsonEngram.class);

            if (isValidDlcId(jsonEngram.dlc_id)) {
                if (isEngramUnique(jsonEngram)) {
                    Engram engram = buildEngram(jsonEngram);
                    addEngramToMap(engram);
                } else {
                    Log.d("Duplicate Engram found: " + jsonEngram.toString());
                }
            }
        }
    }

    private boolean isEngramUnique(JsonEngram jsonEngram) {
        //  test name for uuid
        String uuid = getEngramUUIDByName(jsonEngram.name);
        if (uuid == null) return true;

        //  test uuid for object
        Engram engram = getEngram(uuid);
        if (engram == null) return true;

        //  build testables for uniqueness
        String name = jsonEngram.name;
        String description = jsonEngram.description;
        String imageFile = jsonEngram.image_file;
        int level = jsonEngram.level;
        int yield = jsonEngram.yield;
        int points = jsonEngram.points;
        int xp = jsonEngram.xp;

        //  return if incoming object equals mapped object
        return engram.equals(name, description, imageFile, level, yield, points, xp);
    }

    public abstract Engram buildEngram(JsonEngram jsonEngram);

    private void addEngramToMap(Engram engram) {
        engramIdMap.put(engram.getName(), engram.getUuid());
        engramMap.put(engram.getUuid(), engram);
    }

    public Collection<Engram> transformEngramMap() {
        Map<String, Engram> transformedMap = new TreeMap<>();
        for (Map.Entry<String, String> entry : engramIdMap.entrySet()) {
            String name = entry.getKey();
            String uuid = entry.getValue();
            Engram engram = getEngram(uuid);
            transformedMap.put(name, engram);
        }
        return transformedMap.values();
    }

    void mapStationsFromJson() {
        JsonNode stationArray = inObject.get("station");
        for (JsonNode stationObject : stationArray) {
            JsonStation jsonStation = mapper.convertValue(stationObject, JsonStation.class);

            if (isValidDlcId(jsonStation.dlc_id)) {
                if (isStationUnique(jsonStation)) {
                    Station station = buildStation(jsonStation);
                    addStationToMap(station);
                } else {
                    Log.d("Duplicate Station found: " + jsonStation.toString());
                }
            }
        }
    }

    private boolean isStationUnique(JsonStation jsonStation) {
        //  test name for uuid
        String uuid = getStationUUIDByName(jsonStation.name);
        if (uuid == null) return true;

        //  test uuid for object
        Station station = getStation(uuid);
        if (station == null) return true;

        //  build testables for uniqueness
        String name = jsonStation.name;
        String imageFile = jsonStation.image_file;
        String engramId = getEngramUUIDByName(jsonStation.name);

        //  return if incoming object equals mapped object
        return !station.equals(name, imageFile, engramId);
    }

    public abstract Station buildStation(JsonStation jsonStation);

    private void addStationToMap(Station station) {
        stationIdMap.put(station.getName(), station.getUuid());
        stationMap.put(station.getUuid(), station);
    }

    public Collection<Station> transformStationMap() {
        Map<String, Station> transformedMap = new TreeMap<>();
        for (Map.Entry<String, String> entry : stationIdMap.entrySet()) {
            String name = entry.getKey();
            String uuid = entry.getValue();
            Station station = getStation(uuid);
            transformedMap.put(name, station);
        }
        return transformedMap.values();
    }

    void mapCompositionFromJson() {
        JsonNode engramArray = inObject.get("engram");
        for (JsonNode engramObject : engramArray) {
            JsonEngram jsonEngram = mapper.convertValue(engramObject, JsonEngram.class);

            if (isValidDlcId(jsonEngram.dlc_id)) {
                if (isCompositionUnique(jsonEngram)) {
                    Composition composition = buildComposition(jsonEngram);
                    addCompositionToMap(jsonEngram.name, composition);
                } else {
                    Log.d("Duplicate Composition found: " + jsonEngram.toString());
                }
            }
        }
    }

    private boolean isCompositionUnique(JsonEngram jsonEngram) {
        //  test name for uuid
        String uuid = getCompositionUUIDByName(jsonEngram.name);
        if (uuid == null) return true;

        //  test uuid for object
        Composition composition = getComposition(uuid);
        if (composition == null) return true;

        //  build testables for uniqueness
        String name = jsonEngram.name;
        String engramId = getEngramUUIDByName(name);

        //  return if incoming object equals mapped object
        return !composition.equals(engramId);
    }

    public abstract Composition buildComposition(JsonEngram jsonEngram);

    private void addCompositionToMap(String name, Composition composition) {
        compositionIdMap.put(name, composition.getUuid());
        compositionMap.put(composition.getUuid(), composition);
    }

    public Collection<Composition> transformCompositionMap() {
        Map<String, Composition> transformedMap = new TreeMap<>();
        for (Map.Entry<String, String> entry : compositionIdMap.entrySet()) {
            String name = entry.getKey();
            String uuid = entry.getValue();
            Composition composition = getComposition(uuid);
            transformedMap.put(name, composition);
        }
        return transformedMap.values();
    }

    void mapCompositesFromJson(String compositionId, JsonEngram jsonEngram) {
        for (JsonComposite jsonComposite : jsonEngram.composition) {
            if (isCompositeUnique(compositionId, jsonComposite.resource_id, jsonComposite)) {
                Composite composite = buildComposite(compositionId, jsonComposite);
                addCompositeToMap(composite);
            }
        }
    }

    private boolean isCompositeUnique(String compositionId, String name, JsonComposite jsonComposite) {
        //  test name for uuid
        List<String> uuids = getCompositeUUIDListByName(name);
        if (uuids.isEmpty()) return true;

        for (String uuid : uuids) {
            //  test uuid for object
            Composite composite = getComposite(uuid);
            if (composite == null) continue;

            //  build testables for uniqueness
            String resourceId = getResourceUUIDByName(name);
            String engramId = getEngramUUIDByName(name);
            boolean isEngram = engramId != null;
            String sourceId = isEngram ? engramId : resourceId;
            String imageFile = isEngram ? getEngramImageFileByUUID(engramId) : getResourceImageFileByUUID(resourceId);
            int quantity = jsonComposite.quantity;

            //  return if incoming object equals mapped object
            if (composite.equals(name, imageFile, quantity, sourceId, isEngram, compositionId)) {
                return false;
            }
        }

        return true;
    }

    public abstract Composite buildComposite(String compositionId, JsonComposite jsonComposite);

    public void addCompositeToMap(Composite composite) {
        addCompositeToIdMap(composite.getName(), composite.getUuid());
        compositeMap.put(composite.getUuid(), composite);
    }

    public void addCompositeToIdMap(String name, String uuid) {
        List<String> uuids = getCompositeUUIDListByName(name);
        uuids.add(uuid);
        compositeIdMap.put(name, uuids);
    }

    public Collection<Composite> transformCompositeMap() {
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

    public abstract void mapStationDirectoryItem(Station station);

    public abstract void mapEngramDirectoryItem(Engram engram, String parentId);

    public abstract int mapFolderDirectoryItem(Station station, Folder folder, long categoryId, String parentId);

    List<String> convertResourceNamesToId(List<String> in) {
        List<String> out = new ArrayList<>();

        for (String resourceName : in) {
            String uuid = getResourceUUIDByName(resourceName);
            if (uuid == null) {
                uuid = getEngramUUIDByName(resourceName);
                if (uuid == null) {
                    addNullifiedResource(resourceName);
                    continue;
                }
            }

            removeNullifiedResource(resourceName);

            out.add(uuid);
        }

        return out;
    }

    List<String> generateSubsForResource(String in) {
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

    /**
     * Tests if the dlc id provided matches
     *
     * @param dlcId dlc id to test
     * @return if both ids match
     */
    boolean isValidDlcId(long dlcId) {
        return jsonDlc._id == dlcId;
    }

    /**
     * Test if the dlc id provided is valid, used to add stations/folders that belong to primary game data
     *
     * @param dlcId dlc id to test
     * @return if dlc id is valid
     */
    public abstract boolean isValidDlcIdForDirectory(long dlcId);

    /**
     * directory [
     * {
     * uuid
     * name
     * imageFile
     * parentId
     * sourceId
     * gameId
     * }
     * ]
     */
    void mapDirectoryFromJson() {
        JsonNode stationArray = inObject.get("station");
        for (JsonNode stationObject : stationArray) {
            JsonStation jsonStation = mapper.convertValue(stationObject, JsonStation.class);

            if (isValidDlcIdForDirectory(jsonStation.dlc_id)) {
                //  get station from map
                Station station = getStationByName(jsonStation.name);

                //  map station object
                mapStationDirectoryItem(station);
            }
        }
    }

    int mapFolderDirectory(final Station station, final long categoryId, final String parentId) {
        int count = 0;

        JsonNode categoryArray = inObject.get("category");
        for (JsonNode categoryObject : categoryArray) {
            JsonCategory jsonCategory = mapper.convertValue(categoryObject, JsonCategory.class);

            if (isValidDlcIdForDirectory(jsonCategory.dlc_id)) {

                //  test if current station exists for category
                if (!jsonCategory.station.contains(station.getName())) continue;

                //  test if both categories share the same parent
                if (jsonCategory.parent_id != categoryId) continue;

                //  get folder from folderMap
                Folder folder = getFolderByCategoryId(jsonCategory._id);

                //  map folder object
                int itemCount = mapFolderDirectoryItem(station, folder, jsonCategory._id, parentId);

                count += itemCount;
            }
        }

        return count;
    }

    int mapEngramDirectory(final Station station, final long categoryId, final String parentId) {
        int count = 0;

        JsonNode engramArray = inObject.get("engram");
        for (JsonNode engramObject : engramArray) {
            JsonEngram jsonEngram = mapper.convertValue(engramObject, JsonEngram.class);

            if (isValidDlcId(jsonEngram.dlc_id)) {
                //  test if current station exists for category
                if (!jsonEngram.station.contains(station.getName())) continue;

                //  test if engram shares the same parent
                if (jsonEngram.category_id != categoryId) continue;

                //  get engram from engramMap
                Engram engram = getEngramByName(jsonEngram.name);

                //  create engram json object
                mapEngramDirectoryItem(engram, parentId);

                //  increase count for length validity
                count++;
            }
        }

        return count;
    }

    public abstract String buildFilePathForJSONExport();
}