package app.updatify.model;

import app.illuminate.model.IlluminateComposition;
import model.Composition;

import java.util.UUID;

public class UpdatifyComposition extends Composition {

    public UpdatifyComposition(String uuid, String engramId) {
        super(uuid, engramId);
    }

    public static Composition createFrom(IlluminateComposition iComposition) {
        return new Composition(UUID.randomUUID().toString(),
                iComposition.getEngramId());
    }

    public static Composition updateToNew(Composition tComposition, IlluminateComposition iComposition) {
        return new Composition(tComposition.getUuid(),
                iComposition.getEngramId());
    }
}