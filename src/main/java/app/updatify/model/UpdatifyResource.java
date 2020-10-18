package app.updatify.model;

import app.illuminate.model.IlluminateResource;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Resource;

import java.util.Date;
import java.util.UUID;

import static util.Constants.*;

public class UpdatifyResource extends Resource {
    private final String gameId;

    @JsonCreator
    public UpdatifyResource(@JsonProperty(cUuid) String uuid,
                            @JsonProperty(cName) String name,
                            @JsonProperty(cDescription) String description,
                            @JsonProperty(cImageFile) String imageFile,
                            @JsonProperty(cLastUpdated) Date lastUpdated,
                            @JsonProperty(cGameId) String gameId) {
        super(uuid, name, description, imageFile, lastUpdated);
        this.gameId = gameId;
    }

    public static UpdatifyResource createFrom(IlluminateResource iResource, String gameId) {
        return new UpdatifyResource(UUID.randomUUID().toString(),
                iResource.getName(),
                iResource.getDescription(),
                iResource.getImageFile(),
                new Date(),
                gameId);
    }

    public static UpdatifyResource updateToNew(Resource tResource, IlluminateResource iResource, String gameId) {
        return new UpdatifyResource(tResource.getUuid(),
                iResource.getName(),
                iResource.getDescription(),
                iResource.getImageFile(),
                new Date(),
                gameId);
    }

    public static UpdatifyResource with(Resource resource, String gameId) {
        return new UpdatifyResource(resource.getUuid(),
                resource.getName(),
                resource.getDescription(),
                resource.getImageFile(),
                resource.getLastUpdated(),
                gameId);
    }

    public String getGameId() {
        return gameId;
    }
}
