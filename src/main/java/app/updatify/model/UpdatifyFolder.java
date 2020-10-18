package app.updatify.model;

import app.illuminate.model.IlluminateFolder;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Folder;

import java.util.UUID;

import static util.Constants.*;

public class UpdatifyFolder extends Folder {
    private final String gameId;

    @JsonCreator
    public UpdatifyFolder(@JsonProperty(cUuid) String uuid,
                          @JsonProperty(cName) String name,
                          @JsonProperty(cGameId) String gameId) {
        super(uuid, name);
        this.gameId = gameId;
    }

    public static UpdatifyFolder createFrom(IlluminateFolder iFolder, String gameId) {
        return new UpdatifyFolder(UUID.randomUUID().toString(), iFolder.getName(), gameId);
    }

    public static UpdatifyFolder updateToNew(Folder tFolder, IlluminateFolder iFolder, String gameId) {
        return new UpdatifyFolder(tFolder.getUuid(), iFolder.getName(), gameId);
    }

    public static UpdatifyFolder with(Folder folder, String gameId) {
        return new UpdatifyFolder(folder.getUuid(), folder.getName(), gameId);
    }

    public String getGameId() {
        return gameId;
    }
}