package app.illuminate.model;

import app.illuminate.controller.IlluminateGameData;
import app.illuminate.model.details.IlluminateDetails;
import com.fasterxml.jackson.databind.JsonNode;

import static util.Constants.cDetails;

public class DlcIlluminateGameData extends IlluminateGameData {
    private DlcIlluminateGameData(JsonNode inNode) {
        super(inNode);

        mapGameDataFromJson();
    }

    public static DlcIlluminateGameData fromJson(JsonNode inNode) {
        return new DlcIlluminateGameData(inNode);
    }

    @Override
    public IlluminateDetails getDetailsObject() {
        return (IlluminateDetails) super.getDetailsObject();
    }

    @Override
    protected void createDetailsObject() {
        this.details = IlluminateDetails.from(inNode.get(cDetails));
    }

    @Override
    public JsonNode resolveToJson() {
        return null;
    }
}
