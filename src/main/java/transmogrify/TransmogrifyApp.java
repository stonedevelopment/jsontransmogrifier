package transmogrify;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.data.DLCGameData;
import model.data.GameData;
import model.data.PrimaryGameData;
import model.json.JsonDlc;
import util.JSONUtil;

import java.io.IOException;

import static util.JSONUtil.writeOut;
import static util.Log.f;

/**
 * Converts old JSON data into separated JSON files with static UUIDs.
 * <p>
 * We want UUIDs to be static so if the User saves a crafting queue, the Engram doesn't change even after an update.
 */
public class TransmogrifyApp {
    private static final String inFileName = "data_editable.json";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static PrimaryGameData primaryGameData;

    public static void main(String[] args) {
        try {
            ObjectNode outNode = mapper.createObjectNode();
            ArrayNode outDlcArrayNode = mapper.createArrayNode();

            JsonNode inNode = JSONUtil.parseIn(inFileName);
            JsonNode inDlcArrayNode = inNode.get("dlc");
            for (JsonNode dlcNode : inDlcArrayNode) {
                JsonDlc jsonDlc = mapper.treeToValue(dlcNode, JsonDlc.class);
                if (jsonDlc.type.equals("primary")) {
                    outNode.set("primary", transmogrifyPrimaryGameData(jsonDlc, inNode));
                } else {
                    outDlcArrayNode.add(transmogrifyDlcGameData(jsonDlc, inNode));
                }
            }

            outNode.set("dlc", outDlcArrayNode);
            writeTransmogrifyDataToFile(outNode);

            f("Nullified resources: ", primaryGameData.nullifiedResources.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static JsonNode transmogrifyPrimaryGameData(JsonDlc jsonDlc, JsonNode inObject) throws IOException {
        primaryGameData = new PrimaryGameData(jsonDlc, inObject, mapper);
        primaryGameData.mapGameData();
        writeGameDataToFile(primaryGameData);
        return mapper.valueToTree(primaryGameData.getDetailsObject());
    }

    static JsonNode transmogrifyDlcGameData(JsonDlc jsonDlc, JsonNode inObject) throws IOException {
        DLCGameData gameData = new DLCGameData(jsonDlc, inObject, mapper, primaryGameData);
        gameData.mapGameData();
        writeGameDataToFile(gameData);
        return mapper.valueToTree(gameData.getDetailsObject());
    }

    private static void writeGameDataToFile(GameData gameData) throws IOException {
        writeJsonToFile(gameData.buildFilePathForJSONExport(), gameData.generateJson());
    }

    private static void writeTransmogrifyDataToFile(JsonNode outNode) throws IOException {
        writeJsonToFile(buildFilePathForTransmogrificationFile(), outNode);
    }

    private static String buildFilePathForTransmogrificationFile() {
        return "src/assets/transmogrification.json";
    }

    private static void writeJsonToFile(String fileName, JsonNode outNode) throws IOException {
        writeOut(fileName, outNode);
    }
}