package app.illuminate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Replacement;

import static util.Constants.*;

public class IlluminateReplacement extends Replacement {

    @JsonCreator
    public IlluminateReplacement(@JsonProperty(cUuid) String uuid,
                                 @JsonProperty(cFrom) Object from,
                                 @JsonProperty(cTo) Object to) {
        super(uuid, from, to);
    }

    @JsonIgnore
    @Override
    public String getUuid() {
        return super.getUuid();
    }
}
