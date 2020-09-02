package app.illuminate.model.dlc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.dlc.DlcComposition;

import java.util.Date;

import static util.Constants.*;

public class DlcIlluminateComposition extends DlcComposition {

    @JsonCreator
    public DlcIlluminateComposition(@JsonProperty(cUuid) String uuid,
                                    @JsonProperty(cEngramId) String engramId,
                                    @JsonProperty(cLastUpdated) Date lastUpdated,
                                    @JsonProperty(cGameId) String gameId,
                                    @JsonProperty(cDlcId) String dlcId) {
        super(uuid, engramId, lastUpdated, gameId, dlcId);
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