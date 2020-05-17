package transmogrify.model;

import java.util.Date;

public class Station {
    private final String uuid;
    private final String name;
    private final String imageFile;
    private final String engramId;
    private final Date lastUpdated;

    public Station(String uuid, String name, String imageFile, String engramId, Date lastUpdated) {
        this.uuid = uuid;
        this.name = name;
        this.imageFile = imageFile;
        this.engramId = engramId;
        this.lastUpdated = lastUpdated;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getImageFile() {
        return imageFile;
    }

    public String getEngramId() {
        return engramId;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }
}
