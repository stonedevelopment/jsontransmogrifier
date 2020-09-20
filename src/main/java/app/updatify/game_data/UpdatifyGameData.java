package app.updatify.game_data;

import com.fasterxml.jackson.databind.JsonNode;
import controller.GameData;
import model.*;
import model.details.Details;
import util.JSONUtil;

import java.util.Map;

import static util.Constants.*;

public class UpdatifyGameData extends GameData {
    private final JsonNode illuminationNode;
    private Map<String, JsonNode> illuminationMap;

    public UpdatifyGameData(JsonNode transmogNode, JsonNode illuminationNode) {
        super(transmogNode);
        this.illuminationNode = illuminationNode;
    }

    private JsonNode getTransmogNode() {
        return getInNode();
    }

    @Override
    protected void createDetailsObject() {
        this.details = Details.fromJson(getTransmogNode().get(cDetails));
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
            JsonNode illuminatedNode = JSONUtil.parseIn(cArkAssetsFilePath, illuminatedFile.asText());
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
    protected void mapResourcesFromJson() {
        JsonNode jsonArray = getTransmogNode().get(cResources);
        for (JsonNode jsonNode : jsonArray) {
            addResource(Resource.fromJson(jsonNode));
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
    protected void mapStationsFromJson() {
        JsonNode jsonArray = getTransmogNode().get(cStations);
        for (JsonNode jsonNode : jsonArray) {
            addStation(Station.fromJson(jsonNode));
        }
    }

    @Override
    protected void addComposition(Composition composition) {
        String name = getEngramNameByUUID(composition.getEngramId());
        addComposition(name, composition);
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

    @Override
    public JsonNode resolveToJson() {
        return null;
    }
}
