package app.updatify.model;

import app.illuminate.model.IlluminateEngram;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.Engram;

import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import static util.Constants.cComposition;

public class UpdatifyEngram extends Engram {

    public UpdatifyEngram(String uuid, String name, String description, String imageFile, int level, int yield, int points, int xp, int craftingTime, Date lastUpdated) {
        super(uuid, name, description, imageFile, level, yield, points, xp, craftingTime, lastUpdated);
    }

    public static IlluminateEngram fromJson(JsonNode jsonNode) {
        ObjectNode outNode = new ObjectMapper().createObjectNode();

        Iterator<String> fieldNames = jsonNode.fieldNames();
        Iterator<JsonNode> elements = jsonNode.elements();

        while (elements.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode element = elements.next();
            if (fieldName.equals(cComposition)) continue;

            outNode.set(fieldName, element);
        }

        return IlluminateEngram.fromJson(outNode);
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