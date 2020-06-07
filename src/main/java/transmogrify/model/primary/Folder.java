package transmogrify.model.primary;

public class Folder {
    private final String uuid;
    private final String name;
    private final String gameId;

    public Folder(String uuid, String name, String gameId) {
        this.uuid = uuid;
        this.name = name;
        this.gameId = gameId;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getGameId() {
        return gameId;
    }

    public boolean equals(String name) {
        return getName().equals(name);
    }
}
