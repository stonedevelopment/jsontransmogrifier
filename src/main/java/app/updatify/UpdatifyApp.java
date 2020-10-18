package app.updatify;

import app.updatify.controller.UpdatifyController;
import app.updatify.controller.UpdatifyDlcController;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static util.Constants.*;
import static util.JSONUtil.writeOut;

/**
 * Converts Illuminated JSON files to importable data files with matching UUIDs.
 * <p>
 * We want UUIDs to be static so if the User saves a crafting queue, the Engram doesn't change even after an update.
 */
public class UpdatifyApp {
    private final ObjectMapper mapper = new ObjectMapper();
    private final JsonNode transmogrificationNode;
    private final JsonNode illuminationNode;
    private UpdatifyController primaryController;
    private ObjectNode updatificationNode = mapper.createObjectNode();

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

    /**
     * Write updatification json node to file.
     */
    public void export() {
        writeJsonToFile();
    }

    private void writeJsonToFile() {
        writeOut(cArkAssetsFilePath, cUpdatificationFileName, updatificationNode);
    }

    private void updatifyPrimary() {
        JsonNode tNode = transmogrificationNode.get(cPrimary);
        JsonNode iNode = illuminationNode.get(cPrimary);
        primaryController = UpdatifyController.from(tNode, iNode);
        primaryController.start();
        primaryController.export();

        createPrimaryUpdatificationNode();
    }

    private void createPrimaryUpdatificationNode() {
        ObjectNode primaryNode = mapper.createObjectNode();
        primaryNode.put(cUuid, primaryController.getUuid());
        primaryNode.put(cName, primaryController.getName());
        primaryNode.put(cFilePath, primaryController.getUpdatifiedFilePath());
        primaryNode.put(cLastUpdated, primaryController.getLastUpdated().getTime());
        setPrimaryUpdatificationNode(primaryNode);
    }

    private void setPrimaryUpdatificationNode(JsonNode primaryNode) {
        updatificationNode.set(cPrimary, primaryNode);
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

        createDlcUpdatificationNode(dlcController);
    }

    private void createDlcUpdatificationNode(UpdatifyDlcController dlcController) {
        ObjectNode dlcNode = mapper.createObjectNode();
        dlcNode.put(cUuid, dlcController.getUuid());
        dlcNode.put(cName, dlcController.getName());
        dlcNode.put(cFilePath, dlcController.getUpdatifiedFilePath());
        dlcNode.put(cLastUpdated, dlcController.getLastUpdated().getTime());
        addDlcUpdatificationNode(dlcNode);
    }

    private void addDlcUpdatificationNode(JsonNode dlcNode) {
        ArrayNode dlcArray = (ArrayNode) updatificationNode.get(cDlc);
        if (dlcArray == null) {
            dlcArray = mapper.createArrayNode();
        }
        dlcArray.add(dlcNode);
        updatificationNode.set(cDlc, dlcArray);
    }
}