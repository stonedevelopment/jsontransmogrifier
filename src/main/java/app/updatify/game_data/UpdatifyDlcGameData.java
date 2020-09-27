package app.updatify.game_data;

import app.illuminate.model.details.IlluminateDlcDetails;
import app.updatify.model.UpdatifyBlacklistItem;
import app.updatify.model.UpdatifyDetails;
import app.updatify.model.UpdatifyDlcDetails;
import com.fasterxml.jackson.databind.JsonNode;
import util.Log;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static util.Constants.*;

public class UpdatifyDlcGameData extends UpdatifyGameData {
    private final Map<String, List<UpdatifyBlacklistItem>> blacklistMap = new TreeMap<>();

    public UpdatifyDlcGameData(JsonNode transmogrificationNode, JsonNode illuminationNode) {
        super(transmogrificationNode, illuminationNode);
    }

    @Override
    public UpdatifyDlcDetails getDetails() {
        return (UpdatifyDlcDetails) super.getDetails();
    }

    private boolean isTotalConversion() {
        return getDetails().isTotalConversion();
    }

    @Override
    protected void createDetailsObject() {
        //  instantiate
        UpdatifyDlcDetails tDetails = UpdatifyDlcDetails.fromJson(getTransmogNode().get(cDetails));
        setDetails(tDetails);

        //  compare
        IlluminateDlcDetails iDetails = IlluminateDlcDetails.fromJson(getIlluminatedNode(cDetails));
        if (!tDetails.equals(iDetails)) {
            updateDetails(UpdatifyDetails.convertToNew(tDetails, iDetails));
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
     * "stations": [],
     * "folders": [],
     * "engrams": [
     * "Canteen",
     * "Metal Ingot"
     * ]
     */
    private void mapBlackListFromJson() {
        JsonNode blacklistNode = getIlluminatedNode(cBlacklist);

        //  map resource blacklist
        blacklistNode.findValuesAsText(cResources).forEach((name) -> {
            addBlacklistItem(cResources, UpdatifyBlacklistItem.createFrom(name, getResourceByName(name).getUuid()));
        });

        //  map station blacklist
        blacklistNode.findValuesAsText(cStations).forEach((name) -> {
            addBlacklistItem(cStations, UpdatifyBlacklistItem.createFrom(name, getStationByName(name).getUuid()));
        });

        //  map folder blacklist
        blacklistNode.findValuesAsText(cFolders).forEach((name) -> {
            addBlacklistItem(cFolders, UpdatifyBlacklistItem.createFrom(name, getFolderByName(name).getUuid()));
        });

        //  map engram blacklist
        blacklistNode.findValuesAsText(cEngrams).forEach((name) -> {
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
        JsonNode totalConversionNode = getIlluminatedNode(cTotalConversion);
        //  check if exists
    }

    private void addBlacklistItem(String type, UpdatifyBlacklistItem blacklistItem) {
        List<UpdatifyBlacklistItem> blacklistItemList = blacklistMap.get(type);
        blacklistItemList.add(blacklistItem);
        blacklistMap.put(type, blacklistItemList);
    }
}
