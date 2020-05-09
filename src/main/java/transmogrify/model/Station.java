package transmogrify.model;

public class Station {
    private final String uuid;
    private final String name;
    private final String engramId;

    public Station(String uuid, String name, String engramId) {
        this.uuid = uuid;
        this.name = name;
        this.engramId = engramId;
    }

    public String getUuid() {
        return uuid;
//        return name;
    }

    public String getName() {
        return name;
    }

    public String getEngramId() {
        return engramId;
    }
}
