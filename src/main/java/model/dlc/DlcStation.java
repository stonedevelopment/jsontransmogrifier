package model.dlc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Station;

import java.util.Date;

import static util.Constants.*;

public class DlcStation extends Station {
    private final String dlcId;

    @JsonCreator
    public DlcStation(@JsonProperty(cUuid) String uuid,
                      @JsonProperty(cName) String name,
                      @JsonProperty(cImageFile) String imageFile,
                      @JsonProperty(cEngramId) String engramId,
                      @JsonProperty(cLastUpdated) Date lastUpdated,
                      @JsonProperty(cGameId) String gameId,
                      @JsonProperty(cDlcId) String dlcId) {
        super(uuid, name, imageFile, engramId, lastUpdated, gameId);
        this.dlcId = dlcId;
    }

    public static DlcStation fromJson(JsonNode node) {
        return new ObjectMapper().convertValue(node, DlcStation.class);
    }

    public String getDlcId() {
        return dlcId;
    }
}
