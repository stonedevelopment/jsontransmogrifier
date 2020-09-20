package app.illuminate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Folder;

import static util.Constants.cName;
import static util.Constants.cUuid;

public class IlluminateFolder extends Folder {

    @JsonCreator
    public IlluminateFolder(@JsonProperty(cUuid) String uuid,
                            @JsonProperty(cName) String name) {
        super(uuid, name);
    }

    public static IlluminateFolder fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, IlluminateFolder.class);
    }

    @JsonIgnore
    @Override
    public String getUuid() {
        return super.getUuid();
    }
}
