package app.updatify.model.dlc;

import app.illuminate.model.IlluminateFolder;
import app.updatify.model.UpdatifyFolder;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Folder;

import java.util.UUID;

import static util.Constants.*;

public class UpdatifyDlcFolder extends UpdatifyFolder {
    private final String dlcId;

    @JsonCreator
    public UpdatifyDlcFolder(@JsonProperty(cUuid) String uuid,
                             @JsonProperty(cName) String name,
                             @JsonProperty(cGameId) String gameId,
                             @JsonProperty(cDlcId) String dlcId) {
        super(uuid, name, gameId);
        this.dlcId = dlcId;
    }

    public static UpdatifyDlcFolder createFrom(IlluminateFolder iFolder,
                                               String gameId,
                                               String dlcId) {
        return new UpdatifyDlcFolder(UUID.randomUUID().toString(),
                iFolder.getName(),
                gameId,
                dlcId);
    }

    public static UpdatifyDlcFolder updateToNew(Folder tFolder,
                                                IlluminateFolder iFolder,
                                                String gameId,
                                                String dlcId) {
        return new UpdatifyDlcFolder(tFolder.getUuid(),
                iFolder.getName(),
                gameId,
                dlcId);
    }

    public static UpdatifyDlcFolder with(Folder folder,
                                         String gameId,
                                         String dlcId) {
        return new UpdatifyDlcFolder(folder.getUuid(),
                folder.getName(),
                gameId,
                dlcId);
    }

    public String getDlcId() {
        return dlcId;
    }
}