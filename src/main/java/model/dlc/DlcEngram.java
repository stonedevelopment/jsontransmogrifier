package model.dlc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Engram;

import java.util.Date;

import static util.Constants.*;

public class DlcEngram extends Engram {
    private final String dlcId;

    @JsonCreator
    public DlcEngram(@JsonProperty(cUuid) String uuid,
                     @JsonProperty(cName) String name,
                     @JsonProperty(cDescription) String description,
                     @JsonProperty(cImageFile) String imageFile,
                     @JsonProperty(cLevel) int level,
                     @JsonProperty(cYield) int yield,
                     @JsonProperty(cPoints) int points,
                     @JsonProperty(cXp) int xp,
                     @JsonProperty(cCraftingTime) int craftingTime,
                     @JsonProperty(cLastUpdated) Date lastUpdated,
                     @JsonProperty(cGameId) String gameId,
                     @JsonProperty(cDlcId) String dlcId) {
        super(uuid, name, description, imageFile, level, yield, points, xp, craftingTime, lastUpdated, gameId);
        this.dlcId = dlcId;
    }

    public static DlcEngram fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, DlcEngram.class);
    }

    public String getDlcId() {
        return dlcId;
    }
}