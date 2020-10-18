package app.updatify.model;

import app.illuminate.model.IlluminateStation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Station;

import java.util.Date;
import java.util.UUID;

import static util.Constants.*;

public class UpdatifyStation extends Station {
    private final String gameId;

    @JsonCreator
    public UpdatifyStation(@JsonProperty(cUuid) String uuid,
                           @JsonProperty(cName) String name,
                           @JsonProperty(cImageFile) String imageFile,
                           @JsonProperty(cEngramId) String sourceId,
                           @JsonProperty(cLastUpdated) Date lastUpdated,
                           @JsonProperty(cGameId) String gameId) {
        super(uuid, name, imageFile, sourceId, lastUpdated);
        this.gameId = gameId;
    }

    public static UpdatifyStation createFrom(IlluminateStation iStation, String gameId) {
        return new UpdatifyStation(UUID.randomUUID().toString(),
                iStation.getName(),
                iStation.getImageFile(),
                iStation.getSourceId(),
                new Date(),
                gameId);
    }

    public static UpdatifyStation updateToNew(Station tStation, IlluminateStation iStation, String gameId) {
        return new UpdatifyStation(tStation.getUuid(),
                iStation.getName(),
                iStation.getImageFile(),
                iStation.getSourceId(),
                new Date(),
                gameId);
    }

    public static UpdatifyStation with(Station station, String gameId) {
        return new UpdatifyStation(station.getUuid(),
                station.getName(),
                station.getImageFile(),
                station.getSourceId(),
                station.getLastUpdated(),
                gameId);
    }

    public String getGameId() {
        return gameId;
    }
}
