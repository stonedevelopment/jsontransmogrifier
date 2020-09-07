package app.illuminate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Folder;

import static util.Constants.*;

public class IlluminateReplacementFolder extends IlluminateReplacement {

    @JsonCreator
    public IlluminateReplacementFolder(@JsonProperty(cUuid) String uuid,
                                       @JsonProperty(cFrom) Object from,
                                       @JsonProperty(cTo) Object to) {
        super(uuid, from, to);
    }

    @Override
    public Folder getFrom() {
        return (Folder) super.getFrom();
    }

    @Override
    public Folder getTo() {
        return (Folder) super.getTo();
    }
}
