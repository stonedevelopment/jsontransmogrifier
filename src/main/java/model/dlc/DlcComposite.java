package model.dlc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Composite;

import static util.Constants.*;

public class DlcComposite extends Composite {
    private final String dlcId;

    @JsonCreator
    public DlcComposite(@JsonProperty(cUuid) String uuid,
                        @JsonProperty(cName) String name,
                        @JsonProperty(cImageFile) String imageFile,
                        @JsonProperty(cQuantity) int quantity,
                        @JsonProperty(cSourceId) String sourceId,
                        @JsonProperty(cIsEngram) boolean isEngram,
                        @JsonProperty(cCompositionId) String compositionId,
                        @JsonProperty(cGameId) String gameId,
                        @JsonProperty(cDlcId) String dlcId) {
        super(uuid, name, imageFile, quantity, sourceId, isEngram, compositionId, gameId);
        this.dlcId = dlcId;
    }

    public static DlcComposite fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, DlcComposite.class);
    }

    public String getDlcId() {
        return dlcId;
    }
}