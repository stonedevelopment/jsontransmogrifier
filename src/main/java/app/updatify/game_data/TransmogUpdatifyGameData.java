package app.updatify.game_data;

import app.transmogrify.model.details.TransmogDetails;
import com.fasterxml.jackson.databind.JsonNode;

import static util.Constants.cDetails;

public class TransmogUpdatifyGameData extends UpdatifyGameData {
    public TransmogUpdatifyGameData(JsonNode inNode) {
        super(inNode);
    }

    @Override
    protected void createDetailsObject() {
        details = TransmogDetails.from(inNode.get(cDetails));
    }

    @Override
    public TransmogDetails getDetailsObject() {
        return (TransmogDetails) super.getDetailsObject();
    }

}
