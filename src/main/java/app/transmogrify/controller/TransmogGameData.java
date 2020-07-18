package app.transmogrify.controller;

import app.transmogrify.model.details.TransmogDetails;
import app.transmogrify.model.json.*;
import com.fasterxml.jackson.databind.JsonNode;
import controller.GameData;
import model.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static util.Constants.*;

/**
 * Base controller class for Transmogrifying old json data into UUID json data
 */
public abstract class TransmogGameData extends GameData {
    protected final JsonDlc jsonDlc;

    //  oldId, uuid
    Map<Long, String> categoryIdMap = new TreeMap<>();

    protected TransmogGameData(JsonNode inNode, JsonDlc jsonDlc) {
        super(inNode);
        this.jsonDlc = jsonDlc;
    }

    @Override
    public TransmogDetails getDetailsObject() {
        return (TransmogDetails) super.getDetailsObject();
    }

    protected long getDlcId() {
        return jsonDlc._id;
    }

    public String getFolderUUIDByCategoryId(long categoryId) {
        return categoryIdMap.get(categoryId);
    }

    public Folder getFolderByCategoryId(long categoryId) {
        String uuid = getFolderUUIDByCategoryId(categoryId);
        return getFolder(uuid);
    }

    @Override
    protected void mapGameDataFromJson() {
        createDetailsObject();
        mapFoldersFromJson();
        mapResourcesFromJson();
        mapEngramsFromJson();
        mapStationsFromJson();
        mapCompositionFromJson();
        mapDirectoryFromJson();
    }

    @Override
    protected void mapFoldersFromJson() {
        JsonNode categoryArray = inNode.get(cJsonCategory);
        for (JsonNode categoryObject : categoryArray) {
            JsonCategory jsonCategory = mapper.convertValue(categoryObject, JsonCategory.class);

            if (isValidDlcId(jsonCategory.dlc_id)) {
                Folder folder;
                if (isFolderUnique(jsonCategory)) {
                    folder = buildFolder(jsonCategory);
                } else {
                    folder = getFolderByName(jsonCategory.name);
                }
                addFolder(jsonCategory._id, folder);
            }
        }
    }

    @Override
    protected void mapResourcesFromJson() {
        JsonNode resourceArray = inNode.get(cJsonResource);
        for (JsonNode resourceObject : resourceArray) {
            JsonResource jsonResource = mapper.convertValue(resourceObject, JsonResource.class);

            if (isValidDlcId(jsonResource.dlc_id)) {
                //  test if is complex resource, skip if true
                if (jsonResource.complex_resource) continue;

                if (isResourceUnique(jsonResource)) {
                    Resource resource = buildResource(jsonResource);
                    addResource(resource);
                }
            }
        }
    }

    @Override
    protected void mapEngramsFromJson() {
        JsonNode engramArray = inNode.get(cJsonEngram);
        for (JsonNode engramObject : engramArray) {
            JsonEngram jsonEngram = mapper.convertValue(engramObject, JsonEngram.class);

            if (isValidDlcId(jsonEngram.dlc_id)) {
                if (isEngramUnique(jsonEngram)) {
                    Engram engram = buildEngram(jsonEngram);
                    addEngram(engram);
                }
            }
        }
    }

    @Override
    protected void mapStationsFromJson() {
        JsonNode stationArray = inNode.get(cJsonStation);
        for (JsonNode stationObject : stationArray) {
            JsonStation jsonStation = mapper.convertValue(stationObject, JsonStation.class);

            if (isValidDlcId(jsonStation.dlc_id)) {
                if (isStationUnique(jsonStation)) {
                    Station station = buildStation(jsonStation);
                    addStation(station);
                }
            }
        }
    }

    @Override
    protected void mapCompositionFromJson() {
        JsonNode engramArray = inNode.get(cJsonEngram);
        for (JsonNode engramObject : engramArray) {
            JsonEngram jsonEngram = mapper.convertValue(engramObject, JsonEngram.class);

            if (isValidDlcId(jsonEngram.dlc_id)) {
                if (isCompositionUnique(jsonEngram)) {
                    Composition composition = buildComposition(jsonEngram);
                    addComposition(jsonEngram.name, composition);
                }
            }
        }
    }

    protected void mapCompositesFromJson(String compositionId, JsonEngram jsonEngram) {
        for (JsonComposite jsonComposite : jsonEngram.composition) {
            if (isCompositeUnique(compositionId, jsonComposite.resource_id, jsonComposite)) {
                Composite composite = buildComposite(compositionId, jsonComposite);
                addComposite(composite);
            }
        }
    }

    @Override
    protected void mapDirectoryFromJson() {
        JsonNode stationArray = inNode.get(cJsonStation);
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

    protected int mapFolderDirectory(final Station station, final long categoryId, final String parentId) {
        int count = 0;

        JsonNode categoryArray = inNode.get(cJsonCategory);
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
                int itemCount = mapFolderDirectoryItemByCategoryId(station, folder, jsonCategory._id, parentId);

                count += itemCount;
            }
        }

        return count;
    }

    protected int mapEngramDirectory(final Station station, final long categoryId, final String parentId) {
        int count = 0;

        JsonNode engramArray = inNode.get(cJsonEngram);
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

    protected abstract int mapFolderDirectoryItemByCategoryId(Station station, Folder folder, long categoryId, String parentId);

    protected abstract void mapStationDirectoryItem(Station station);

    protected abstract void mapEngramDirectoryItem(Engram engram, String parentId);

    protected abstract Folder buildFolder(JsonCategory jsonCategory);

    protected abstract Resource buildResource(JsonResource jsonResource);

    protected abstract Engram buildEngram(JsonEngram jsonEngram);

    protected abstract Station buildStation(JsonStation jsonStation);

    protected abstract Composition buildComposition(JsonEngram jsonEngram);

    protected abstract Composite buildComposite(String compositionId, JsonComposite jsonComposite);

    protected void addFolder(long categoryId, Folder folder) {
        String uuid = folder.getUuid();

        addFolder(folder);
        addFolderToCategoryIdMap(categoryId, uuid);
    }

    private void addFolderToCategoryIdMap(long categoryId, String uuid) {
        categoryIdMap.put(categoryId, uuid);
    }

    private boolean isFolderUnique(JsonCategory jsonCategory) {
        //  test name for uuid
        String uuid = getFolderUUIDByName(jsonCategory.name);
        if (uuid == null) return true;

        //  test uuid for object
        Folder folder = getFolder(uuid);
        if (folder == null) return true;

        //  build testables for uniqueness
        String name = jsonCategory.name;

        //  return if incoming object equals mapped object
        return !folder.equals(name);
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
        return !resource.equals(name, description, imageFile);
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
        return !engram.equals(name, description, imageFile, level, yield, points, xp);
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

    public boolean isCompositionUnique(JsonEngram jsonEngram) {
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

    public boolean isCompositeUnique(String compositionId, String name, JsonComposite jsonComposite) {
        //  test name for uuid
        List<String> uuidList = getCompositeUUIDListByName(name);
        if (uuidList.isEmpty()) return true;

        for (String uuid : uuidList) {
            //  test uuid for object
            Composite composite = getComposite(uuid);
            if (composite == null) continue;

            //  build testables for uniqueness
            String resourceId = getResourceUUIDByName(name);
            String engramId = getEngramUUIDByName(name);

            assert ((resourceId == null && engramId != null) || (resourceId != null && engramId == null));

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

    /**
     * Tests if the dlc id provided matches
     *
     * @param dlcId dlc id to test
     * @return if both ids match
     */
    protected boolean isValidDlcId(long dlcId) {
        return jsonDlc._id == dlcId;
    }

    /**
     * Test if the dlc id provided is valid, used to add stations/folders that belong to primary game data
     *
     * @param dlcId dlc id to test
     * @return if dlc id is valid
     */
    protected abstract boolean isValidDlcIdForDirectory(long dlcId);
}