package transmogrify.model;

/**
 * "name": "Absorbent Substrate",
 * "description": "",
 * "image_file": "absorbent_substrate.webp"
 */
public class Resource {
    private final String uuid;
    private final String name;
    private final String description;
    private final String imageFile;

    public Resource(String uuid, String name, String description, String imageFile) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.imageFile = imageFile;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageFile() {
        return imageFile;
    }
}
