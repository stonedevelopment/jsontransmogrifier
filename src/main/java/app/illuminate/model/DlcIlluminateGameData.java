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
    protected void mapStationDirectoryItem(Station station) {

    }

    @Override
    protected void mapEngramDirectoryItem(Engram engram, String parentId) {

    }

    @Override
    public JsonNode resolveToJson() {
        return null;
    }
}
