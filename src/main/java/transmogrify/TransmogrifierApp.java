package transmogrify;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import transmogrify.model.data.DLCGameData;
import transmogrify.model.data.PrimaryGameData;
import util.JSONUtil;

import java.io.IOException;
import java.util.UUID;

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

            primaryGameData = new PrimaryGameData(inObject, mapper);
            primaryGameData.mapGameData();
            writePrimaryJsonToFile();

            for (long dlcId = 2; dlcId <= 5; dlcId++) {
                DLCGameData gameData = new DLCGameData(dlcId, inObject, mapper, primaryGameData);
                gameData.mapGameData();
                writeDLCJsonToFile(gameData);
            }

            logf("Nullified resources: ", primaryGameData.nullifiedResources.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writePrimaryJsonToFile() throws IOException {
        writeJsonToFile(primaryGameData.buildFilePathForJSONExport(), primaryGameData.generateJson());
    }

    private static void writeDLCJsonToFile(DLCGameData gameData) throws IOException {
        writeJsonToFile(gameData.buildFilePathForJSONExport(), gameData.generateJson());
    }

    private static void writeJsonToFile(String fileName, JsonNode outNode) throws IOException {
        writeOut(fileName, outNode);
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}