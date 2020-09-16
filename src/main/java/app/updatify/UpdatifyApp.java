package app.updatify;

import app.illuminate.controller.IlluminateGameData;
import app.illuminate.model.details.IlluminateDetails;
import app.transmogrify.controller.TransmogGameData;
import app.transmogrify.model.details.TransmogDetails;
import app.updatify.controller.UpdatifyController;
import com.fasterxml.jackson.databind.JsonNode;
import util.Log;

import static util.Constants.cPrimary;

/**
 * Converts Illuminated JSON files to importable data files with matching UUIDs.
 * <p>
 * We want UUIDs to be static so if the User saves a crafting queue, the Engram doesn't change even after an update.
 */
public class UpdatifyApp {
    private final JsonNode transmogrificationNode;
    private final JsonNode illuminationNode;
    private UpdatifyController primaryController;

    public UpdatifyApp(JsonNode transmogrificationNode, JsonNode illuminationNode) {
        this.transmogrificationNode = transmogrificationNode;
        this.illuminationNode = illuminationNode;
    }

    /**
     * Start app
     */
    public void start() {
        updatifyPrimary();
    }

    /**
     * Export resolved data to database-ready json file
     */
    public void export() {

    }

    private void updatifyPrimary() {
        JsonNode primaryTransmogrifiedNode = transmogrificationNode.get(cPrimary);
        JsonNode primaryIlluminatedNode = illuminationNode.get(cPrimary);
        primaryController = new UpdatifyController(primaryTransmogrifiedNode, primaryIlluminatedNode);
        primaryController.start();

        //  map transmog
        //  map illuminated
        //  section by section, compare and update each difference
    }

    private void updatifyDlc() {

    }

    private void updatifyDetails(TransmogGameData transmogGameData, IlluminateGameData illuminateGameData) {
        TransmogDetails transmogDetails = transmogGameData.getDetailsObject();
        IlluminateDetails illuminateDetails = illuminateGameData.getDetailsObject();
        if (transmogDetails.equals(illuminateDetails)) {
            //  no change
            Log.d("No change required.");
            //  save to out node for output
        } else {
            //  create new details object
            //  overwrite transmog details with illuminate details
            Log.d("Something's different!");
        }
    }
}
