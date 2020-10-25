package app.updatify.model;

import app.illuminate.model.IlluminateComposite;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Composite;

import java.util.Date;
import java.util.UUID;

import static util.Constants.*;

public class UpdatifyComposite extends Composite {
    private final String gameId;

    @JsonCreator
    public UpdatifyComposite(@JsonProperty(cUuid) String uuid,
                             @JsonProperty(cName) String name,
                             @JsonProperty(cImageFile) String imageFile,
                             @JsonProperty(cQuantity) int quantity,
                             @JsonProperty(cSourceId) String sourceId,
                             @JsonProperty(cIsEngram) boolean isEngram,
                             @JsonProperty(cCompositionId) String compositionId,
                             @JsonProperty(cLastUpdated) Date lastUpdated,
                             @JsonProperty(cGameId) String gameId) {
        super(uuid, name, imageFile, quantity, sourceId, isEngram, compositionId, lastUpdated);
        this.gameId = gameId;
    }

    public static UpdatifyComposite createFrom(IlluminateComposite iComposite,
                                               String imageFile,
                                               String sourceId,
                                               String compositionId,
                                               String gameId) {
        return new UpdatifyComposite(UUID.randomUUID().toString(),
                iComposite.getName(),
                imageFile,
                iComposite.getQuantity(),
                sourceId,
                iComposite.isEngram(),
                compositionId,
                new Date(),
                gameId);
    }

    public static UpdatifyComposite updateToNew(Composite tComposite,
                                                IlluminateComposite iComposite,
                                                String gameId) {
        return new UpdatifyComposite(tComposite.getUuid(),
                iComposite.getName(),
                tComposite.getImageFile(),
                iComposite.getQuantity(),
                tComposite.getSourceId(),
                iComposite.isEngram(),
                tComposite.getCompositionId(),
                new Date(),
                gameId);
    }

    public static UpdatifyComposite with(Composite composite,
                                         String gameId) {
        return new UpdatifyComposite(composite.getUuid(),
                composite.getName(),
                composite.getImageFile(),
                composite.getQuantity(),
                composite.getSourceId(),
                composite.isEngram(),
                composite.getCompositionId(),
                composite.getLastUpdated(),
                gameId);
    }

    public String getDlcId() {
        return gameId;
    }
}