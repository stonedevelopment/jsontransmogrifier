package transmogrify.model;

public class Folder {
    private final String uuid;
    private final String name;

    public Folder(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public String getUuid() {
        return uuid;
//        return name;
    }

    public String getName() {
        return name;
    }
}
