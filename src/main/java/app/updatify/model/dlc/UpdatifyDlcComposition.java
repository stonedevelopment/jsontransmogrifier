package app.updatify.model.dlc;

import app.illuminate.model.IlluminateComposition;
import app.updatify.model.UpdatifyComposition;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Composition;

import java.util.Date;
import java.util.UUID;

import static util.Constants.*;

public class UpdatifyDlcComposition extends UpdatifyComposition {
    private final String dlcId;

    @JsonCreator
    public UpdatifyDlcComposition(@JsonProperty(cUuid) String uuid,
                                  @JsonProperty(cEngramId) String engramId,
                                  @JsonProperty(cLastUpdated) Date lastUpdated,
                                  @JsonProperty(cGameId) String gameId,
                                  @JsonProperty(cDlcId) String dlcId) {
        super(uuid, engramId, lastUpdated, gameId);
        this.dlcId = dlcId;
    }

    public static UpdatifyDlcComposition createFrom(IlluminateComposition iComposition,
                                                    String gameId,
                                                    String dlcId) {
        return new UpdatifyDlcComposition(UUID.randomUUID().toString(),
                iComposition.getEngramId(),
                iComposition.getLastUpdated(),
                gameId,
                dlcId);
    }

    public static UpdatifyDlcComposition with(Composition composition,
                                              String gameId,
                                              String dlcId) {
        return new UpdatifyDlcComposition(composition.getUuid(),
                composition.getEngramId(),
                composition.getLastUpdated(),
                gameId,
                dlcId);
    }

    public String getDlcId() {
        return dlcId;
    }
}