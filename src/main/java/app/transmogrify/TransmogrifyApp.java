package app.transmogrify;

import app.transmogrify.controller.TransmogGameData;
import app.transmogrify.model.game_data.DlcTransmogGameData;
import app.transmogrify.model.game_data.PrimaryTransmogGameData;
import app.transmogrify.model.json.JsonDlc;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import util.JSONUtil;

import java.io.IOException;

import static util.Constants.*;
import static util.JSONUtil.writeOut;

/**
 * Converts old JSON data into separated JSON files with static UUIDs.
 * <p>
 * We want UUIDs to be static so if the User saves a crafting queue, the Engram doesn't change even after an update.
 */
public class TransmogrifyApp {
    private static final String inFileName = "data_editable.json";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static PrimaryTransmogGameData primaryGameData;

    public static void main(String[] args) {
        try {
            ObjectNode outNode = mapper.createObjectNode();
            ArrayNode outDlcArrayNode = mapper.createArrayNode();

            JsonNode inNode = JSONUtil.parseIn(inFileName);
            JsonNode inDlcArrayNode = inNode.get(cJsonDlc);
            for (JsonNode dlcNode : inDlcArrayNode) {
                JsonDlc jsonDlc = mapper.treeToValue(dlcNode, JsonDlc.class);
                if (jsonDlc.type.equals(cDlcTypePrimary)) {
                    outNode.set(cPrimary, transmogrifyPrimaryGameData(inNode, jsonDlc));
                } else {
                    outDlcArrayNode.add(transmogrifyDlcGameData(inNode, jsonDlc));
                }
            }

            outNode.set(cDlc, outDlcArrayNode);
            writeTransmogrifyDataToFile(outNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static JsonNode transmogrifyPrimaryGameData(JsonNode inObject, JsonDlc jsonDlc) throws IOException {
        primaryGameData = new PrimaryTransmogGameData(inObject, jsonDlc);
        writeGameDataToFile(primaryGameData);
        return mapper.valueToTree(primaryGameData.getDetailsObject());
    }

    static JsonNode transmogrifyDlcGameData(JsonNode inObject, JsonDlc jsonDlc) throws IOException {
        DlcTransmogGameData gameData = new DlcTransmogGameData(inObject, jsonDlc, primaryGameData);
        writeGameDataToFile(gameData);
        return mapper.valueToTree(gameData.getDetailsObject());
    }

    private static void writeGameDataToFile(TransmogGameData gameData) throws IOException {
        writeJsonToFile(gameData.getDetailsObject().getTransmogFile(), gameData.resolveToJson());
    }

    private static void writeTransmogrifyDataToFile(JsonNode outNode) throws IOException {
        writeJsonToFile(cTransmogrificationFileName, outNode);
    }

    private static void writeJsonToFile(String fileName, JsonNode outNode) throws IOException {
        writeOut(fileName, outNode);
    }
}