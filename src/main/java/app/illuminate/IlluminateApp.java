package app.illuminate;

import app.illuminate.controller.IlluminateGameData;
import app.illuminate.model.DlcIlluminateGameData;
import app.illuminate.model.PrimaryIlluminateGameData;
import app.illuminate.model.details.IlluminateDetails;
import app.transmogrify.model.details.DlcTransmogDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static util.Constants.*;
import static util.JSONUtil.parseIn;
import static util.JSONUtil.writeOut;

/**
 * Converts transmogrified json files into, separate, readable files for easy editing
 * <p>
 * todo Create illuminate.json to tell Updatify what needs to be converted back into raw uuids.
 * Illuminate!
 */
public class IlluminateApp {
    private static final ObjectMapper mapper = new ObjectMapper();

    private static PrimaryIlluminateGameData primaryGameData;

    public static void main(String[] args) {
        //  load transmogrification.json
        //  load parent object
        //  convert transmogrified.json file into separate readable json files
        try {
            JsonNode transmogNode = parseIn(cArkAssetsFilePath, cTransmogrificationFileName);
            illuminatePrimaryNode(transmogNode);
//            illuminateDlcNode(transmogNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void illuminatePrimaryNode(JsonNode transmogNode) throws IOException {
        IlluminateDetails details = IlluminateDetails.from(transmogNode.get(cPrimary));
        String fileName = details.buildTransmogFilePath();

        JsonNode transmogrifiedNode = parseIn(cArkAssetsFilePath, fileName);
        primaryGameData = new PrimaryIlluminateGameData(transmogrifiedNode);
        writeGameDataToFiles(primaryGameData);
    }

    private static void illuminateDlcNode(JsonNode transmogNode) throws IOException {
        JsonNode dlcArrayNode = transmogNode.get(cDlc);
        for (JsonNode dlcNode : dlcArrayNode) {
            DlcTransmogDetails details = mapper.convertValue(dlcNode, DlcTransmogDetails.class);
            String filePath = details.getTransmogFile();

            JsonNode transmogrifiedNode = parseIn(cArkAssetsFilePath, filePath);
            DlcIlluminateGameData gameData = new DlcIlluminateGameData(transmogrifiedNode);
            writeGameDataToFiles(gameData);
        }
    }

    private static void writeGameDataToFiles(IlluminateGameData gameData) throws IOException {
        JsonNode resolvedNode = gameData.resolveToJson();

        writeJsonToFile(gameData.getFilePathForResources(), resolvedNode.get(cResources));
        writeJsonToFile(gameData.getFilePathForDirectory(), resolvedNode.get(cDirectory));
    }

    private static void writeJsonToFile(String fileName, JsonNode outNode) throws IOException {
        writeOut(cArkAssetsFilePath, fileName, outNode);
    }
}
