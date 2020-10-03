package app.updatify;

import app.updatify.controller.UpdatifyController;
import app.updatify.controller.UpdatifyDlcController;
import com.fasterxml.jackson.databind.JsonNode;

import static util.Constants.*;

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
        updatifyDlcList();
    }

    private void updatifyPrimary() {
        JsonNode tNode = transmogrificationNode.get(cPrimary);
        JsonNode iNode = illuminationNode.get(cPrimary);
        primaryController = UpdatifyController.from(tNode, iNode);
        primaryController.start();
        primaryController.export();
    }

    /**
     * Iterate through Illumination node
     * Search for DLC name in Transmog node
     * If not found, it's a new DLC
     */
    private void updatifyDlcList() {
        JsonNode tNodes = transmogrificationNode.get(cDlc);
        JsonNode iNodes = illuminationNode.get(cDlc);
        for (JsonNode iNode : iNodes) {
            String iName = iNode.get(cName).asText();

            boolean isNew = true;
            for (JsonNode tNode : tNodes) {
                String tName = tNode.get(cName).asText();
                if (iName.equals(tName)) {
                    updatifyDlc(tNode, iNode);
                    isNew = false;
                }
            }

            if (isNew) {
                updatifyDlc(null, iNode);
            }
        }
    }

    private void updatifyDlc(JsonNode tNode, JsonNode iNode) {
        UpdatifyDlcController dlcController = UpdatifyDlcController.from(tNode, iNode, primaryController);
        dlcController.start();
        dlcController.export();
    }
}