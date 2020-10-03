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

    protected JsonNode getTransmogNode() {
        return getInNode();
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
    private boolean hasUpdated() {
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
        for (JsonNode illuminatedFile : illuminationNode.get(cIlluminatedFiles)) {
            String type = illuminatedFile.get(cType).asText();
            String filePath = illuminatedFile.get(cFilePath).asText();
            JsonNode illuminatedNode = JSONUtil.parseIn(cArkAssetsFilePath, filePath);
            addIlluminatedNode(type, illuminatedNode);
        }
    }

    @Override
    protected void createDetailsObject() {
        //  instantiate
        UpdatifyDetails tDetails = UpdatifyDetails.fromJson(getTransmogNode().get(cDetails));
        setDetails(tDetails);

        //  compare
        IlluminateDetails iDetails = IlluminateDetails.fromJson(getIlluminatedNode(cDetails));
        if (!tDetails.equals(iDetails)) {
            updateDetails(UpdatifyDetails.convertToNew(tDetails, iDetails));
            setHasUpdate();
        } else {
            Log.d("No change to details for " + getDetails().getName());
        }
    }

    @Override
    protected void mapResourcesFromJson() {
        JsonNode tArray = getTransmogNode().get(cResources);
        for (JsonNode jsonNode : tArray) {
            addResource(Resource.fromJson(jsonNode));
        }

        JsonNode iArray = getIlluminatedNode(cResources);
        for (JsonNode jsonNode : iArray) {
            IlluminateResource iResource = IlluminateResource.fromJson(jsonNode);
            Resource tResource = getResourceByName(iResource.getName());

            if (tResource == null) {
                addResource(UpdatifyResource.createFrom(iResource));
            } else if (!tResource.equals(iResource)) {
                updateResource(UpdatifyResource.updateToNew(tResource, iResource));
            }
        }
    }

    @Override
    protected void mapStationsFromJson() {
        JsonNode tArray = getTransmogNode().get(cStations);
        for (JsonNode jsonNode : tArray) {
            addStation(Station.fromJson(jsonNode));
        }

        JsonNode iArray = getIlluminatedNode(cStations);
        for (JsonNode jsonNode : iArray) {
            IlluminateStation iStation = IlluminateStation.fromJson(jsonNode);
            Station tStation = getStationByName(iStation.getName());

            if (tStation == null) {
                Log.d("Adding Station: " + iStation.toString());
                addStation(UpdatifyStation.createFrom(iStation));
            } else if (!tStation.equals(iStation)) {
                Log.d("Updating Station: " + iStation.toString());
                updateStation(UpdatifyStation.updateToNew(tStation, iStation));
            }
        }
    }

    @Override
    protected void mapFoldersFromJson() {
        JsonNode jsonArray = getTransmogNode().get(cFolders);
        for (JsonNode jsonNode : jsonArray) {
            addFolder(Folder.fromJson(jsonNode));
        }

        JsonNode iArray = getIlluminatedNode(cFolders);
        for (JsonNode jsonNode : iArray) {
            IlluminateFolder iFolder = IlluminateFolder.fromJson(jsonNode);
            Folder tFolder = getFolderByName(iFolder.getName());

            if (tFolder == null) {
                Log.d("Adding Folder: " + iFolder.toString());
                addFolder(UpdatifyFolder.createFrom(iFolder));
            } else if (!tFolder.equals(iFolder)) {
                Log.d("Updating Folder: " + iFolder.toString());
                updateFolder(UpdatifyFolder.updateToNew(tFolder, iFolder));
            }
        }
    }

    @Override
    protected void mapEngramsFromJson() {
        JsonNode tArray = getTransmogNode().get(cEngrams);
        for (JsonNode tNode : tArray) {
            addEngram(Engram.fromJson(tNode));
        }

        JsonNode iArray = getIlluminatedNode(cEngrams);
        for (JsonNode iNode : iArray) {
            IlluminateEngram iEngram = UpdatifyEngram.fromJson(iNode);
            Engram tEngram = getEngramByName(iEngram.getName());

            if (tEngram == null) {
                Log.d("Adding Engram: " + iEngram.toString());
                addEngram(UpdatifyEngram.createFrom(iEngram));
            } else if (!tEngram.equals(iEngram)) {
                Log.d("Updating Engram: " + iEngram.toString());
                updateEngram(UpdatifyEngram.updateToNew(tEngram, iEngram));
            }
        }
    }

    @Override
    protected void mapCompositionFromJson() {
        JsonNode tArray = getTransmogNode().get(cComposition);
        for (JsonNode jsonNode : tArray) {
            addComposition(Composition.fromJson(jsonNode));
        }
    }

    protected void mapCompositesFromJson() {
        JsonNode jsonArray = getTransmogNode().get(cComposites);
        for (JsonNode jsonNode : jsonArray) {
            addComposite(Composite.fromJson(jsonNode));
        }

        JsonNode iArray = getIlluminatedNode(cEngrams);
        for (JsonNode jsonNode : iArray) {
            String name = jsonNode.get(cName).asText();
            String compositionId = getCompositionUUIDByName(name);

            JsonNode compositionNode = jsonNode.get(cComposition);
            JsonNode compositesNode = compositionNode.get(cComposites);

            if (compositionId == null) {
                String engramId = getEngramUUIDByName(name);
                Composition iComposition = UpdatifyComposition.createFrom(IlluminateComposition.with(engramId));
                compositionId = iComposition.getUuid();

                for (JsonNode compositeNode : compositesNode) {
                    IlluminateComposite iComposite = IlluminateComposite.fromJson(compositeNode);
                    String imageFile = getImageFileByName(name);
                    Log.d("Adding Composite: " + iComposite.toString());
                    addComposite(UpdatifyComposite.createFrom(iComposite, imageFile, engramId, compositionId));
                }
            } else {
                for (JsonNode compositeNode : compositesNode) {
                    IlluminateComposite iComposite = IlluminateComposite.fromJson(compositeNode);
                    for (String compositeId : getCompositeUUIDListByName(iComposite.getName())) {
                        Composite tComposite = getComposite(compositeId);
                        if (compositionId.equals(tComposite.getCompositionId())) {
                            if (!tComposite.equals(iComposite)) {
                                Log.d("Updating Composite: " + iComposite.toString());
                                updateComposite(UpdatifyComposite.updateToNew(tComposite, iComposite));
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void mapDirectoryFromJson() {
        JsonNode tArray = getTransmogNode().get(cDirectory);
        for (JsonNode jsonNode : tArray) {
            addUncheckedDirectoryItem(DirectoryItem.fromJson(jsonNode));
        }

        JsonNode iArray = getIlluminatedNode(cDirectory);
        for (JsonNode iNode : iArray) {
            String name = iNode.get(cName).asText();
            DirectoryItem tDirectoryItem = getDirectoryItemByName(name, null);
            if (tDirectoryItem == null) {
                //  we have a new directory or a directory that was moved
                // TODO: 9/26/2020 check if moved, treat as new for now
                Station station = getStationByName(name);
                tDirectoryItem = UpdatifyDirectoryItem.createFromStation(station, null);
                addDirectoryItem(tDirectoryItem);
            } else {
                //  mapped directory
                //  remove from uncheck list
                removeUncheckedDirectoryItem(tDirectoryItem.getUuid());
            }

            //  map station's hierarchy
            mapIlluminatedDirectory(iNode, tDirectoryItem.getUuid());
        }

        //  check for unchecked directory items, remove from lists
        for (String uuid : uncheckedDirectoryItems) {
            Log.d("Removing unchecked DirectoryItem: " + uuid);
            // TODO: 9/26/2020 Remove unchecked DirectoryItems
        }
    }

    private void mapIlluminatedDirectory(JsonNode iNode, String parentId) {
        JsonNode folders = iNode.get(cFolders);
        for (JsonNode folderNode : folders) {
            String name = folderNode.get(cName).asText();
            DirectoryItem fDirectoryItem = getDirectoryItemByName(name, parentId);
            if (fDirectoryItem == null) {
                Folder folder = getFolderByName(name);
                fDirectoryItem = UpdatifyDirectoryItem.createFromFolder(folder, parentId);
                Log.d("Adding Folder DirectoryItem: " + fDirectoryItem.toString());
                addDirectoryItem(fDirectoryItem);
            } else {
                removeUncheckedDirectoryItem(fDirectoryItem.getUuid());
            }

            //  map folder's hierarchy
            mapIlluminatedDirectory(folderNode, fDirectoryItem.getUuid());
        }

        JsonNode engrams = iNode.get(cEngrams);
        for (JsonNode engramNode : engrams) {
            String name = engramNode.asText();
            DirectoryItem eDirectoryItem = getDirectoryItemByName(name, parentId);
            if (eDirectoryItem == null) {
                Engram engram = getEngramByName(name);
                eDirectoryItem = UpdatifyDirectoryItem.createFromEngram(engram, parentId);
                Log.d("Adding Engram DirectoryItem: " + eDirectoryItem.toString());
                addDirectoryItem(eDirectoryItem);
            } else {
                removeUncheckedDirectoryItem(eDirectoryItem.getUuid());
            }
        }
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

    private void updateResource(Resource resource) {
        addResourceToMap(resource.getUuid(), resource);
    }

    private void updateStation(Station station) {
        addStationToMap(station.getUuid(), station);
    }

    private void updateFolder(Folder folder) {
        addFolderToMap(folder.getUuid(), folder);
    }

    private void updateEngram(Engram engram) {
        addEngramToMap(engram.getUuid(), engram);
    }

    private void updateComposite(Composite composite) {
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
