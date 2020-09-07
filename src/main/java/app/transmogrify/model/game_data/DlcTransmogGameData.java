package app.transmogrify.model.game_data;

import app.transmogrify.controller.TransmogGameData;
import app.transmogrify.model.details.DlcTransmogDetails;
import app.transmogrify.model.json.JsonComposite;
import app.transmogrify.model.json.JsonDlc;
import app.transmogrify.model.json.JsonTotalConversion;
import app.transmogrify.model.json.JsonTotalConversionResource;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.*;
import model.dlc.TotalConversion;

import java.util.Map;

import static util.Constants.*;

public class DlcTransmogGameData extends TransmogGameData {
    private final PrimaryTransmogGameData primaryGameData;
    private final TotalConversion totalConversion = new TotalConversion();

    private DlcTransmogGameData(JsonNode inNode, JsonDlc jsonDlc, PrimaryTransmogGameData primaryGameData) {
        super(inNode, jsonDlc);

        this.primaryGameData = primaryGameData;
        mapGameDataFromJson();
    }

    public static DlcTransmogGameData with(JsonNode inNode, JsonDlc jsonDlc, PrimaryTransmogGameData primaryGameData) {
        return new DlcTransmogGameData(inNode, jsonDlc, primaryGameData);
    }

    @Override
    public DlcTransmogDetails getDetailsObject() {
        return (DlcTransmogDetails) details;
    }

    @Override
    protected void createDetailsObject() {
        this.details = DlcTransmogDetails.with(jsonDlc);
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
    public String getFolderUUIDByCategoryId(long categoryId) {
        String uuid = super.getFolderUUIDByCategoryId(categoryId);
        return uuid != null ? uuid : primaryGameData.getFolderUUIDByCategoryId(categoryId);
    }

    @Override
    public Folder getFolderByCategoryId(long categoryId) {
        Folder folder = super.getFolderByCategoryId(categoryId);
        return folder != null ? folder : primaryGameData.getFolderByCategoryId(categoryId);
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
    public Composite getComposite(String uuid) {
        Composite composite = super.getComposite(uuid);
        return composite != null ? composite : primaryGameData.getComposite(uuid);
    }

    /**
     * Helper method that crawls through the Resource map for a UUID, then the Engram map if not found.
     * There should only be 2 locations to search for since all Engrams used in a composition used to be considered
     * Resources.
     *
     * @param name Name of replacement object to search for
     * @return UUID of found replacement object
     */
    public String getReplacementUUIDByName(String name) {
        String uuid = getResourceUUIDByName(name);
        return uuid != null ? uuid : getEngramUUIDByName(name);
    }

    @Override
    protected void mapGameDataFromJson() {
        mapTotalConversionFromJson();
        super.mapGameDataFromJson();
    }

    @Override
    protected boolean isValidDlcIdForDirectory(long dlcId) {
        return isValidDlcId(dlcId) || primaryGameData.isValidDlcIdForDirectory(dlcId);
    }

    @Override
    public boolean isCompositeUnique(String compositionId, String name, JsonComposite jsonComposite) {
        return primaryGameData.isCompositeUnique(compositionId, name, jsonComposite) &&
                super.isCompositeUnique(compositionId, name, jsonComposite);
    }

    private void mapTotalConversionFromJson() {
        JsonNode totalConversionArray = inNode.get(cDlcTypeTotalConversion);
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

    private JsonNode createRemoveSection() {
        ObjectNode outNode = mapper.createObjectNode();

        //  add uuids of resources to remove
        ArrayNode resourceArray = mapper.createArrayNode();
        outNode.set(cResources, resourceArray);

        //  add uuids of stations to remove
        ArrayNode stationArray = mapper.createArrayNode();
        for (String stationName : totalConversion.stationsToRemove) {
            stationArray.add(getStationUUIDByName(stationName));
        }
        outNode.set(cStations, stationArray);

        //  add uuids of folders to remove
        ArrayNode folderArray = mapper.createArrayNode();
        for (long categoryId : totalConversion.foldersToRemove) {
            folderArray.add(getFolderUUIDByCategoryId(categoryId));
        }
        outNode.set(cFolders, folderArray);

        //  add uuids of engrams to remove
        ArrayNode engramArray = mapper.createArrayNode();
        for (String engramName : totalConversion.engramsToRemove) {
            engramArray.add(getEngramUUIDByName(engramName));
        }
        outNode.set(cEngrams, engramArray);

        return outNode;
    }

    private JsonNode createReplaceSection() {
        ObjectNode outNode = mapper.createObjectNode();

        //  add uuids of resources to replace
        ArrayNode resourceArray = mapper.createArrayNode();
        for (Map.Entry<String, String> resourceEntry : totalConversion.resourcesToReplace.entrySet()) {
            String from = getReplacementUUIDByName(resourceEntry.getKey());
            String to = getReplacementUUIDByName(resourceEntry.getValue());

            ObjectNode resourceNode = mapper.createObjectNode();
            resourceNode.put(cFrom, from);
            resourceNode.put(cTo, to);

            resourceArray.add(resourceNode);
        }
        outNode.set(cResources, resourceArray);

        outNode.set(cStations, mapper.createArrayNode());
        outNode.set(cFolders, mapper.createArrayNode());
        outNode.set(cEngrams, mapper.createArrayNode());

        return outNode;
    }

    @Override
    public JsonNode resolveToJson() {
        ObjectNode gameDataObject = mapper.createObjectNode();

        gameDataObject.set(cDetails, mapper.valueToTree(getDetailsObject()));

        //  add resources, without complex resources
        gameDataObject.set(cResources, mapper.valueToTree(transformResourceMap()));

        //  add stations
        gameDataObject.set(cStations, mapper.valueToTree(transformStationMap()));

        //  add folders
        gameDataObject.set(cFolders, mapper.valueToTree(transformFolderMap()));

        //  add engrams
        gameDataObject.set(cEngrams, mapper.valueToTree(transformEngramMap()));

        //  add composition
        gameDataObject.set(cComposition, mapper.valueToTree(transformCompositionMap()));

        //  add composites
        gameDataObject.set(cComposites, flattenCompositeMapToJson(transformCompositeMap()));

        //  add directory, traverse through tree, fill with uuids
        gameDataObject.set(cDirectory, flattenDirectoryToJson(transformDirectory()));

        //  add remove section
        gameDataObject.set(cRemove, createRemoveSection());

        //  add replace section
        gameDataObject.set(cReplace, createReplaceSection());

        return gameDataObject;
    }
}