package app.illuminate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static util.Constants.cFrom;
import static util.Constants.cTo;

public abstract class IlluminateReplacement {
    private final Object from;
    private final Object to;

    @JsonCreator
    public IlluminateReplacement(@JsonProperty(cFrom) Object from,
                                 @JsonProperty(cTo) Object to) {
        this.from = from;
        this.to = to;
    }

    public Object getFrom() {
        return from;
    }

    public Object getTo() {
        return to;
    }
}
