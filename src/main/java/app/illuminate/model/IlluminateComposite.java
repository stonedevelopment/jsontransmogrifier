package app.illuminate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Composite;

import static util.Constants.*;

public class IlluminateComposite extends Composite {

    @JsonCreator
    public IlluminateComposite(@JsonProperty(cUuid) String uuid,
                               @JsonProperty(cName) String name,
                               @JsonProperty(cImageFile) String imageFile,
                               @JsonProperty(cQuantity) int quantity,
                               @JsonProperty(cSourceId) String sourceId,
                               @JsonProperty(cIsEngram) boolean isEngram,
                               @JsonProperty(cCompositionId) String compositionId) {
        super(uuid, name, imageFile, quantity, sourceId, isEngram, compositionId);
    }

    public static IlluminateComposite fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, IlluminateComposite.class);
    }

    @JsonIgnore
    @Override
    public String getSourceId() {
        return super.getSourceId();
    }

    @JsonIgnore
    @Override
    public String getCompositionId() {
        return super.getCompositionId();
    }
}
