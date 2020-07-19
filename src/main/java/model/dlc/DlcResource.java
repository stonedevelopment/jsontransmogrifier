package model.dlc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Resource;

import java.util.Date;

import static util.Constants.*;

public class DlcResource extends Resource {
    private final String dlcId;

    @JsonCreator
    public DlcResource(@JsonProperty(cUuid) String uuid,
                       @JsonProperty(cName) String name,
                       @JsonProperty(cDescription) String description,
                       @JsonProperty(cImageFile) String imageFile,
                       @JsonProperty(cLastUpdated) Date lastUpdated,
                       @JsonProperty(cGameId) String gameId,
                       @JsonProperty(cDlcId) String dlcId) {
        super(uuid, name, description, imageFile, lastUpdated, gameId);
        this.dlcId = dlcId;
    }

    public static DlcResource fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, DlcResource.class);
    }

    public String getDlcId() {
        return dlcId;
    }
}
