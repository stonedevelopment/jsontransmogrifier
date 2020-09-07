package app.illuminate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Station;

import static util.Constants.*;

public class IlluminateReplacementStation extends IlluminateReplacement {

    @JsonCreator
    public IlluminateReplacementStation(@JsonProperty(cUuid) String uuid,
                                        @JsonProperty(cFrom) Object from,
                                        @JsonProperty(cTo) Object to) {
        super(uuid, from, to);
    }

    @Override
    public Station getFrom() {
        return (Station) super.getFrom();
    }

    @Override
    public Station getTo() {
        return (Station) super.getTo();
    }
}
