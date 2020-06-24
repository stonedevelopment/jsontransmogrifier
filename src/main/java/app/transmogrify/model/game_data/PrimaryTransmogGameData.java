package app.transmogrify.model.game_data;

import app.transmogrify.model.json.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.*;
import model.details.PrimaryDetails;
import util.Log;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static util.Constants.*;

public class PrimaryTransmogGameData extends TransmogGameData {
    private final PrimaryDetails details;

    public PrimaryTransmogGameData(JsonNode inObject, JsonDlc jsonDlc) {
        super(inObject, jsonDlc);
        this.details = createDetailsObject(jsonDlc);
        mapGameDataFromJson();
    }

    public String getUuid() {
        return details.getUuid();
    }

    @Override
    public boolean isValidDlcIdForDirectory(long dlcId) {
        return getDlcId() == dlcId;
    }

    @Override
    public PrimaryDetails createDetailsObject(JsonDlc jsonDlc) {
        String uuid = !cDebug ? UUID.randomUUID().toString() : jsonDlc.name;
        String name = jsonDlc.name;
        String description = jsonDlc.description;
        String filePath = jsonDlc.filePath;
        String logoFile = "logo.webp";
        String folderFile = "folder.webp";
        String backFolderFile = "backFolder.webp";
        return new PrimaryDetails(uuid, name, description, filePath, logoFile, folderFile, backFolderFile);
    }

    @Override
    public PrimaryDetails getDetailsObject() {
        return details;
    }

    @Override
    protected String getResourceUUIDByName(String name) {
        return super.getResourceUUIDByName(name);
    }

    @Override
    protected String getResourceImageFileByUUID(String uuid) {
        return super.getResourceImageFileByUUID(uuid);
    }

    @Override
    protected Resource getResource(String uuid) {
        return super.getResource(uuid);
    }

    @Override
    protected String getEngramUUIDByName(String name) {
        return super.getEngramUUIDByName(name);
    }

    @Override
    protected String getEngramImageFileByUUID(String uuid) {
        return super.getEngramImageFileByUUID(uuid);
    }

    @Override
    protected Engram getEngramByName(String name) {
        return super.getEngramByName(name);
    }

    @Override
    protected Engram getEngram(String uuid) {
        return super.getEngram(uuid);
    }

    @Override
    protected String getStationUUIDByName(String name) {
        return super.getStationUUIDByName(name);
    }

    @Override
    protected Station getStationByName(String name) {
        return super.getStationByName(name);
    }

    @Override
    protected Station getStation(String uuid) {
        return super.getStation(uuid);
    }

    @Override
    protected String getFolderUUIDByCategoryId(long id) {
        return super.getFolderUUIDByCategoryId(id);
    }

    @Override
    protected Folder getFolderByCategoryId(long id) {
        return super.getFolderByCategoryId(id);
    }

    @Override
    protected Folder getFolder(String uuid) {
        return super.getFolder(uuid);
    }

    @Override
    protected String getCompositionUUIDByName(String name) {
        return super.getCompositionUUIDByName(name);
    }

    @Override
    protected Composition getComposition(String uuid) {
        return super.getComposition(uuid);
    }

    @Override
    protected List<String> getCompositeUUIDListByName(String name) {
        return super.getCompositeUUIDListByName(name);
    }

    @Override
    protected Composite getComposite(String uuid) {
        return super.getComposite(uuid);
    }

    @Override
    protected Folder buildFolder(JsonCategory jsonCategory) {
        String uuid = !cDebug ? generateUUID() : jsonCategory.name;
        String name = jsonCategory.name;
        String gameId = details.getUuid();
        return new Folder(uuid, name, gameId);
    }

    @Override
    protected Resource buildResource(JsonResource jsonResource) {
        String uuid = !cDebug ? generateUUID() : jsonResource.name;
        String name = jsonResource.name;
        String description = "";
        String imageFile = jsonResource.image_file;
        Date lastUpdated = new Date();
        String gameId = details.getUuid();

        return new Resource(uuid, name, description, imageFile, lastUpdated, gameId);
    }

    @Override
    protected Engram buildEngram(JsonEngram jsonEngram) {
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
        String gameId = details.getUuid();

        return new Engram(uuid, name, description, imageFile, level, yield, points, xp, craftingTime, lastUpdated,
                gameId);
    }

    @Override
    protected Station buildStation(JsonStation jsonStation) {
        String uuid = !cDebug ? generateUUID() : jsonStation.name;
        String name = jsonStation.name;
        String imageFile = jsonStation.image_file;
        String engramId = getEngramUUIDByName(jsonStation.name);
        Date lastUpdated = new Date();
        String gameId = details.getUuid();
        return new Station(uuid, name, imageFile, engramId, lastUpdated, gameId);
    }

    @Override
    protected Composition buildComposition(JsonEngram jsonEngram) {
        String uuid = !cDebug ? generateUUID() : jsonEngram.name;
        String engramId = getEngramUUIDByName(jsonEngram.name);
        Date lastUpdated = new Date();
        String gameId = details.getUuid();

        mapCompositesFromJson(uuid, jsonEngram);

        return new Composition(uuid, engramId, lastUpdated, gameId);
    }

    @Override
    protected Composite buildComposite(String compositionId, JsonComposite jsonComposite) {
        String uuid = generateUUID();
        String name = jsonComposite.resource_id;
        String resourceId = getResourceUUIDByName(name);
        String engramId = getEngramUUIDByName(name);
        boolean isEngram = engramId != null;
        String sourceId = isEngram ? engramId : resourceId;
        String imageFile = isEngram ? getEngramImageFileByUUID(engramId) : getResourceImageFileByUUID(resourceId);
        int quantity = jsonComposite.quantity;
        String gameId = getDetailsObject().getUuid();

        if (imageFile == null) {
            Log.d("imageFile is null: " + name + " isEngram? " + isEngram + " / " + getEngramImageFileByUUID(engramId) + ", " + getResourceImageFileByUUID(resourceId));
            Log.d(uuid + ", " + name + ", " + resourceId + ", " + engramId + ", " + isEngram + ", " + sourceId + ", " + imageFile + ", " + quantity + ", " + gameId);
        }

        return new Composite(uuid, name, imageFile, quantity, sourceId, isEngram, compositionId, gameId);
    }

    @Override
    protected void mapStationDirectoryItem(Station station) {
        String uuid = !cDebug ? generateUUID() : station.getName();
        String name = station.getName();
        String imageFile = station.getImageFile();
        String sourceId = station.getUuid();
        String gameId = details.getUuid();

        int engramCount = mapEngramDirectory(station, 0, uuid);
        int folderCount = mapFolderDirectory(station, 0, uuid);
        int totalCount = engramCount + folderCount;

        if (totalCount > 0) {
            directory.add(new DirectoryItem(uuid, name, imageFile, cStationViewType, null, sourceId, gameId));
        } else {
            Log.d("mapStationDirectoryItem >> 0 count: " + station.getName() + " e:" + engramCount + "/f:" + folderCount);
        }

    }

    @Override
    protected void mapEngramDirectoryItem(Engram engram, String parentId) {
        String uuid = !cDebug ? generateUUID() : engram.getName();
        String sourceId = engram.getUuid();
        String name = engram.getName();
        String imageFile = engram.getImageFile();
        String gameId = details.getUuid();

        directory.add(new DirectoryItem(uuid, name, imageFile, cEngramViewType, parentId, sourceId, gameId));
    }

    @Override
    protected int mapFolderDirectoryItemByCategoryId(Station station, Folder folder, long categoryId, String parentId) {
        String uuid = !cDebug ? generateUUID() : folder.getName();
        String sourceId = folder.getUuid();
        String name = folder.getName();
        String imageFile = details.getFolderFile();
        String gameId = details.getUuid();

        int engramCount = mapEngramDirectory(station, categoryId, uuid);
        int folderCount = mapFolderDirectory(station, categoryId, uuid);
        int totalCount = engramCount + folderCount;

        if (totalCount > 0) {
            directory.add(new DirectoryItem(uuid, name, imageFile, cFolderViewType, parentId, sourceId, gameId));
        } else {
            Log.d("mapFolderDirectoryItem >> 0 count: " + station.getName() + "/" + folder.getName() + " e:" + engramCount + "/f:" + folderCount);
        }

        return totalCount;
    }

    @Override
    public JsonNode resolveToJson() {
        //  create game data node
        ObjectNode gameDataNode = mapper.createObjectNode();

        gameDataNode.set(cDetails, mapper.valueToTree(details));

        //  add resources, without complex resources
        gameDataNode.set(cResources, mapper.valueToTree(transformResourceMap()));

        //  add stations
        gameDataNode.set(cStations, mapper.valueToTree(transformStationMap()));

        //  add folders
        gameDataNode.set(cFolders, mapper.valueToTree(transformFolderMapByCategoryId()));

        //  add engrams
        gameDataNode.set(cEngrams, mapper.valueToTree(transformEngramMap()));

        //  add composition
        gameDataNode.set(cComposition, mapper.valueToTree(transformCompositionMap()));

        //  add composites
        gameDataNode.set(cComposites, mapper.valueToTree(transformCompositeMap()));

        //  add directory, traverse through tree, fill with uuids
        gameDataNode.set(cDirectory, mapper.valueToTree(directory));

        return gameDataNode;
    }
}
