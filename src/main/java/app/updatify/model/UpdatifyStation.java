package app.updatify.model;

import app.illuminate.model.IlluminateStation;
import model.Station;

import java.util.Date;
import java.util.UUID;

public class UpdatifyStation extends Station {

    public UpdatifyStation(String uuid, String name, String imageFile, String sourceId, Date lastUpdated) {
        super(uuid, name, imageFile, sourceId, lastUpdated);
    }

    public static Station createFrom(IlluminateStation iStation) {
        return new Station(UUID.randomUUID().toString(),
                iStation.getName(),
                iStation.getImageFile(),
                iStation.getSourceId(),
                new Date());
    }

    public static Station updateToNew(Station tStation, IlluminateStation iStation) {
        return new Station(tStation.getUuid(),
                iStation.getName(),
                iStation.getImageFile(),
                iStation.getSourceId(),
                new Date());
    }
}
