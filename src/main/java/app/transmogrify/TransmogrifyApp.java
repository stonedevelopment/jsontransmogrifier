package app.transmogrify;

import app.transmogrify.controller.TransmogGameData;
import app.transmogrify.model.game_data.DlcTransmogGameData;
import app.transmogrify.model.game_data.PrimaryTransmogGameData;
import app.transmogrify.model.json.JsonDlc;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
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
    private PrimaryTransmogGameData primaryGameData;
    private List<DlcTransmogGameData> dlcGameDataList;

    public TransmogrifyApp(JsonNode inNode) {
        this.inNode = inNode;
    }

    private static void writeJsonToFile(String filePath, JsonNode outNode) throws IOException {
        writeOut(cArkAssetsFilePath, filePath, outNode);
    }

    public void transmogrify() {
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

    public void export() {
        try {
            writeTransmogrifiedGameData();
            writeTransmogrification();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void transmogrifyPrimaryGameData(JsonDlc jsonDlc) {
        primaryGameData = new PrimaryTransmogGameData(inNode, jsonDlc);
    }

    private void transmogrifyDlcGameData(JsonDlc jsonDlc) {
        DlcTransmogGameData dlcTransmogGameData = new DlcTransmogGameData(inNode, jsonDlc, primaryGameData);
        dlcGameDataList.add(dlcTransmogGameData);
    }

    private void writeTransmogrifiedGameData() throws IOException {
        writePrimaryGameData();
        writeDlcGameData();
    }

    private void writePrimaryGameData() throws IOException {
        writeGameDataToFile(primaryGameData);
    }

    private void writeDlcGameData() throws IOException {
        for (DlcTransmogGameData dlcGameData : dlcGameDataList) {
            writeGameDataToFile(dlcGameData);
        }
    }

    private void writeTransmogrification() throws IOException {
        ObjectNode outNode = mapper.createObjectNode();
        outNode.set(cPrimary, mapper.valueToTree(primaryGameData.getDetailsObject()));

        ArrayNode outDlcArrayNode = mapper.createArrayNode();
        for (DlcTransmogGameData dlcGameData : dlcGameDataList) {
            outDlcArrayNode.add(mapper.valueToTree(dlcGameData.getDetailsObject()));
        }

        writeTransmogrifyDataToFile(outNode);
    }

    private void writeGameDataToFile(TransmogGameData gameData) throws IOException {
        String filePath = gameData.getDetailsObject().getFilePath();
        String fileName = gameData.getDetailsObject().getTransmogFile();
        String fullPath = filePath.concat(fileName);
        writeJsonToFile(fullPath, gameData.resolveToJson());
    }

    private void writeTransmogrifyDataToFile(JsonNode outNode) throws IOException {
        writeJsonToFile(cTransmogrificationFileName, outNode);
    }
}