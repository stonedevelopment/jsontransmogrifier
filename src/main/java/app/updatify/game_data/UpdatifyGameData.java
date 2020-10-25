package app.updatify.game_data;

import app.illuminate.model.*;
import app.illuminate.model.details.IlluminateDetails;
import app.updatify.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controller.GameData;
import model.*;
import util.JSONUtil;
import util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.Constants.*;

public class UpdatifyGameData extends GameData {
    private final JsonNode illuminationNode;
    private final Map<String, JsonNode> illuminationMap = new HashMap<>();
    private final List<String> uncheckedDirectoryItems = new ArrayList<>();
    private boolean hasUpdate = false;

    public UpdatifyGameData(JsonNode transmogrificationNode, JsonNode illuminationNode) {
        super(convertTransmogrificationNode(transmogrificationNode));
        this.illuminationNode = illuminationNode;
    }

    private static JsonNode convertTransmogrificationNode(JsonNode transmogrificationNode) {
        return JSONUtil.parseIn(cArkAssetsFilePath, transmogrificationNode.get(cFilePath).asText());
    }

    @Override
    public UpdatifyDetails getDetails() {
        return (UpdatifyDetails) super.getDetails();
    }

    public String getGameId() {
        return getDetails().getUuid();
    }

    protected JsonNode getTransmogNode() {
        return getInNode();
    }

    protected JsonNode getTransmogNode(String type) {
        return getTransmogNode().get(type);
    }

    protected JsonNode getIlluminatedNode(String type) {
        return illuminationMap.get(type);
    }

    private DirectoryItem getDirectoryItemByName(String name, String parentId) {
        for (DirectoryItem directoryItem : getDirectoryItemListByParentUUID(parentId)) {
            if (directoryItem.getName().equals(name)) return directoryItem;
        }
        return null;
    }

    /**
     * Helper method for top-level details to determine if whether or not information was updated
     *
     * @return if information has been updated
     */
    public boolean hasUpdate() {
        return hasUpdate;
    }

    protected void setHasUpdate() {
        this.hasUpdate = true;
    }

    @Override
    public void mapGameDataFromJson() {
        //  we first need to map out the illumination files
        mapIlluminationNode();

        createDetailsObject();
        mapResourcesFromJson();
        mapStationsFromJson();
        mapFoldersFromJson();
        mapEngramsFromJson();
        mapCompositionFromJson();
        mapCompositesFromJson();
        mapDirectoryFromJson();
    }

    protected void mapIlluminationNode() {
        illuminationNode.get(cIlluminatedFiles).forEach((illuminatedFile) -> {
            String type = illuminatedFile.get(cType).asText();
            String filePath = illuminatedFile.get(cFilePath).asText();
            JsonNode illuminatedNode = JSONUtil.parseIn(cArkAssetsFilePath, filePath);
            addIlluminatedNode(type, illuminatedNode);
        });
    }

    @Override
    protected void createDetailsObject() {
        //  instantiate
        UpdatifyDetails tDetails = UpdatifyDetails.fromJson(getTransmogNode().get(cDetails));
        setDetails(tDetails);

        // TODO: 10/18/2020 what if tDetails doesn't exist?

        //  compare
        IlluminateDetails iDetails = IlluminateDetails.fromJson(getIlluminatedNode(cDetails));
        if (!tDetails.equals(iDetails)) {
            setHasUpdate();
            updateDetails(UpdatifyDetails.convertToNew(tDetails, iDetails));
        }
    }

    @Override
    protected void mapResourcesFromJson() {
        //  map raw data
        getTransmogNode(cResources).forEach((tNode) -> {
            addResource(UpdatifyResource.with(Resource.fromJson(tNode), getGameId()));
        });

        //  map Illuminated data, updating if different
        getIlluminatedNode(cResources).forEach((iNode -> {
            IlluminateResource iResource = IlluminateResource.fromJson(iNode);
            Resource tResource = getResourceByName(iResource.getName());

            if (tResource == null) {
                setHasUpdate();
                addResource(UpdatifyResource.createFrom(iResource, getGameId()));
            } else if (!tResource.equals(iResource)) {
                setHasUpdate();
                updateResource(UpdatifyResource.updateToNew(tResource, iResource, getGameId()));
            }
        }));
    }

    @Override
    protected void mapStationsFromJson() {
        //  map raw data
        getTransmogNode(cStations).forEach((tNode) -> {
            addStation(UpdatifyStation.with(Station.fromJson(tNode), getGameId()));
        });

        //  map Illuminated data, updating if different
        getIlluminatedNode(cStations).forEach((iNode -> {
            IlluminateStation iStation = IlluminateStation.fromJson(iNode);
            Station tStation = getStationByName(iStation.getName());

            if (tStation == null) {
                setHasUpdate();
                addStation(UpdatifyStation.createFrom(iStation, getGameId()));
            } else if (!tStation.equals(iStation)) {
                setHasUpdate();
                updateStation(UpdatifyStation.updateToNew(tStation, iStation, getGameId()));
            }
        }));
    }

    @Override
    protected void mapFoldersFromJson() {
        //  map raw data
        getTransmogNode(cFolders).forEach((tNode) -> {
            addFolder(UpdatifyFolder.with(Folder.fromJson(tNode), getGameId()));
        });

        //  map Illuminated data, updating if different
        getIlluminatedNode(cFolders).forEach((iNode) -> {
            IlluminateFolder iFolder = IlluminateFolder.fromJson(iNode);
            Folder tFolder = getFolderByName(iFolder.getName());

            if (tFolder == null) {
                setHasUpdate();
                addFolder(UpdatifyFolder.createFrom(iFolder, getGameId()));
            } else if (!tFolder.equals(iFolder)) {
                setHasUpdate();
                updateFolder(UpdatifyFolder.updateToNew(tFolder, iFolder, getGameId()));
            }
        });
    }

    @Override
    protected void mapEngramsFromJson() {
        //  map raw data
        getTransmogNode(cEngrams).forEach((tNode) -> {
            addEngram(UpdatifyEngram.with(Engram.fromJson(tNode), getGameId()));
        });

        //  map Illuminated data, updating if different
        getIlluminatedNode(cEngrams).forEach((iNode) -> {
            IlluminateEngram iEngram = UpdatifyEngram.fromJson(iNode);
            Engram tEngram = getEngramByName(iEngram.getName());

            if (tEngram == null) {
                setHasUpdate();
                addEngram(UpdatifyEngram.createFrom(iEngram, getGameId()));
            } else if (!tEngram.equals(iEngram)) {
                setHasUpdate();
                updateEngram(UpdatifyEngram.updateToNew(tEngram, iEngram, getGameId()));
            }
        });
    }

    @Override
    protected void mapCompositionFromJson() {
        //  map raw data
        getTransmogNode(cComposition).forEach((tNode) -> {
            addComposition(UpdatifyComposition.with(Composition.fromJson(tNode), getGameId()));
        });

        //  illuminated data will map during the map composites method
    }

    protected void mapCompositesFromJson() {
        //  map raw data
        getTransmogNode(cComposites).forEach((tNode) -> {
            addComposite(UpdatifyComposite.with(Composite.fromJson(tNode), getGameId()));
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
                Composition composition = UpdatifyComposition.createFrom(IlluminateComposition.with(engramId),
                        getGameId());
                //  add new composition to map
                addComposition(composition);

                //  iterate through composites node
                compositesNode.forEach((compositeNode) -> {
                    IlluminateComposite iComposite = IlluminateComposite.fromJson(compositeNode);
                    String imageFile = getImageFileByName(name);
                    addComposite(UpdatifyComposite.createFrom(iComposite, imageFile, engramId, composition.getUuid(),
                            getGameId()));
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
                                updateComposite(UpdatifyComposite.updateToNew(tComposite, iComposite, getGameId()));
                            }
                        }
                    });
                });
            }
        });
    }

    @Override
    protected void mapDirectoryFromJson() {
        getTransmogNode(cDirectory).forEach((tNode) -> {
            addUncheckedDirectoryItem(UpdatifyDirectoryItem.with(DirectoryItem.fromJson(tNode), getGameId()));
        });

        getIlluminatedNode(cDirectory).forEach((iNode) -> {
            String name = iNode.get(cName).asText();
            DirectoryItem tDirectoryItem = getDirectoryItemByName(name, null);
            if (tDirectoryItem == null) {
                //  we have a new directory or a directory that was moved
                // TODO: 9/26/2020 check if moved, treat as new for now
                Station station = getStationByName(name);
                tDirectoryItem = UpdatifyDirectoryItem.createFromStation(station, getGameId());
                setHasUpdate();
                addDirectoryItem(tDirectoryItem);
            } else {
                //  mapped directory
                //  remove from uncheck list
                removeUncheckedDirectoryItem(tDirectoryItem.getUuid());
            }

            //  map station's hierarchy
            mapIlluminatedDirectory(iNode, tDirectoryItem.getUuid());
        });

        //  check for unchecked directory items, remove from lists
        for (String uuid : uncheckedDirectoryItems) {
            Log.d("Removing unchecked DirectoryItem: " + uuid);
            // TODO: 9/26/2020 Remove unchecked DirectoryItems
        }
    }

    private void mapIlluminatedDirectory(JsonNode iNode, String parentId) {
        iNode.get(cFolders).forEach((folderNode) -> {
            String name = folderNode.get(cName).asText();
            DirectoryItem fDirectoryItem = getDirectoryItemByName(name, parentId);
            if (fDirectoryItem == null) {
                Folder folder = getFolderByName(name);
                fDirectoryItem = UpdatifyDirectoryItem.createFromFolder(folder, parentId, getGameId());
                Log.d("Adding Folder DirectoryItem: " + fDirectoryItem.toString());
                setHasUpdate();
                addDirectoryItem(fDirectoryItem);
            } else {
                removeUncheckedDirectoryItem(fDirectoryItem.getUuid());
            }

            //  map folder's hierarchy
            mapIlluminatedDirectory(folderNode, fDirectoryItem.getUuid());
        });

        iNode.get(cEngrams).forEach((engramNode) -> {
            String name = engramNode.asText();
            DirectoryItem eDirectoryItem = getDirectoryItemByName(name, parentId);
            if (eDirectoryItem == null) {
                Engram engram = getEngramByName(name);
                eDirectoryItem = UpdatifyDirectoryItem.createFromEngram(engram, parentId, getGameId());
                Log.d("Adding Engram DirectoryItem: " + eDirectoryItem.toString());
                setHasUpdate();
                addDirectoryItem(eDirectoryItem);
            } else {
                removeUncheckedDirectoryItem(eDirectoryItem.getUuid());
            }
        });
    }

    private void addIlluminatedNode(String type, JsonNode illuminatedNode) {
        illuminationMap.put(type, illuminatedNode);
    }

    protected void addComposition(Composition composition) {
        String name = getEngramNameByUUID(composition.getEngramId());
        addComposition(name, composition);
    }

    /**
     * Add DirectoryItem, as normal, but we add its UUID to an unchecked list for future reference.
     * This list will be used to test if a directory item was moved or removed.
     */
    private void addUncheckedDirectoryItem(DirectoryItem directoryItem) {
        uncheckedDirectoryItems.add(directoryItem.getUuid());
        addDirectoryItem(directoryItem);
    }

    private void removeUncheckedDirectoryItem(String uuid) {
        uncheckedDirectoryItems.remove(uuid);
    }

    protected void updateResource(Resource resource) {
        addResourceToMap(resource.getUuid(), resource);
    }

    protected void updateStation(Station station) {
        addStationToMap(station.getUuid(), station);
    }

    protected void updateFolder(Folder folder) {
        addFolderToMap(folder.getUuid(), folder);
    }

    protected void updateEngram(Engram engram) {
        addEngramToMap(engram.getUuid(), engram);
    }

    protected void updateComposite(Composite composite) {
        addCompositeToMap(composite.getUuid(), composite);
    }

    @Override
    public ObjectNode resolveToJson() {
        ObjectNode outNode = mapper.createObjectNode();

        outNode.set(cDetails, mapper.valueToTree(getDetails()));

        //  add resources, without complex resources
        outNode.set(cResources, mapper.valueToTree(transformResourceMap()));

        //  add stations
        outNode.set(cStations, mapper.valueToTree(transformStationMap()));

        //  add folders
        outNode.set(cFolders, mapper.valueToTree(transformFolderMap()));

        //  add engrams
        outNode.set(cEngrams, mapper.valueToTree(transformEngramMap()));

        //  add composition
        outNode.set(cComposition, mapper.valueToTree(transformCompositionMap()));

        //  add composites
        outNode.set(cComposites, flattenCompositeMapToJson(transformCompositeMap()));

        //  add directory, traverse through tree, fill with uuids
        outNode.set(cDirectory, flattenDirectoryToJson(transformDirectory()));

        return outNode;
    }
}