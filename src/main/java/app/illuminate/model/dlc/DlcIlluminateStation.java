package app.illuminate.model.dlc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.dlc.DlcStation;

import java.util.Date;

import static util.Constants.*;

public class DlcIlluminateStation extends DlcStation {

    @JsonCreator
    public DlcIlluminateStation(@JsonProperty(cUuid) String uuid,
                                @JsonProperty(cName) String name,
                                @JsonProperty(cImageFile) String imageFile,
                                @JsonProperty(cEngramId) String engramId,
                                @JsonProperty(cLastUpdated) Date lastUpdated,
                                @JsonProperty(cGameId) String gameId,
                                @JsonProperty(cDlcId) String dlcId) {
        super(uuid, name, imageFile, engramId, lastUpdated, gameId, dlcId);
    }

    @JsonIgnore
    @Override
    public String getEngramId() {
        return super.getEngramId();
    }

    @JsonIgnore
    @Override
    public String getGameId() {
        return super.getGameId();
    }

    @JsonIgnore
    @Override
    public String getDlcId() {
        return super.getDlcId();
    }
}
