package app.updatify.model.dlc;

import app.illuminate.model.IlluminateEngram;
import app.updatify.model.UpdatifyEngram;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Engram;

import java.util.Date;
import java.util.UUID;

import static util.Constants.*;

public class UpdatifyDlcEngram extends UpdatifyEngram {
    private final String dlcId;

    @JsonCreator
    public UpdatifyDlcEngram(@JsonProperty(cUuid) String uuid,
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

    public static UpdatifyDlcEngram createFrom(IlluminateEngram iEngram,
                                               String gameId,
                                               String dlcId) {
        return new UpdatifyDlcEngram(UUID.randomUUID().toString(),
                iEngram.getName(),
                iEngram.getDescription(),
                iEngram.getImageFile(),
                iEngram.getLevel(),
                iEngram.getYield(),
                iEngram.getPoints(),
                iEngram.getXp(),
                iEngram.getCraftingTime(),
                new Date(),
                gameId,
                dlcId);
    }

    public static UpdatifyDlcEngram updateToNew(Engram tEngram,
                                                IlluminateEngram iEngram,
                                                String gameId,
                                                String dlcId) {
        return new UpdatifyDlcEngram(tEngram.getUuid(),
                iEngram.getName(),
                iEngram.getDescription(),
                iEngram.getImageFile(),
                iEngram.getLevel(),
                iEngram.getYield(),
                iEngram.getPoints(),
                iEngram.getXp(),
                iEngram.getCraftingTime(),
                new Date(),
                gameId,
                dlcId);
    }

    public static UpdatifyDlcEngram with(Engram engram,
                                         String gameId,
                                         String dlcId) {
        return new UpdatifyDlcEngram(engram.getUuid(),
                engram.getName(),
                engram.getDescription(),
                engram.getImageFile(),
                engram.getLevel(),
                engram.getYield(),
                engram.getPoints(),
                engram.getXp(),
                engram.getCraftingTime(),
                engram.getLastUpdated(),
                gameId,
                dlcId);
    }

    public String getDlcId() {
        return dlcId;
    }
}