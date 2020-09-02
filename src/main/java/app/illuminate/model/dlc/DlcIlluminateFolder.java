package app.illuminate.model.dlc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.dlc.DlcFolder;

import static util.Constants.*;

public class DlcIlluminateFolder extends DlcFolder {

    @JsonCreator
    public DlcIlluminateFolder(@JsonProperty(cUuid) String uuid,
                               @JsonProperty(cName) String name,
                               @JsonProperty(cGameId) String gameId,
                               @JsonProperty(cDlcId) String dlcId) {
        super(uuid, name, gameId, dlcId);
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