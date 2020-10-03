package app.updatify.game_data;

import app.illuminate.model.details.IlluminateDlcDetails;
import app.updatify.model.UpdatifyBlacklistItem;
import app.updatify.model.UpdatifyDlcComposite;
import app.updatify.model.UpdatifyDlcDetails;
import app.updatify.model.UpdatifyTotalConversionItem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.Composite;
import model.Engram;
import model.Folder;
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

    public Map<String, List<UpdatifyBlacklistItem>> getBlacklistMap() {
        return blacklistMap;
    }

    public List<UpdatifyBlacklistItem> getBlacklistItemList(String type) {
        List<UpdatifyBlacklistItem> blacklistItemList = getBlacklistMap().get(type);
        return blacklistItemList == null ? new ArrayList<>() : blacklistItemList;
    }

    private Station getPrimaryStationByName(String name) {
        return primaryGameData.getStationByName(name);
    }

    private Folder getPrimaryFolderByName(String name) {
        return primaryGameData.getFolderByName(name);
    }

    private Engram getPrimaryEngramByName(String name) {
        return primaryGameData.getEngramByName(name);
    }

    private List<String> getPrimaryCompositeUUIDListByName(String name) {
        return primaryGameData.getCompositeUUIDListByName(name);
    }

    private Composite getPrimaryComposite(String uuid) {
        return primaryGameData.getComposite(uuid);
    }

    private String getUUIDByName(String name) {
        String uuid = getResourceUUIDByName(name);
        if (uuid == null) {
            uuid = getEngramUUIDByName(name);
        }
        return uuid;
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

        //  map station blacklist
        blacklistNode.get(cStations).forEach((nameNode) -> {
            addBlacklistItem(cStations, UpdatifyBlacklistItem.createFrom(getPrimaryStationByName(nameNode.asText()).getUuid()));
        });

        //  map folder blacklist
        blacklistNode.get(cFolders).forEach((nameNode) -> {
            addBlacklistItem(cFolders, UpdatifyBlacklistItem.createFrom(getPrimaryFolderByName(nameNode.asText()).getUuid()));
        });

        //  map engram blacklist
        blacklistNode.get(cEngrams).forEach((nameNode) -> {
            addBlacklistItem(cEngrams, UpdatifyBlacklistItem.createFrom(getPrimaryEngramByName(nameNode.asText()).getUuid()));
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
            List<String> fromCompositeList = getPrimaryCompositeUUIDListByName(fromName);

            String toName = totalConversionNode.get(cTo).asText();
            String toSourceId = getUUIDByName(toName);

            //  convert compositions?
            fromCompositeList.forEach((compositeId) -> {
                Composite fromComposite = getPrimaryComposite(compositeId);
                Composite toComposite = UpdatifyDlcComposite.convertToNew(fromComposite, toName, toSourceId);
                addTotalConversionItem(UpdatifyTotalConversionItem.createFrom(fromComposite, toComposite));
            });

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
        outNode.set(cStations, mapper.valueToTree(getBlacklistItemList(cStations)));
        outNode.set(cFolders, mapper.valueToTree(getBlacklistItemList(cFolders)));
        outNode.set(cEngrams, mapper.valueToTree(getBlacklistItemList(cEngrams)));
        return outNode;
    }

    private JsonNode transformTotalConversionList() {
        return mapper.valueToTree(totalConversionItemList);
    }
}