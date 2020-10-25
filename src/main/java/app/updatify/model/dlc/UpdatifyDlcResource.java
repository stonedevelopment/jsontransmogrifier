package app.updatify.model.dlc;

import app.illuminate.model.IlluminateResource;
import app.updatify.model.UpdatifyResource;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Resource;

import java.util.Date;
import java.util.UUID;

import static util.Constants.*;

public class UpdatifyDlcResource extends UpdatifyResource {
    private final String dlcId;

    @JsonCreator
    public UpdatifyDlcResource(@JsonProperty(cUuid) String uuid,
                               @JsonProperty(cName) String name,
                               @JsonProperty(cDescription) String description,
                               @JsonProperty(cImageFile) String imageFile,
                               @JsonProperty(cLastUpdated) Date lastUpdated,
                               @JsonProperty(cGameId) String gameId,
                               @JsonProperty(cDlcId) String dlcId) {
        super(uuid, name, description, imageFile, lastUpdated, gameId);
        this.dlcId = dlcId;
    }

    public static UpdatifyDlcResource createFrom(IlluminateResource iResource,
                                                 String gameId,
                                                 String dlcId) {
        return new UpdatifyDlcResource(UUID.randomUUID().toString(),
                iResource.getName(),
                iResource.getDescription(),
                iResource.getImageFile(),
                new Date(),
                gameId,
                dlcId);
    }

    public static UpdatifyDlcResource updateToNew(Resource tResource,
                                                  IlluminateResource iResource,
                                                  String gameId,
                                                  String dlcId) {
        return new UpdatifyDlcResource(tResource.getUuid(),
                iResource.getName(),
                iResource.getDescription(),
                iResource.getImageFile(),
                new Date(),
                gameId,
                dlcId);
    }

    public static UpdatifyDlcResource with(Resource resource,
                                           String gameId,
                                           String dlcId) {
        return new UpdatifyDlcResource(resource.getUuid(),
                resource.getName(),
                resource.getDescription(),
                resource.getImageFile(),
                resource.getLastUpdated(),
                gameId,
                dlcId);
    }

    public String getDlcId() {
        return dlcId;
    }
}
