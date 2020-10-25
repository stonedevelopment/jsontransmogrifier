package app.updatify.game_data;

import app.illuminate.model.*;
import app.illuminate.model.details.IlluminateDlcDetails;
import app.updatify.model.UpdatifyBlacklistItem;
import app.updatify.model.UpdatifyEngram;
import app.updatify.model.UpdatifyTotalConversionItem;
import app.updatify.model.dlc.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.*;
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

    public String getPrimaryGameId() {
        return primaryGameData.getGameId();
    }

    private Resource getPrimaryResourceByName(String name) {
        return primaryGameData.getResourceByName(name);
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

    @Override
    public String getCompositionUUIDByName(String name) {
        String uuid = super.getCompositionUUIDByName(name);
        if (uuid == null) {
            return primaryGameData.getCompositionUUIDByName(name);
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
            updateDetails(UpdatifyDlcDetails.convertToNew(tDetails, iDetails, primaryGameData.getGameId()));
            setHasUpdate();
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

    @Override
    protected void mapResourcesFromJson() {
        //  map raw data
        getTransmogNode(cResources).forEach((tNode) -> {
            addResource(UpdatifyDlcResource.with(Resource.fromJson(tNode), getPrimaryGameId(), getGameId()));
        });

        //  map Illuminated data, updating if different
        getIlluminatedNode(cResources).forEach((iNode -> {
            IlluminateResource iResource = IlluminateResource.fromJson(iNode);
            Resource tResource = getResourceByName(iResource.getName());

            if (tResource == null) {
                setHasUpdate();
                addResource(UpdatifyDlcResource.createFrom(iResource, getPrimaryGameId(), getGameId()));
            } else if (!tResource.equals(iResource)) {
                setHasUpdate();
                updateResource(UpdatifyDlcResource.updateToNew(tResource, iResource, getPrimaryGameId(), getGameId()));
            }
        }));
    }

    @Override
    protected void mapStationsFromJson() {
        //  map raw data
        getTransmogNode(cStations).forEach((tNode) -> {
            addStation(UpdatifyDlcStation.with(Station.fromJson(tNode), getPrimaryGameId(), getGameId()));
        });

        //  map Illuminated data, updating if different
        getIlluminatedNode(cStations).forEach((iNode -> {
            IlluminateStation iStation = IlluminateStation.fromJson(iNode);
            Station tStation = getStationByName(iStation.getName());

            if (tStation == null) {
                setHasUpdate();
                addStation(UpdatifyDlcStation.createFrom(iStation, getPrimaryGameId(), getGameId()));
            } else if (!tStation.equals(iStation)) {
                setHasUpdate();
                updateStation(UpdatifyDlcStation.updateToNew(tStation, iStation, getPrimaryGameId(), getGameId()));
            }
        }));
    }

    @Override
    protected void mapFoldersFromJson() {
        //  map raw data
        getTransmogNode(cFolders).forEach((tNode) -> {
            addFolder(UpdatifyDlcFolder.with(Folder.fromJson(tNode), getPrimaryGameId(), getGameId()));
        });

        //  map Illuminated data, updating if different
        getIlluminatedNode(cFolders).forEach((iNode) -> {
            IlluminateFolder iFolder = IlluminateFolder.fromJson(iNode);
            Folder tFolder = getFolderByName(iFolder.getName());

            if (tFolder == null) {
                setHasUpdate();
                addFolder(UpdatifyDlcFolder.createFrom(iFolder, getPrimaryGameId(), getGameId()));
            } else if (!tFolder.equals(iFolder)) {
                setHasUpdate();
                updateFolder(UpdatifyDlcFolder.updateToNew(tFolder, iFolder, getPrimaryGameId(), getGameId()));
            }
        });
    }

    @Override
    protected void mapEngramsFromJson() {
        //  map raw data
        getTransmogNode(cEngrams).forEach((tNode) -> {
            addEngram(UpdatifyDlcEngram.with(Engram.fromJson(tNode), getPrimaryGameId(), getGameId()));
        });

        //  map Illuminated data, updating if different
        getIlluminatedNode(cEngrams).forEach((iNode) -> {
            IlluminateEngram iEngram = UpdatifyEngram.fromJson(iNode);
            Engram tEngram = getEngramByName(iEngram.getName());

            if (tEngram == null) {
                setHasUpdate();
                addEngram(UpdatifyDlcEngram.createFrom(iEngram, getPrimaryGameId(), getGameId()));
            } else if (!tEngram.equals(iEngram)) {
                setHasUpdate();
                updateEngram(UpdatifyDlcEngram.updateToNew(tEngram, iEngram, getPrimaryGameId(), getGameId()));
            }
        });
    }

    @Override
    protected void mapCompositionFromJson() {
        //  map raw data
        getTransmogNode(cComposition).forEach((tNode) -> {
            addComposition(UpdatifyDlcComposition.with(Composition.fromJson(tNode), getPrimaryGameId(), getGameId()));
        });

        //  illuminated data will map during the map composites method
    }

    protected void mapCompositesFromJson() {
        //  map raw data
        getTransmogNode(cComposites).forEach((tNode) -> {
            addComposite(UpdatifyDlcComposite.with(Composite.fromJson(tNode), getPrimaryGameId(), getGameId()));
        });

        //  map Illuminated data, updating if different
        getIlluminatedNode(cEngrams).forEach((iNode) -> {
            String name = iNode.get(cName).asText();
            String compositionId = getCompositionUUIDByName(name);

            JsonNode compositionNode = iNode.get(cComposition);
            JsonNode compositesNode = compositionNode.get(cComposites);

            if (compositionId == null) {
                Log.d("Composition not found: " + name);
                //  flag for update
                setHasUpdate();

                //  create new composition
                String engramId = getEngramUUIDByName(name);
                UpdatifyDlcComposition composition = UpdatifyDlcComposition.createFrom(IlluminateComposition.with(engramId),
                        getPrimaryGameId(), getGameId());
                //  add composition to map
                addComposition(composition);

                //  iterate through composites node
                compositesNode.forEach((compositeNode) -> {
                    IlluminateComposite iComposite = IlluminateComposite.fromJson(compositeNode);
                    String imageFile = getImageFileByName(name);
                    addComposite(UpdatifyDlcComposite.createFrom(iComposite, imageFile, engramId, composition.getUuid(),
                            getPrimaryGameId(), getGameId()));
                });
            } else {
                //  iterate through composites node
                compositesNode.forEach((compositeNode) -> {
                    IlluminateComposite iComposite = IlluminateComposite.fromJson(compositeNode);
                    getCompositeUUIDListByName(iComposite.getName()).forEach((compositeId) -> {
                        Composite tComposite = getComposite(compositeId);
                        if (compositionId.equals(tComposite.getCompositionId())) {
                            if (!tComposite.equals(iComposite)) {
                                setHasUpdate();
                                updateComposite(UpdatifyDlcComposite.updateToNew(tComposite, iComposite,
                                        getPrimaryGameId(), getGameId()));
                            }
                        }
                    });
                });
            }
        });
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
            addBlacklistItem(cResources, UpdatifyBlacklistItem.createFrom(
                    getPrimaryResourceByName(nameNode.asText()).getUuid(), getGameId()));
        });

        //  map station blacklist
        blacklistNode.get(cStations).forEach((nameNode) -> {
            addBlacklistItem(cStations, UpdatifyBlacklistItem.createFrom(
                    getPrimaryStationByName(nameNode.asText()).getUuid(), getGameId()));
        });

        //  map folder blacklist
        blacklistNode.get(cFolders).forEach((nameNode) -> {
            addBlacklistItem(cFolders, UpdatifyBlacklistItem.createFrom(
                    getPrimaryFolderByName(nameNode.asText()).getUuid(), getGameId()));
        });

        //  map engram blacklist
        blacklistNode.get(cEngrams).forEach((nameNode) -> {
            addBlacklistItem(cEngrams, UpdatifyBlacklistItem.createFrom(
                    getPrimaryEngramByName(nameNode.asText()).getUuid(), getGameId()));
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
        getIlluminatedNode(cTotalConversion).forEach((totalConversionNode) -> {
            String fromName = totalConversionNode.get(cFrom).asText();
            List<String> fromCompositeList = getPrimaryCompositeUUIDListByName(fromName);

            String toName = totalConversionNode.get(cTo).asText();
            String toSourceId = getUUIDByName(toName);

            //  convert compositions?
            fromCompositeList.forEach((compositeId) -> {
                Composite fromComposite = getPrimaryComposite(compositeId);
                Composite toComposite = UpdatifyDlcComposite.convertToNew(fromComposite, toName, toSourceId, getPrimaryGameId(), getGameId());
                addTotalConversionItem(UpdatifyTotalConversionItem.createFrom(fromComposite, toComposite, getPrimaryGameId(), getGameId()));
            });

        });
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
        outNode.set(cResources, mapper.valueToTree(getBlacklistItemList(cResources)));
        outNode.set(cStations, mapper.valueToTree(getBlacklistItemList(cStations)));
        outNode.set(cFolders, mapper.valueToTree(getBlacklistItemList(cFolders)));
        outNode.set(cEngrams, mapper.valueToTree(getBlacklistItemList(cEngrams)));
        return outNode;
    }

    private JsonNode transformTotalConversionList() {
        return mapper.valueToTree(totalConversionItemList);
    }
}