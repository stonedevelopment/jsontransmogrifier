package app.illuminate.model.dlc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.dlc.DlcResource;

import java.util.Date;

import static util.Constants.*;

public class DlcIlluminateResource extends DlcResource {

    @JsonCreator
    public DlcIlluminateResource(@JsonProperty(cUuid) String uuid,
                                 @JsonProperty(cName) String name,
                                 @JsonProperty(cDescription) String description,
                                 @JsonProperty(cImageFile) String imageFile,
                                 @JsonProperty(cLastUpdated) Date lastUpdated,
                                 @JsonProperty(cGameId) String gameId,
                                 @JsonProperty(cDlcId) String dlcId) {
        super(uuid, name, description, imageFile, lastUpdated, gameId, dlcId);
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
