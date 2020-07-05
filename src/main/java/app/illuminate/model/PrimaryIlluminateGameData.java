package app.illuminate.model;

import app.illuminate.controller.IlluminateGameData;
import com.fasterxml.jackson.databind.JsonNode;
import model.*;
import model.details.Details;

import static util.Constants.*;

public class PrimaryIlluminateGameData extends IlluminateGameData {
    public PrimaryIlluminateGameData(JsonNode inNode) {
        super(inNode);

        mapGameDataFromJson();
    }

    @Override
    protected void createDetailsObject() {
        this.details = Details.from(inNode.get(cDetails));
    }

    @Override
    protected void mapStationDirectoryItem(Station station) {

    }

    @Override
    protected void mapEngramDirectoryItem(Engram engram, String parentId) {

    }

    @Override
    public JsonNode resolveToJson() {
        //  resolve maps to separate json files for easy editing
        return null;
    }
}
