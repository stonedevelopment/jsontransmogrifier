package app.illuminate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Engram;

import static util.Constants.*;

public class IlluminateReplacementEngram extends IlluminateReplacement {

    @JsonCreator
    public IlluminateReplacementEngram(@JsonProperty(cUuid) String uuid,
                                       @JsonProperty(cFrom) Object from,
                                       @JsonProperty(cTo) Object to) {
        super(uuid, from, to);
    }

    @Override
    public Engram getFrom() {
        return (Engram) super.getFrom();
    }

    @Override
    public Engram getTo() {
        return (Engram) super.getTo();
    }
}
