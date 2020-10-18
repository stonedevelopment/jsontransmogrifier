package app.updatify.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Composite;

import java.util.Date;
import java.util.UUID;

import static util.Constants.*;

public class UpdatifyDlcComposite extends UpdatifyComposite {

    @JsonCreator
    public UpdatifyDlcComposite(@JsonProperty(cUuid) String uuid,
                                @JsonProperty(cName) String name,
                                @JsonProperty(cImageFile) String imageFile,
                                @JsonProperty(cQuantity) int quantity,
                                @JsonProperty(cSourceId) String sourceId,
                                @JsonProperty(cIsEngram) boolean isEngram,
                                @JsonProperty(cCompositionId) String compositionId,
                                @JsonProperty(cLastUpdated) Date lastUpdated,
                                @JsonProperty(cGameId) String gameId) {
        super(uuid, name, imageFile, quantity, sourceId, isEngram, compositionId, lastUpdated, gameId);
    }

    public static UpdatifyDlcComposite convertToNew(Composite fromComposite, String toName, String toSourceId,
                                                    String gameId) {
        return new UpdatifyDlcComposite(UUID.randomUUID().toString(),
                toName,
                fromComposite.getImageFile(),
                fromComposite.getQuantity(),
                toSourceId,
                fromComposite.isEngram(),
                fromComposite.getCompositionId(),
                new Date(),
                gameId);
    }
}