package app.illuminate.model.controller;

import app.illuminate.controller.IlluminateGameData;
import app.illuminate.model.details.IlluminateDetails;
import com.fasterxml.jackson.databind.JsonNode;

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
    public IlluminateDetails getDetails() {
        return super.getDetails();
    }

    @Override
    protected void createDetailsObject() {
        this.details = IlluminateDetails.from(getInNode().get(cDetails));
    }
}