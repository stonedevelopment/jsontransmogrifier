package model.dlc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Composition;

import java.util.Date;

import static util.Constants.*;

public class DlcComposition extends Composition {
    private final String dlcId;

    @JsonCreator
    public DlcComposition(@JsonProperty(cUuid) String uuid,
                          @JsonProperty(cEngramId) String engramId,
                          @JsonProperty(cLastUpdated) Date lastUpdated,
                          @JsonProperty(cGameId) String gameId,
                          @JsonProperty(cDlcId) String dlcId) {
        super(uuid, engramId, lastUpdated, gameId);
        this.dlcId = dlcId;
    }

    public static DlcComposition fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, DlcComposition.class);
    }

    public String getDlcId() {
        return dlcId;
    }
}