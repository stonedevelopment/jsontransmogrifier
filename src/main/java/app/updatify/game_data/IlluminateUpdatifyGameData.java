package app.updatify.game_data;

import app.illuminate.model.details.IlluminateDetails;
import com.fasterxml.jackson.databind.JsonNode;

import static util.Constants.cDetails;

public class IlluminateUpdatifyGameData extends UpdatifyGameData {
    public IlluminateUpdatifyGameData(JsonNode inNode) {
        super(inNode);
    }

    @Override
    protected void createDetailsObject() {
        details = IlluminateDetails.from(inNode.get(cDetails));
    }

    @Override
    public IlluminateDetails getDetailsObject() {
        return (IlluminateDetails) super.getDetailsObject();
    }
}
