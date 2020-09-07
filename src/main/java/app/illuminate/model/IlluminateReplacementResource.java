package app.illuminate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Resource;

import static util.Constants.*;

public class IlluminateReplacementResource extends IlluminateReplacement {

    @JsonCreator
    public IlluminateReplacementResource(@JsonProperty(cUuid) String uuid,
                                         @JsonProperty(cFrom) Object from,
                                         @JsonProperty(cTo) Object to) {
        super(uuid, from, to);
    }

    @Override
    public Resource getFrom() {
        return (Resource) super.getFrom();
    }

    @Override
    public Resource getTo() {
        return (Resource) super.getTo();
    }
}
