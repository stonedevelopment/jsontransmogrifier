package app.updatify.model;

import app.illuminate.model.IlluminateResource;
import model.Resource;

import java.util.Date;
import java.util.UUID;

public class UpdatifyResource extends Resource {

    public UpdatifyResource(String uuid, String name, String description, String imageFile, Date lastUpdated) {
        super(uuid, name, description, imageFile, lastUpdated);
    }

    public static Resource createFrom(IlluminateResource iResource) {
        return new Resource(UUID.randomUUID().toString(),
                iResource.getName(),
                iResource.getDescription(),
                iResource.getImageFile(),
                new Date());
    }

    public static Resource updateToNew(Resource tResource, IlluminateResource iResource) {
        return new Resource(tResource.getUuid(),
                iResource.getName(),
                iResource.getDescription(),
                iResource.getImageFile(),
                new Date());
    }
}
