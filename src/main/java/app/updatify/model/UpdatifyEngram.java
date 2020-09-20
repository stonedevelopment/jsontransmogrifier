package app.updatify.model;

import app.illuminate.model.IlluminateEngram;
import model.Engram;

import java.util.Date;
import java.util.UUID;

public class UpdatifyEngram extends Engram {

    public UpdatifyEngram(String uuid, String name, String description, String imageFile, int level, int yield, int points, int xp, int craftingTime, Date lastUpdated) {
        super(uuid, name, description, imageFile, level, yield, points, xp, craftingTime, lastUpdated);
    }

    public static Engram createFrom(IlluminateEngram iEngram) {
        return new Engram(UUID.randomUUID().toString(),
                iEngram.getName(),
                iEngram.getDescription(),
                iEngram.getImageFile(),
                iEngram.getLevel(),
                iEngram.getYield(),
                iEngram.getPoints(),
                iEngram.getXp(),
                iEngram.getCraftingTime(),
                new Date());
    }

    public static Engram updateToNew(Engram tEngram, IlluminateEngram iEngram) {
        return new Engram(tEngram.getUuid(),
                iEngram.getName(),
                iEngram.getDescription(),
                iEngram.getImageFile(),
                iEngram.getLevel(),
                iEngram.getYield(),
                iEngram.getPoints(),
                iEngram.getXp(),
                iEngram.getCraftingTime(),
                new Date());
    }
}