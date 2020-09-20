package app.updatify.model;

import app.illuminate.model.IlluminateFolder;
import model.Folder;

import java.util.UUID;

public class UpdatifyFolder extends Folder {

    public UpdatifyFolder(String uuid, String name) {
        super(uuid, name);
    }

    public static Folder createFrom(IlluminateFolder iFolder) {
        return new Folder(UUID.randomUUID().toString(),
                iFolder.getName());
    }

    public static Folder updateToNew(Folder tFolder, IlluminateFolder iFolder) {
        return new Folder(tFolder.getUuid(),
                iFolder.getName());
    }
}