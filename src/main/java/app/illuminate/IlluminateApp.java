package app.illuminate;

import app.illuminate.controller.IlluminateGameData;
import app.illuminate.model.controller.DlcIlluminateGameData;
import app.illuminate.model.controller.PrimaryIlluminateGameData;
import app.illuminate.model.details.DlcIlluminateDetails;
import app.illuminate.model.details.IlluminateDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static util.Constants.*;
import static util.JSONUtil.parseIn;
import static util.JSONUtil.writeOut;

/**
 * Converts transmogrified json files into, separate, readable files for easy editing
 * <p>
 * Illuminate!
 */
public class IlluminateApp {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final List<DlcIlluminateGameData> dlcGameDataList = new ArrayList<>();
    private static PrimaryIlluminateGameData primaryGameData;
    private final JsonNode inNode;

    public IlluminateApp(JsonNode inNode) {
        this.inNode = inNode;
    }

    public void illuminate() {
        illuminatePrimaryNode();
        illuminateDlcNode();
    }

    public void export() {
        writeIlluminatedGameData();
        writeIllumination();
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
        JsonNode resolvedNodes = gameData.resolveToJson();
        Iterator<JsonNode> elements = resolvedNodes.elements();
        Iterator<String> fieldNames = resolvedNodes.fieldNames();

        //  iterate elements to build output node
        while (elements.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode resolvedNode = elements.next();
            addResolvedNodeToGameData(gameData, resolvedNode, fieldName);
        }
    }

    /**
     * Generates file path, Writes illuminated/resolved node to json file, adds path to game data controller
     *
     * @param gameData        Controller instance
     * @param illuminatedNode Illuminated Node of type value
     * @param type            String constant holding name of illuminated node
     */
    private void addResolvedNodeToGameData(IlluminateGameData gameData, JsonNode illuminatedNode, String type) {
        //  generate file path for illuminated node
        String illuminatedFilePath = gameData.generateIlluminatedFilePath(type);
        //  write illuminated node to json
        writeNodeToFile(illuminatedFilePath, illuminatedNode);
        //  add illuminated json file name for illumination write out
        gameData.addIlluminatedFile(illuminatedFilePath);
    }

    private void writeIllumination() {
        ObjectNode outNode = mapper.createObjectNode();
        outNode.set(cPrimary, generateIlluminationDetails(primaryGameData));

        ArrayNode outDlcArrayNode = mapper.createArrayNode();
        for (DlcIlluminateGameData dlcGameData : dlcGameDataList) {
            outDlcArrayNode.add(generateIlluminationDetails(dlcGameData));
        }

        outNode.set(cDlc, outDlcArrayNode);

        writeNodeToFile(cIlluminationFileName, outNode);
    }

    private JsonNode generateIlluminationDetails(IlluminateGameData gameData) {
        ObjectNode outNode = mapper.createObjectNode();
        outNode.put(cName, gameData.getDetailsObject().getName());
        outNode.set(cIlluminatedFiles, gameData.getDetailsObject().getIlluminatedFiles());
        return outNode;
    }

    private void writeNodeToFile(String fileName, JsonNode outNode) {
        writeOut(cArkAssetsFilePath, fileName, outNode);
    }
}