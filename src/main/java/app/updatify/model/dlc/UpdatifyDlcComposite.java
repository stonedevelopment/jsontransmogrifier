package app.updatify.model.dlc;

import app.illuminate.model.IlluminateComposite;
import app.updatify.model.UpdatifyComposite;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Composite;

import java.util.Date;
import java.util.UUID;

import static util.Constants.*;

public class UpdatifyDlcComposite extends UpdatifyComposite {
    private final String dlcId;

    @JsonCreator
    public UpdatifyDlcComposite(@JsonProperty(cUuid) String uuid,
                                @JsonProperty(cName) String name,
                                @JsonProperty(cImageFile) String imageFile,
                                @JsonProperty(cQuantity) int quantity,
                                @JsonProperty(cSourceId) String sourceId,
                                @JsonProperty(cIsEngram) boolean isEngram,
                                @JsonProperty(cCompositionId) String compositionId,
                                @JsonProperty(cLastUpdated) Date lastUpdated,
                                @JsonProperty(cGameId) String gameId,
                                @JsonProperty(cDlcId) String dlcId) {
        super(uuid, name, imageFile, quantity, sourceId, isEngram, compositionId, lastUpdated, gameId);
        this.dlcId = dlcId;
    }

    public static UpdatifyDlcComposite createFrom(IlluminateComposite iComposite,
                                                  String imageFile,
                                                  String sourceId,
                                                  String compositionId,
                                                  String gameId,
                                                  String dlcId) {
        return new UpdatifyDlcComposite(UUID.randomUUID().toString(),
                iComposite.getName(),
                imageFile,
                iComposite.getQuantity(),
                sourceId,
                iComposite.isEngram(),
                compositionId,
                new Date(),
                gameId,
                dlcId);
    }

    public static UpdatifyDlcComposite updateToNew(Composite tComposite,
                                                   IlluminateComposite iComposite,
                                                   String gameId,
                                                   String dlcId) {
        return new UpdatifyDlcComposite(tComposite.getUuid(),
                iComposite.getName(),
                tComposite.getImageFile(),
                iComposite.getQuantity(),
                tComposite.getSourceId(),
                iComposite.isEngram(),
                tComposite.getCompositionId(),
                new Date(),
                gameId,
                dlcId);
    }

    public static UpdatifyDlcComposite convertToNew(Composite fromComposite,
                                                    String toName,
                                                    String toSourceId,
                                                    String gameId,
                                                    String dlcId) {
        return new UpdatifyDlcComposite(UUID.randomUUID().toString(),
                toName,
                fromComposite.getImageFile(),
                fromComposite.getQuantity(),
                toSourceId,
                fromComposite.isEngram(),
                fromComposite.getCompositionId(),
                new Date(),
                gameId,
                dlcId);
    }

    public static UpdatifyDlcComposite with(Composite composite,
                                            String gameId,
                                            String dlcId) {
        return new UpdatifyDlcComposite(composite.getUuid(),
                composite.getName(),
                composite.getImageFile(),
                composite.getQuantity(),
                composite.getSourceId(),
                composite.isEngram(),
                composite.getCompositionId(),
                composite.getLastUpdated(),
                gameId,
                dlcId);
    }

    public String getDlcId() {
        return dlcId;
    }
}