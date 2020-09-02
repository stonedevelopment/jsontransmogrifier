package app.illuminate.model.dlc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.dlc.DlcEngram;

import java.util.Date;

import static util.Constants.*;

public class DlcIlluminateEngram extends DlcEngram {

    @JsonCreator
    public DlcIlluminateEngram(@JsonProperty(cUuid) String uuid,
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
        super(uuid, name, description, imageFile, level, yield, points, xp, craftingTime, lastUpdated, gameId, dlcId);
    }

    @JsonIgnore
    @Override
    public String getGameId() {
        return super.getGameId();
    }

    @JsonIgnore
    @Override
    public String getDlcId() {
        return super.getDlcId();
    }
}