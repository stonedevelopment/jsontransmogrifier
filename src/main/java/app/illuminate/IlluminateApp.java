package app.illuminate;

import app.illuminate.controller.IlluminateGameData;
import app.illuminate.model.DlcIlluminateGameData;
import app.illuminate.model.PrimaryIlluminateGameData;
import app.illuminate.model.details.DlcIlluminateDetails;
import app.illuminate.model.details.IlluminateDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

import static util.Constants.*;
import static util.JSONUtil.parseIn;
import static util.JSONUtil.writeOut;

/**
 * Converts transmogrified json files into, separate, readable files for easy editing
 * <p>
 * todo Create illumination.json to tell Updatify what needs to be converted back into raw uuids.
 * Illuminate!
 */
public class IlluminateApp {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static PrimaryIlluminateGameData primaryGameData;
    private static List<DlcIlluminateGameData> dlcGameDataList = new ArrayList<>();
    private final JsonNode inNode;

    public IlluminateApp(JsonNode inNode) {
        this.inNode = inNode;
    }

    public void illuminate() {
        illuminatePrimaryNode();
        illuminateDlcNode();
    }

    private void illuminatePrimaryNode() {
        IlluminateDetails details = IlluminateDetails.from(inNode.get(cPrimary));
        String filePath = details.buildTransmogFilePath();

        JsonNode transmogrifiedNode = parseIn(cArkAssetsFilePath, filePath);
        primaryGameData = PrimaryIlluminateGameData.with(transmogrifiedNode);
    }

    private void illuminateDlcNode() {
        JsonNode dlcArrayNode = inNode.get(cDlc);
        for (JsonNode dlcNode : dlcArrayNode) {
            DlcIlluminateDetails details = mapper.convertValue(dlcNode, DlcIlluminateDetails.class);
            String filePath = details.buildTransmogFilePath();

            JsonNode transmogrifiedNode = parseIn(cArkAssetsFilePath, filePath);
            DlcIlluminateGameData dlcGameData = DlcIlluminateGameData.with(transmogrifiedNode, primaryGameData);
            dlcGameDataList.add(dlcGameData);
        }
    }

    public void export() {
        writeIlluminatedGameData();
        writeIllumination();
    }

    private void writeIlluminatedGameData() {
        writePrimaryGameData();
        writeDlcGameData();
    }

    private void writePrimaryGameData() {
        writeGameDataToFile(primaryGameData);
    }

    private void writeDlcGameData() {
        for (DlcIlluminateGameData dlcGameData : dlcGameDataList) {
            writeGameDataToFile(dlcGameData);
        }
    }

    private void writeGameDataToFile(IlluminateGameData gameData) {
        JsonNode resolvedNode = gameData.resolveToJson();

        writeJsonToFile(gameData.getFilePathForResources(), resolvedNode.get(cResources));
        writeJsonToFile(gameData.getFilePathForDirectory(), resolvedNode.get(cDirectory));
    }

    private void writeIllumination() {
        ObjectNode outNode = mapper.createObjectNode();
        outNode.set(cPrimary, mapper.valueToTree(primaryGameData.getDetailsObject()));

        ArrayNode outDlcArrayNode = mapper.createArrayNode();
        for (DlcIlluminateGameData dlcGameData : dlcGameDataList) {
            outDlcArrayNode.add(mapper.valueToTree(dlcGameData.getDetailsObject()));
        }

        writeJsonToFile(cIlluminationFileName, outNode);
    }

    private void writeJsonToFile(String fileName, JsonNode outNode) {
        writeOut(cArkAssetsFilePath, fileName, outNode);
    }
}