package model.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.details.DLCDetails;
import model.dlc.*;
import model.json.*;
import model.primary.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DLCGameData extends GameData {
    PrimaryGameData primaryGameData;
    DLCDetails details;
    TotalConversion totalConversion = new TotalConversion();

    public DLCGameData(JsonDlc jsonDlc, JsonNode inObject, ObjectMapper mapper,
                       PrimaryGameData primaryGameData) {
        super(jsonDlc, inObject, mapper);
        this.primaryGameData = primaryGameData;
        this.details = createDetailsObject(jsonDlc);
    }

    private boolean isTotalConversion(String type) {
        return type.equals("total_conversion");
    }

    @Override
    public void removeNullifiedResource(String name) {
        super.removeNullifiedResource(name);
        primaryGameData.removeNullifiedResource(name);
    }

    @Override
    public void mapGameData() {
        mapTotalConversionFromJson();
        super.mapGameData();
    }

    @Override
    public DlcFolder buildFolder(JsonCategory jsonCategory) {
        String uuid = !cDebug ? generateUUID() : jsonCategory.name;
        String name = jsonCategory.name;
        String gameId = details.getGameId();
        String dlcId = details.getUuid();
        return new DlcFolder(uuid, name, gameId, dlcId);
    }

    @Override
    public DlcResource buildResource(JsonResource jsonResource) {
        String uuid = !cDebug ? generateUUID() : jsonResource.name;
        String name = jsonResource.name;
        String description = "";
        String imageFile = jsonResource.image_file;
        Date lastUpdated = new Date();
        String gameId = details.getGameId();
        String dlcId = details.getUuid();

        return new DlcResource(uuid, name, description, imageFile, lastUpdated, gameId, dlcId);
    }

    @Override
    public DlcEngram buildEngram(JsonEngram jsonEngram) {
        String uuid = !cDebug ? generateUUID() : jsonEngram.name;
        String name = jsonEngram.name;
        String description = jsonEngram.description;
        String imageFile = jsonEngram.image_file;
        int level = jsonEngram.level;
        int yield = jsonEngram.yield;
        int points = jsonEngram.points;
        int xp = jsonEngram.xp;
        int craftingTime = 0;
        Date lastUpdated = new Date();
        String gameId = details.getGameId();
        String dlcId = details.getUuid();

        return new DlcEngram(uuid, name, description, imageFile, level, yield, points, xp, craftingTime, lastUpdated,
                gameId, dlcId);
    }

    @Override
    public DlcStation buildStation(JsonStation jsonStation) {
        String uuid = !cDebug ? generateUUID() : jsonStation.name;
        String name = jsonStation.name;
        String imageFile = jsonStation.image_file;
        String engramId = getEngramUUIDByName(jsonStation.name);
        Date lastUpdated = new Date();
        String gameId = details.getGameId();
        String dlcId = details.getUuid();

        return new DlcStation(uuid, name, imageFile, engramId, lastUpdated, gameId, dlcId);
    }

    @Override
    public DlcComposition buildComposition(JsonEngram jsonEngram) {
        String uuid = !cDebug ? generateUUID() : jsonEngram.name;
        String engramId = getEngramUUIDByName(jsonEngram.name);
        Date lastUpdated = new Date();
        String gameId = details.getGameId();
        String dlcId = details.getUuid();

        mapCompositesFromJson(uuid, jsonEngram);

        return new DlcComposition(uuid, engramId, lastUpdated, gameId, dlcId);
    }

    @Override
    public Composite buildComposite(String compositionId, JsonComposite jsonComposite) {
        String uuid = generateUUID();
        String resourceId = getResourceUUIDByName(jsonComposite.resource_id);
        String engramId = getEngramUUIDByName(jsonComposite.resource_id);
        boolean isEngram = engramId != null;
        String sourceId = isEngram ? engramId : resourceId;
        String name = jsonComposite.resource_id;
        String imageFile = isEngram ? getEngramImageFileByUUID(engramId) : getResourceImageFileByUUID(resourceId);
        if (imageFile == null) {
            throw new NullPointerException();
        }

        int quantity = jsonComposite.quantity;
        String gameId = details.getGameId();
        String dlcId = details.getUuid();

        return new DlcComposite(uuid, name, imageFile, quantity, sourceId, isEngram, compositionId, gameId, dlcId);
    }

    @Override
    public JsonNode generateJson() {
        ObjectNode gameDataObject = mapper.createObjectNode();

        gameDataObject.set("details", mapper.valueToTree(details));

        //  add resources, without complex resources
        gameDataObject.set("resources", mapper.valueToTree(transformResourceMap()));

        //  add stations
        gameDataObject.set("stations", mapper.valueToTree(transformStationMap()));

        //  add folders
        gameDataObject.set("folders", mapper.valueToTree(transformFolderMap()));

        //  add engrams
        gameDataObject.set("engrams", mapper.valueToTree(transformEngramMap()));

        //  add composition
        gameDataObject.set("composition", mapper.valueToTree(transformCompositionMap()));

        //  add composites
        gameDataObject.set("composites", mapper.valueToTree(transformCompositeMap()));

        //  add directory, traverse through tree, fill with uuids
        gameDataObject.set("directory", mapper.valueToTree(directory));

        //  add remove section
        gameDataObject.set("remove", createRemoveSection());

        //  add replace section
        gameDataObject.set("replace", createReplaceSection());

        return gameDataObject;
    }

    @Override
    public void mapStationDirectoryItem(Station station) {
        String uuid = !cDebug ? generateUUID() : station.getName();
        String sourceId = station.getUuid();
        String name = station.getName();
        String imageFile = station.getImageFile();
        String gameId = details.getGameId();
        String dlcId = details.getUuid();

        int engramCount = mapEngramDirectory(station, 0, uuid);
        int folderCount = mapFolderDirectory(station, 0, uuid);
        int totalCount = engramCount + folderCount;

        if (totalCount > 0) {
            directory.add(new DlcDirectoryItem(uuid, name, imageFile, cStationViewType, null, sourceId, gameId, dlcId));
        }

    }

    @Override
    public void mapEngramDirectoryItem(Engram engram, String parentId) {
        String uuid = !cDebug ? generateUUID() : engram.getName();
        String sourceId = engram.getUuid();
        String name = engram.getName();
        String imageFile = engram.getImageFile();
        String gameId = details.getGameId();
        String dlcId = details.getUuid();

        directory.add(new DlcDirectoryItem(uuid, name, imageFile, cEngramViewType, parentId, sourceId, gameId, dlcId));
    }

    @Override
    public int mapFolderDirectoryItem(Station station, Folder folder, long categoryId, String parentId) {
        String uuid = !cDebug ? generateUUID() : folder.getName();
        String sourceId = folder.getUuid();
        String name = folder.getName();
        String imageFile = details.getFolderFile();
        String gameId = details.getGameId();
        String dlcId = details.getUuid();

        int engramCount = mapEngramDirectory(station, categoryId, uuid);
        int folderCount = mapFolderDirectory(station, categoryId, uuid);
        int totalCount = engramCount + folderCount;

        if (totalCount > 0) {
            directory.add(new DlcDirectoryItem(uuid, name, imageFile, cFolderViewType, parentId, sourceId, gameId, dlcId));
        }

        return totalCount;
    }

    @Override
    public boolean isValidDlcIdForDirectory(long dlcId) {
        return isValidDlcId(dlcId) || primaryGameData.isValidDlcIdForDirectory(dlcId);
    }

    @Override
    public String buildFilePathForJSONExport() {
        return String.format("src/assets/DLC/%s/transmogrified.json", details.getName());
    }

    @Override
    public DLCDetails createDetailsObject(JsonDlc jsonDlc) {
        String uuid = !cDebug ? UUID.randomUUID().toString() : jsonDlc.name;
        String name = jsonDlc.name;
        String description = jsonDlc.description;
        boolean totalConversion = isTotalConversion(jsonDlc.type);
        String filePath = jsonDlc.filePath;
        String logoFile = "logo.webp";
        String folderFile = "folder.webp";
        String backFolderFile = "backFolder.webp";
        String gameId = primaryGameData.details.getUuid();
        return new DLCDetails(uuid, name, description, totalConversion, filePath, logoFile, folderFile, backFolderFile, gameId);
    }

    @Override
    public DLCDetails getDetailsObject() {
        return details;
    }

    @Override
    public String getResourceUUIDByName(String name) {
        String uuid = super.getResourceUUIDByName(name);
        return uuid != null ? uuid : primaryGameData.getResourceUUIDByName(name);
    }

    @Override
    public String getResourceImageFileByUUID(String uuid) {
        String imageFile = super.getResourceImageFileByUUID(uuid);
        return imageFile != null ? imageFile : primaryGameData.getResourceImageFileByUUID(uuid);
    }

    @Override
    public Resource getResource(String uuid) {
        Resource resource = super.getResource(uuid);
        return resource != null ? resource : primaryGameData.getResource(uuid);
    }

    @Override
    public String getEngramUUIDByName(String name) {
        String uuid = super.getEngramUUIDByName(name);
        return uuid != null ? uuid : primaryGameData.getEngramUUIDByName(name);
    }

    @Override
    public String getEngramImageFileByUUID(String uuid) {
        String imageFile = super.getEngramImageFileByUUID(uuid);
        return imageFile != null ? imageFile : primaryGameData.getEngramImageFileByUUID(uuid);
    }

    @Override
    public Engram getEngramByName(String name) {
        Engram engram = super.getEngramByName(name);
        return engram != null ? engram : primaryGameData.getEngramByName(name);
    }

    @Override
    public Engram getEngram(String uuid) {
        Engram engram = super.getEngram(uuid);
        return engram != null ? engram : primaryGameData.getEngram(uuid);
    }

    @Override
    public String getStationUUIDByName(String name) {
        String uuid = super.getStationUUIDByName(name);
        return uuid != null ? uuid : primaryGameData.getStationUUIDByName(name);
    }

    @Override
    public Station getStationByName(String name) {
        Station station = super.getStationByName(name);
        return station != null ? station : primaryGameData.getStationByName(name);
    }

    @Override
    public Station getStation(String uuid) {
        Station station = super.getStation(uuid);
        return station != null ? station : primaryGameData.getStation(uuid);
    }

    @Override
    public String getFolderUUIDByCategoryId(long id) {
        String uuid = super.getFolderUUIDByCategoryId(id);
        return uuid != null ? uuid : primaryGameData.getFolderUUIDByCategoryId(id);
    }

    @Override
    public Folder getFolderByCategoryId(long id) {
        Folder folder = super.getFolderByCategoryId(id);
        return folder != null ? folder : primaryGameData.getFolderByCategoryId(id);
    }

    @Override
    public Folder getFolder(String uuid) {
        Folder folder = super.getFolder(uuid);
        return folder != null ? folder : primaryGameData.getFolder(uuid);
    }

    @Override
    public String getCompositionUUIDByName(String name) {
        String uuid = super.getCompositionUUIDByName(name);
        return uuid != null ? uuid : primaryGameData.getCompositionUUIDByName(name);
    }

    @Override
    public Composition getComposition(String uuid) {
        Composition composition = super.getComposition(uuid);
        return composition != null ? composition : primaryGameData.getComposition(uuid);
    }

    @Override
    public List<String> getCompositeUUIDListByName(String name) {
        List<String> uuidList = super.getCompositeUUIDListByName(name);
        uuidList.addAll(primaryGameData.getCompositeUUIDListByName(name));
        return uuidList;
    }

    @Override
    public Composite getComposite(String uuid) {
        Composite composite = super.getComposite(uuid);
        return composite != null ? composite : primaryGameData.getComposite(uuid);
    }

    private JsonNode createRemoveSection() {
        ObjectNode outNode = mapper.createObjectNode();

        //  add uuids of resources to remove
        ArrayNode resourceArray = mapper.createArrayNode();
        outNode.set("resources", resourceArray);

        //  add uuids of stations to remove
        ArrayNode stationArray = mapper.createArrayNode();
        for (String stationName : totalConversion.stationsToRemove) {
            stationArray.add(getStationUUIDByName(stationName));
        }
        outNode.set("stations", stationArray);

        //  add uuids of folders to remove
        ArrayNode folderArray = mapper.createArrayNode();
        for (long categoryId : totalConversion.foldersToRemove) {
            folderArray.add(getFolderUUIDByCategoryId(categoryId));
        }
        outNode.set("folders", folderArray);

        //  add uuids of engrams to remove
        ArrayNode engramArray = mapper.createArrayNode();
        for (String engramName : totalConversion.engramsToRemove) {
            engramArray.add(getEngramUUIDByName(engramName));
        }
        outNode.set("engrams", engramArray);

        return outNode;
    }

    private JsonNode createReplaceSection() {
        ObjectNode outNode = mapper.createObjectNode();

        //  add uuids of resources to replace
        ArrayNode resourceArray = mapper.createArrayNode();
        for (Map.Entry<String, String> resourceEntry : totalConversion.resourcesToReplace.entrySet()) {
            String from = resourceEntry.getKey();
            String to = resourceEntry.getValue();

            ObjectNode resourceNode = mapper.createObjectNode();
            resourceNode.put(from, to);

            resourceArray.add(resourceNode);
        }
        outNode.set("resources", resourceArray);

        //  add uuids of stations to replace
        ArrayNode stationArray = mapper.createArrayNode();
        outNode.set("stations", stationArray);

        //  add uuids of folders to replace
        ArrayNode folderArray = mapper.createArrayNode();
        outNode.set("folders", folderArray);

        //  add uuids of engrams to replace
        ArrayNode engramArray = mapper.createArrayNode();
        outNode.set("engrams", engramArray);

        //  add uuids of engrams to replace
        ArrayNode compositionArray = mapper.createArrayNode();
        outNode.set("composition", compositionArray);

        return outNode;
    }

    private void mapTotalConversionFromJson() {
        JsonNode totalConversionArray = inObject.get("total_conversion");
        for (JsonNode totalConversionObject : totalConversionArray) {
            JsonTotalConversion jsonTotalConversion =
                    mapper.convertValue(totalConversionObject, JsonTotalConversion.class);

            if (isValidDlcId(jsonTotalConversion.dlc_id)) {
                //  add list of resources to replace
                for (JsonTotalConversionResource resourceConversion : jsonTotalConversion.resource) {
                    totalConversion.resourcesToReplace.put(resourceConversion.from, resourceConversion.to);
                }

                //  add lists of removals
                totalConversion.stationsToRemove.addAll(jsonTotalConversion.station);
                totalConversion.foldersToRemove.addAll(jsonTotalConversion.category);
                totalConversion.engramsToRemove.addAll(jsonTotalConversion.engram);
            }
        }
    }
}