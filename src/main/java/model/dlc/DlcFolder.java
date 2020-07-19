package model.dlc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Folder;

import static util.Constants.*;

public class DlcFolder extends Folder {
    private final String dlcId;

    @JsonCreator
    public DlcFolder(@JsonProperty(cUuid) String uuid,
                     @JsonProperty(cName) String name,
                     @JsonProperty(cGameId) String gameId,
                     @JsonProperty(cDlcId) String dlcId) {
        super(uuid, name, gameId);
        this.dlcId = dlcId;
    }

    public static DlcFolder fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, DlcFolder.class);
    }

    public String getDlcId() {
        return dlcId;
    }
}