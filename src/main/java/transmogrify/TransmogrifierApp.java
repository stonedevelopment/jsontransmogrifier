package transmogrify;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import transmogrify.model.data.DLCGameData;
import transmogrify.model.data.GameData;
import transmogrify.model.data.PrimaryGameData;
import transmogrify.model.json.JsonDlc;
import util.JSONUtil;

import java.io.IOException;

import static util.JSONUtil.writeOut;
import static util.LogUtil.logf;

/**
 * Converts old JSON data into separated JSON files with static UUIDs.
 * <p>
 * We want UUIDs to be static so if the User saves a crafting queue, the Engram doesn't change even after an update.
 */
public class TransmogrifierApp {
    static ObjectMapper mapper = new ObjectMapper();
    static PrimaryGameData primaryGameData;

    public static void main(String[] args) {
        try {
            JsonNode inObject = JSONUtil.parseIn();

            JsonNode dlcArray = inObject.get("dlc");
            for (JsonNode dlcNode : dlcArray) {
                GameData gameData;

                JsonDlc jsonDlc = mapper.treeToValue(dlcNode, JsonDlc.class);
                if (jsonDlc.type.equals("primary")) {
                    gameData = new PrimaryGameData(jsonDlc, inObject, mapper);
                    primaryGameData = (PrimaryGameData) gameData;
                } else {
                    gameData = new DLCGameData(jsonDlc, inObject, mapper, primaryGameData);
                }

                // TODO: 5/19/2020 Add The Center and Ragnarok engrams and resources

                gameData.mapGameData();
                writeGameDataToFile(gameData);
            }

            logf("Nullified resources: ", primaryGameData.nullifiedResources.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeGameDataToFile(GameData gameData) throws IOException {
        writeJsonToFile(gameData.buildFilePathForJSONExport(), gameData.generateJson());
    }

    private static void writeJsonToFile(String fileName, JsonNode outNode) throws IOException {
        writeOut(fileName, outNode);
    }
}