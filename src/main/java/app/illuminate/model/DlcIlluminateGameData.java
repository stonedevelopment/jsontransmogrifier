package app.illuminate.model;

import app.illuminate.controller.IlluminateGameData;
import com.fasterxml.jackson.databind.JsonNode;
import model.Engram;
import model.Station;
import model.details.DlcDetails;

import static util.Constants.cDetails;

public class DlcIlluminateGameData extends IlluminateGameData {
    public DlcIlluminateGameData(JsonNode inNode) {
        super(inNode);

        mapGameDataFromJson();
    }

    @Override
    public DlcDetails getDetailsObject() {
        return (DlcDetails) super.getDetailsObject();
    }

    @Override
    protected void createDetailsObject() {
        this.details = DlcDetails.from(inNode.get(cDetails));
    }

    @Override
    public JsonNode resolveToJson() {
        return null;
    }
}
