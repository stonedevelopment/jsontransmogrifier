package app.transmogrify;

import app.transmogrify.controller.TransmogGameData;
import app.transmogrify.model.details.DlcTransmogDetails;
import app.transmogrify.model.details.TransmogDetails;
import app.transmogrify.model.game_data.DlcTransmogGameData;
import app.transmogrify.model.game_data.PrimaryTransmogGameData;
import app.transmogrify.model.json.JsonDlc;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

import static util.Constants.*;
import static util.JSONUtil.writeOut;

/**
 * Converts old JSON data into separated JSON files with static UUIDs.
 * <p>
 * We want UUIDs to be static so if the User saves a crafting queue, the Engram doesn't change even after an update.
 */
public class TransmogrifyApp {
    private final ObjectMapper mapper = new ObjectMapper();
    private final JsonNode inNode;
    private final List<DlcTransmogGameData> dlcGameDataList = new ArrayList<>();
    private PrimaryTransmogGameData primaryGameData;

    public TransmogrifyApp(JsonNode inNode) {
        this.inNode = inNode;
    }

    public void start() {
        JsonNode inDlcArrayNode = inNode.get(cJsonDlc);
        for (JsonNode dlcNode : inDlcArrayNode) {
            JsonDlc jsonDlc = mapper.convertValue(dlcNode, JsonDlc.class);
            if (jsonDlc.type.equals(cDlcTypePrimary)) {
                transmogrifyPrimaryGameData(jsonDlc);
            } else {
                transmogrifyDlcGameData(jsonDlc);
            }
        }
    }

    public PrimaryTransmogGameData getPrimaryGameData() {
        return primaryGameData;
    }

    public List<DlcTransmogGameData> getDlcGameDataList() {
        return dlcGameDataList;
    }

    private void transmogrifyPrimaryGameData(JsonDlc jsonDlc) {
        primaryGameData = PrimaryTransmogGameData.with(inNode, jsonDlc);
    }

    private void transmogrifyDlcGameData(JsonDlc jsonDlc) {
        dlcGameDataList.add(DlcTransmogGameData.with(inNode, jsonDlc, primaryGameData));
    }

    public void export() {
        writeTransmogrifiedGameData();
        writeTransmogrification();
    }

    private void writeTransmogrifiedGameData() {
        writePrimaryGameData();
        writeDlcGameData();
    }

    private void writePrimaryGameData() {
        writeGameDataToFile(primaryGameData);
    }

    private void writeDlcGameData() {
        for (DlcTransmogGameData dlcGameData : dlcGameDataList) {
            writeGameDataToFile(dlcGameData);
        }
    }

    private void writeGameDataToFile(TransmogGameData gameData) {
        String filePath = gameData.getDetailsObject().getFilePath();
        String fileName = gameData.getDetailsObject().getTransmogFile();
        String fullPath = filePath.concat(fileName);
        writeJsonToFile(fullPath, gameData.resolveToJson());
    }

    private void writeTransmogrification() {
        ObjectNode outNode = mapper.createObjectNode();

        ObjectNode primaryNode = mapper.createObjectNode();
        TransmogDetails primaryDetails = primaryGameData.getDetailsObject();
        primaryNode.put(cUuid, primaryDetails.getUuid());
        primaryNode.put(cName, primaryDetails.getName());
        primaryNode.put(cFilePath, primaryDetails.getFilePath());
        primaryNode.put(cTransmogFile, primaryDetails.getTransmogFile());
        primaryNode.put(cLastUpdated, primaryDetails.getLastUpdated().getTime());
        outNode.set(cPrimary, primaryNode);

        ArrayNode outDlcArrayNode = mapper.createArrayNode();
        for (DlcTransmogGameData dlcGameData : dlcGameDataList) {
            ObjectNode dlcNode = mapper.createObjectNode();
            DlcTransmogDetails dlcDetails = dlcGameData.getDetailsObject();
            dlcNode.put(cUuid, dlcDetails.getUuid());
            dlcNode.put(cName, dlcDetails.getName());
            dlcNode.put(cFilePath, dlcDetails.getFilePath());
            dlcNode.put(cTransmogFile, dlcDetails.getTransmogFile());
            dlcNode.put(cLastUpdated, dlcDetails.getLastUpdated().getTime());
            outDlcArrayNode.add(dlcNode);
        }
        outNode.set(cDlc, outDlcArrayNode);

        writeJsonToFile(cTransmogrificationFileName, outNode);
    }

    private void writeJsonToFile(String filePath, JsonNode outNode) {
        writeOut(cArkAssetsFilePath, filePath, outNode);
    }
}