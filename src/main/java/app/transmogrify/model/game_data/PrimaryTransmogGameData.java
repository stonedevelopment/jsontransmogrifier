package app.transmogrify.model.game_data;

import app.transmogrify.controller.TransmogGameData;
import app.transmogrify.model.details.TransmogDetails;
import app.transmogrify.model.json.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.*;
import util.Log;

import java.util.Date;

import static util.Constants.*;

public class PrimaryTransmogGameData extends TransmogGameData {
    private PrimaryTransmogGameData(JsonNode inNode, JsonDlc jsonDlc) {
        super(inNode, jsonDlc);
        mapGameDataFromJson();
    }

    public static PrimaryTransmogGameData with(JsonNode inNode, JsonDlc jsonDlc) {
        return new PrimaryTransmogGameData(inNode, jsonDlc);
    }

    @Override
    protected void createDetailsObject() {
        this.details = TransmogDetails.with(jsonDlc);
    }

    public String getUuid() {
        return getDetailsObject().getUuid();
    }

    @Override
    public boolean isValidDlcIdForDirectory(long dlcId) {
        return getDlcId() == dlcId;
    }

    @Override
    public JsonNode resolveToJson() {
        //  create game data node
        ObjectNode gameDataNode = mapper.createObjectNode();

        gameDataNode.set(cDetails, mapper.valueToTree(getDetailsObject()));

        //  add resources, without complex resources
        gameDataNode.set(cResources, mapper.valueToTree(transformResourceMap()));

        //  add stations
        gameDataNode.set(cStations, mapper.valueToTree(transformStationMap()));

        //  add folders
        gameDataNode.set(cFolders, mapper.valueToTree(transformFolderMap()));

        //  add engrams
        gameDataNode.set(cEngrams, mapper.valueToTree(transformEngramMap()));

        //  add composition
        gameDataNode.set(cComposition, mapper.valueToTree(transformCompositionMap()));

        //  add composites
        gameDataNode.set(cComposites, flattenCompositeMapToJson(transformCompositeMap()));

        //  add directory, traverse through tree, fill with uuids
        gameDataNode.set(cDirectory, flattenDirectoryToJson(transformDirectory()));

        return gameDataNode;
    }
}