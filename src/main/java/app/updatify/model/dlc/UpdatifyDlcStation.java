package app.updatify.model.dlc;

import app.illuminate.model.IlluminateStation;
import app.updatify.model.UpdatifyStation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Station;

import java.util.Date;
import java.util.UUID;

import static util.Constants.*;

public class UpdatifyDlcStation extends UpdatifyStation {
    private final String dlcId;

    @JsonCreator
    public UpdatifyDlcStation(@JsonProperty(cUuid) String uuid,
                              @JsonProperty(cName) String name,
                              @JsonProperty(cImageFile) String imageFile,
                              @JsonProperty(cEngramId) String sourceId,
                              @JsonProperty(cLastUpdated) Date lastUpdated,
                              @JsonProperty(cGameId) String gameId,
                              @JsonProperty(cDlcId) String dlcId) {
        super(uuid, name, imageFile, sourceId, lastUpdated, gameId);
        this.dlcId = dlcId;
    }

    public static UpdatifyDlcStation createFrom(IlluminateStation iStation,
                                                String gameId,
                                                String dlcId) {
        return new UpdatifyDlcStation(UUID.randomUUID().toString(),
                iStation.getName(),
                iStation.getImageFile(),
                iStation.getSourceId(),
                new Date(),
                gameId,
                dlcId);
    }

    public static UpdatifyDlcStation updateToNew(Station tStation,
                                                 IlluminateStation iStation,
                                                 String gameId,
                                                 String dlcId) {
        return new UpdatifyDlcStation(tStation.getUuid(),
                iStation.getName(),
                iStation.getImageFile(),
                iStation.getSourceId(),
                new Date(),
                gameId,
                dlcId);
    }

    public static UpdatifyDlcStation with(Station station,
                                          String gameId,
                                          String dlcId) {
        return new UpdatifyDlcStation(station.getUuid(),
                station.getName(),
                station.getImageFile(),
                station.getSourceId(),
                station.getLastUpdated(),
                gameId,
                dlcId);
    }

    public String getDlcId() {
        return dlcId;
    }
}
