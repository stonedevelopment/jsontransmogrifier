package app.updatify.game_data;

import app.illuminate.model.details.IlluminateDlcDetails;
import app.updatify.model.UpdatifyBlacklistItem;
import app.updatify.model.UpdatifyDlcDetails;
import app.updatify.model.UpdatifyTotalConversionItem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.Engram;
import model.Folder;
import model.Resource;
import model.Station;
import util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.Constants.*;

public class UpdatifyDlcGameData extends UpdatifyGameData {
    private final UpdatifyGameData primaryGameData;
    private final Map<String, List<UpdatifyBlacklistItem>> blacklistMap = new HashMap<>();
    private final List<UpdatifyTotalConversionItem> totalConversionItemList = new ArrayList<>();

    public UpdatifyDlcGameData(JsonNode transmogrificationNode, JsonNode illuminationNode, UpdatifyGameData primaryGameData) {
        super(transmogrificationNode, illuminationNode);
        this.primaryGameData = primaryGameData;
    }

    @Override
    public UpdatifyDlcDetails getDetails() {
        return (UpdatifyDlcDetails) super.getDetails();
    }

    @Override
    public Resource getResourceByName(String name) {
        Resource resource = super.getResourceByName(name);
        if (resource == null) resource = primaryGameData.getResourceByName(name);
        return resource;
    }

    @Override
    public Station getStationByName(String name) {
        Station station = super.getStationByName(name);
        if (station == null) station = primaryGameData.getStationByName(name);
        return station;
    }

    @Override
    public Folder getFolderByName(String name) {
        Folder folder = super.getFolderByName(name);
        if (folder == null) folder = primaryGameData.getFolderByName(name);
        return folder;
    }

    @Override
    public Engram getEngramByName(String name) {
        Engram engram = super.getEngramByName(name);
        if (engram == null) engram = primaryGameData.getEngramByName(name);
        return engram;
    }

    private boolean isTotalConversion() {
        return getDetails().isTotalConversion();
    }

    @Override
    protected void createDetailsObject() {
        UpdatifyDlcDetails tDetails = UpdatifyDlcDetails.fromJson(getTransmogNode().get(cDetails));
        setDetails(tDetails);

        //  compare
        IlluminateDlcDetails iDetails = IlluminateDlcDetails.fromJson(getIlluminatedNode(cDetails));
        if (!tDetails.equals(iDetails)) {
            updateDetails(UpdatifyDlcDetails.convertToNew(tDetails, iDetails));
            setHasUpdate();
        } else {
            Log.d("No change to details for " + getDetails().getName());
        }
    }

    @Override
    public void mapGameDataFromJson() {
        super.mapGameDataFromJson();
        mapBlackListFromJson();
        if (isTotalConversion()) {
            mapTotalConversion();
        }
    }

    /**
     * "resources": [
     * "Metal Ore",
     * "Raw Meat",
     * "Spoiled Meat"
     * ],
     */
    private void mapBlackListFromJson() {
        JsonNode blacklistNode = getIlluminatedNode(cBlacklist);

        //  map resource blacklist
        blacklistNode.get(cResources).forEach((nameNode) -> {
            String name = nameNode.asText();
            addBlacklistItem(cResources, UpdatifyBlacklistItem.createFrom(name, getResourceByName(name).getUuid()));
        });

        //  map station blacklist
        blacklistNode.get(cStations).forEach((nameNode) -> {
            String name = nameNode.asText();
            addBlacklistItem(cStations, UpdatifyBlacklistItem.createFrom(name, getStationByName(name).getUuid()));
        });

        //  map folder blacklist
        blacklistNode.get(cFolders).forEach((nameNode) -> {
            String name = nameNode.asText();
            addBlacklistItem(cFolders, UpdatifyBlacklistItem.createFrom(name, getFolderByName(name).getUuid()));
        });

        //  map engram blacklist
        blacklistNode.get(cEngrams).forEach((nameNode) -> {
            String name = nameNode.asText();
            addBlacklistItem(cEngrams, UpdatifyBlacklistItem.createFrom(name, getEngramByName(name).getUuid()));
        });
    }

    /**
     * [
     * {
     * "from": "Metal Ore",
     * "to": "Iron Ore"
     * }
     * ]
     */
    private void mapTotalConversion() {
        JsonNode totalConversionNodes = getIlluminatedNode(cTotalConversion);
        for (JsonNode totalConversionNode : totalConversionNodes) {
            String fromName = totalConversionNode.get(cFrom).asText();
            Resource fromResource = getResourceByName(fromName);

            String toName = totalConversionNode.get(cTo).asText();
            Resource toResource = getResourceByName(toName);

            addTotalConversionItem(UpdatifyTotalConversionItem.createFrom(fromResource, toResource));
        }
    }

    private void addBlacklistItem(String type, UpdatifyBlacklistItem blacklistItem) {
        List<UpdatifyBlacklistItem> blacklistItemList = blacklistMap.get(type);
        if (blacklistItemList == null) {
            blacklistItemList = new ArrayList<>();
        }
        blacklistItemList.add(blacklistItem);
        blacklistMap.put(type, blacklistItemList);
    }

    private void addTotalConversionItem(UpdatifyTotalConversionItem totalConversionItem) {
        totalConversionItemList.add(totalConversionItem);
    }

    @Override
    public ObjectNode resolveToJson() {
        ObjectNode outNode = super.resolveToJson();

        outNode.set(cBlacklist, transformBlacklistMap());

        if (isTotalConversion()) {
            outNode.set(cTotalConversion, transformTotalConversionList());
        }

        return outNode;
    }

    private JsonNode transformBlacklistMap() {
        ObjectNode outNode = mapper.createObjectNode();
        outNode.set(cResources, mapper.createArrayNode().add((JsonNode) blacklistMap.get(cResources).stream().map((mapper::valueToTree))));
        outNode.set(cStations, mapper.createArrayNode().add((JsonNode) blacklistMap.get(cStations).stream().map((mapper::valueToTree))));
        outNode.set(cFolders, mapper.createArrayNode().add((JsonNode) blacklistMap.get(cFolders).stream().map((mapper::valueToTree))));
        outNode.set(cEngrams, mapper.createArrayNode().add((JsonNode) blacklistMap.get(cEngrams).stream().map((mapper::valueToTree))));
        return outNode;
    }

    private JsonNode transformTotalConversionList() {
        return mapper.createArrayNode().add((JsonNode) totalConversionItemList.stream().map(mapper::valueToTree));
    }
}