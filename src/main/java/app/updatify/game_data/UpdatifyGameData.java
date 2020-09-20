package app.updatify.game_data;

import app.illuminate.model.IlluminateResource;
import app.illuminate.model.details.IlluminateDetails;
import app.transmogrify.model.details.TransmogDetails;
import app.updatify.model.UpdatifyResource;
import com.fasterxml.jackson.databind.JsonNode;
import controller.GameData;
import model.*;
import model.details.Details;
import util.JSONUtil;
import util.Log;

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
    public UpdatifyResource getResourceByName(String name) {
        return (UpdatifyResource) super.getResourceByName(name);
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
        Details tDetails = TransmogDetails.fromJson(getTransmogNode().get(cDetails));
        setDetails(tDetails);

        //  compare
        IlluminateDetails iDetails = IlluminateDetails.from(getIlluminatedNode(cDetails));
        if (!tDetails.equals(iDetails)) {
            updateDetails(tDetails.updateToNew(iDetails));
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
            UpdatifyResource tResource = getResourceByName(iResource.getName());

            if (tResource == null) {
                Log.d("ADDING: " + iResource.toString());
                addResource(UpdatifyResource.createFrom(iResource));
            } else if (!tResource.equals(iResource)) {
                Log.d("UPDATING: " + tResource.toString() + " to " + iResource.toString());
                updateResource(tResource.updateFrom(iResource));
            }
        }
    }

    @Override
    protected void mapStationsFromJson() {
        JsonNode jsonArray = getTransmogNode().get(cStations);
        for (JsonNode jsonNode : jsonArray) {
            addStation(Station.fromJson(jsonNode));
        }
    }

    @Override
    protected void mapFoldersFromJson() {
        JsonNode jsonArray = getTransmogNode().get(cFolders);
        for (JsonNode jsonNode : jsonArray) {
            addFolder(Folder.fromJson(jsonNode));
        }
    }

    @Override
    protected void mapEngramsFromJson() {
        JsonNode jsonArray = getTransmogNode().get(cEngrams);
        for (JsonNode jsonNode : jsonArray) {
            addEngram(Engram.fromJson(jsonNode));
        }
    }

    @Override
    protected void mapCompositionFromJson() {
        JsonNode jsonArray = getTransmogNode().get(cComposition);
        for (JsonNode jsonNode : jsonArray) {
            addComposition(Composition.fromJson(jsonNode));
        }
    }

    protected void mapCompositesFromJson() {
        JsonNode jsonArray = getTransmogNode().get(cComposites);
        for (JsonNode jsonNode : jsonArray) {
            addComposite(Composite.fromJson(jsonNode));
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

    @Override
    protected void addComposition(Composition composition) {
        String name = getEngramNameByUUID(composition.getEngramId());
        addComposition(name, composition);
    }

    private void updateResource(Resource resource) {
        addResourceToMap(resource.getUuid(), resource);
    }

    @Override
    public JsonNode resolveToJson() {
        return null;
    }
}
