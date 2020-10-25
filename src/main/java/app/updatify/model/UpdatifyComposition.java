package app.updatify.model;

import app.illuminate.model.IlluminateComposition;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Composition;

import java.util.Date;
import java.util.UUID;

import static util.Constants.*;

public class UpdatifyComposition extends Composition {
    private final String gameId;

    @JsonCreator
    public UpdatifyComposition(@JsonProperty(cUuid) String uuid,
                               @JsonProperty(cEngramId) String engramId,
                               @JsonProperty(cLastUpdated) Date lastUpdated,
                               @JsonProperty(cGameId) String gameId) {
        super(uuid, engramId, lastUpdated);
        this.gameId = gameId;
    }

    public static UpdatifyComposition createFrom(IlluminateComposition iComposition,
                                                 String gameId) {
        return new UpdatifyComposition(UUID.randomUUID().toString(),
                iComposition.getEngramId(),
                iComposition.getLastUpdated(),
                gameId);
    }

    public static UpdatifyComposition with(Composition composition,
                                           String gameId) {
        return new UpdatifyComposition(composition.getUuid(),
                composition.getEngramId(),
                composition.getLastUpdated(),
                gameId);
    }

    public String getDlcId() {
        return gameId;
    }
}