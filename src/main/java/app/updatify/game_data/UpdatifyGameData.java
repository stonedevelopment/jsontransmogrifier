package app.updatify.game_data;

import app.illuminate.model.*;
import app.illuminate.model.details.IlluminateDetails;
import app.transmogrify.model.details.TransmogDetails;
import app.updatify.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controller.GameData;
import model.*;
import util.JSONUtil;

import java.util.HashMap;
import java.util.Map;

import static util.Constants.*;

public class UpdatifyGameData extends GameData {
    private final JsonNode illuminationNode;
    private final Map<String, JsonNode> illuminationMap = new HashMap<>();
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

    private JsonNode getTransmogNode() {
        return getInNode();
    }

    private JsonNode getIlluminatedNode(String type) {
        return illuminationMap.get(type);
    }

    /**
     * Helper method for top-level details to determine if whether or not information was updated
     *
     * @return if information has been updated
     */
    private boolean hasUpdated() {
        return hasUpdate;
    }

    private void setHasUpdate() {
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

    private void mapIlluminationNode() {
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
        TransmogDetails tDetails = TransmogDetails.fromJson(getTransmogNode().get(cDetails));
        setDetails(tDetails);

        //  compare
        IlluminateDetails iDetails = IlluminateDetails.from(getIlluminatedNode(cDetails));
        if (!tDetails.equals(iDetails)) {
            updateDetails(UpdatifyDetails.convertToNew(tDetails, iDetails));
            setHasUpdate();
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
                addStation(UpdatifyStation.createFrom(iStation));
            } else if (!tStation.equals(iStation)) {
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
                addFolder(UpdatifyFolder.createFrom(iFolder));
            } else if (!tFolder.equals(iFolder)) {
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
                addEngram(UpdatifyEngram.createFrom(iEngram));
            } else if (!tEngram.equals(iEngram)) {
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
                    addComposite(UpdatifyComposite.createFrom(iComposite, imageFile, engramId, compositionId));
                }
            } else {
                for (JsonNode compositeNode : compositesNode) {
                    IlluminateComposite iComposite = IlluminateComposite.fromJson(compositeNode);
                    for (String compositeId : getCompositeUUIDListByName(iComposite.getName())) {
                        Composite tComposite = getComposite(compositeId);
                        if (compositeId.equals(tComposite.getCompositionId())) {
                            if (!tComposite.equals(iComposite)) {
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
        JsonNode jsonArray = getTransmogNode().get(cDirectory);
        for (JsonNode jsonNode : jsonArray) {
            addDirectoryItem(DirectoryItem.fromJson(jsonNode));
        }
    }

    private void addIlluminatedNode(String type, JsonNode illuminatedNode) {
        illuminationMap.put(type, illuminatedNode);
    }

    protected void addComposition(Composition composition) {
        String name = getEngramNameByUUID(composition.getEngramId());
        addComposition(name, composition);
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
    public JsonNode resolveToJson() {
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
//        outNode.set(cDirectory, flattenDirectoryToJson(transformDirectory()));

        return outNode;
    }
}
