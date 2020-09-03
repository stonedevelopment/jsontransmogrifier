package app.illuminate.model.controller;

import app.illuminate.controller.IlluminateGameData;
import app.illuminate.model.IlluminateEngram;
import app.illuminate.model.IlluminateFolder;
import app.illuminate.model.details.IlluminateDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.Composition;
import model.DirectoryItem;
import model.Engram;
import model.Folder;

import static util.Constants.*;

public class PrimaryIlluminateGameData extends IlluminateGameData {
    private PrimaryIlluminateGameData(JsonNode inNode) {
        super(inNode);

        mapGameDataFromJson();
    }

    public static PrimaryIlluminateGameData with(JsonNode inNode) {
        return new PrimaryIlluminateGameData(inNode);
    }

    @Override
    public Composition getComposition(String engramId) {
        String uuid = getCompositionUUIDByEngramId(engramId);
        return super.getComposition(uuid);
    }

    @Override
    public IlluminateDetails getDetailsObject() {
        return super.getDetailsObject();
    }

    @Override
    protected void createDetailsObject() {
        this.details = IlluminateDetails.from(inNode.get(cDetails));
    }
}