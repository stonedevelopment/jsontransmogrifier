package app.illuminate.model.dlc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.dlc.DlcComposite;

import static util.Constants.*;

public class DlcIlluminateComposite extends DlcComposite {

    @JsonCreator
    public DlcIlluminateComposite(@JsonProperty(cUuid) String uuid,
                                  @JsonProperty(cName) String name,
                                  @JsonProperty(cImageFile) String imageFile,
                                  @JsonProperty(cQuantity) int quantity,
                                  @JsonProperty(cSourceId) String sourceId,
                                  @JsonProperty(cIsEngram) boolean isEngram,
                                  @JsonProperty(cCompositionId) String compositionId,
                                  @JsonProperty(cGameId) String gameId,
                                  @JsonProperty(cDlcId) String dlcId) {
        super(uuid, name, imageFile, quantity, sourceId, isEngram, compositionId, gameId, dlcId);
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