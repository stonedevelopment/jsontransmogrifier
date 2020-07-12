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
    private static final ObjectMapper mapper = new ObjectMapper();
    private static PrimaryTransmogGameData primaryGameData;
    private static List<DlcTransmogGameData> dlcGameDataList;

    public TransmogrifyApp(JsonNode inNode) {
        transmogrifyGameData(inNode);

        try {
            writeTransmogrifiedGameData();
            writeTransmogrification();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void transmogrifyGameData(JsonNode inNode) {
        JsonNode inDlcArrayNode = inNode.get(cJsonDlc);
        for (JsonNode dlcNode : inDlcArrayNode) {
            JsonDlc jsonDlc = mapper.convertValue(dlcNode, JsonDlc.class);
            if (jsonDlc.type.equals(cDlcTypePrimary)) {
                transmogrifyPrimaryGameData(inNode, jsonDlc);
            } else {
                transmogrifyDlcGameData(inNode, jsonDlc);
            }
        }
    }

    private void transmogrifyPrimaryGameData(JsonNode inObject, JsonDlc jsonDlc) {
        primaryGameData = new PrimaryTransmogGameData(inObject, jsonDlc);
    }

    private void transmogrifyDlcGameData(JsonNode inNode, JsonDlc jsonDlc) {
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

    private void writeJsonToFile(String filePath, JsonNode outNode) throws IOException {
        writeOut(cArkAssetsFilePath, filePath, outNode);
    }
}