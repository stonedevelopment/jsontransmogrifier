package app.updatify.model;

import app.illuminate.model.IlluminateResource;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Resource;

import java.util.Date;
import java.util.UUID;

import static util.Constants.*;

public class UpdatifyResource extends Resource {

    @JsonCreator
    public UpdatifyResource(@JsonProperty(cUuid) String uuid,
                            @JsonProperty(cName) String name,
                            @JsonProperty(cDescription) String description,
                            @JsonProperty(cImageFile) String imageFile,
                            @JsonProperty(cLastUpdated) Date lastUpdated) {
        super(uuid, name, description, imageFile, lastUpdated);
    }

    public static Resource createFrom(IlluminateResource resource) {
        return new Resource(UUID.randomUUID().toString(),
                resource.getName(),
                resource.getDescription(),
                resource.getImageFile(),
                new Date());
    }

    public Resource updateFrom(Resource resource) {
        return new Resource(getUuid(),
                resource.getName(),
                resource.getDescription(),
                resource.getImageFile(),
                new Date());
    }
}
