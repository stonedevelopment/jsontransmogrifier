package app.illuminate.model;

import app.illuminate.controller.IlluminateGameData;
import app.illuminate.model.details.IlluminateDetails;
import com.fasterxml.jackson.databind.JsonNode;

import static util.Constants.cDetails;

public class PrimaryIlluminateGameData extends IlluminateGameData {
    private PrimaryIlluminateGameData(JsonNode inNode) {
        super(inNode);

        mapGameDataFromJson();
    }

    public static PrimaryIlluminateGameData with(JsonNode inNode) {
        return new PrimaryIlluminateGameData(inNode);
    }

    @Override
    public IlluminateDetails getDetailsObject() {
        return (IlluminateDetails) super.getDetailsObject();
    }

    @Override
    protected void createDetailsObject() {
        this.details = IlluminateDetails.from(inNode.get(cDetails));
    }
}