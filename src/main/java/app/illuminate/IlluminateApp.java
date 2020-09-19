package app.illuminate;

import app.illuminate.controller.IlluminateGameData;
import app.illuminate.model.controller.DlcIlluminateGameData;
import app.illuminate.model.controller.PrimaryIlluminateGameData;
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
    private final List<DlcIlluminateGameData> dlcGameDataList = new ArrayList<>();
    private final JsonNode inNode;
    private PrimaryIlluminateGameData primaryGameData;

    public IlluminateApp(JsonNode inNode) {
        this.inNode = inNode;
    }

    public void start() {
        illuminatePrimaryNode();
        illuminateDlcNode();
    }

    public void export() {
        writeIlluminatedGameData();
        writeIllumination();
    }

    public PrimaryIlluminateGameData getPrimaryGameData() {
        return primaryGameData;
    }

    public List<DlcIlluminateGameData> getDlcGameDataList() {
        return dlcGameDataList;
    }

    private void illuminatePrimaryNode() {
        // TODO: 9/12/2020 This methods needs to pull in from transmogrification.json file, but it is no longer a Details object
        //  consider creating top-level objects for each conversion type (Transmog, Illuminate)
        //  all top-level json files point to file paths and names, they're not meant for updating game details
        JsonNode primaryNode = inNode.get(cPrimary);
        String filePath = primaryNode.get(cFilePath).asText();
        String fileName = primaryNode.get(cTransmogFile).asText();
        String fullPath = filePath.concat(fileName);

        JsonNode transmogrifiedNode = parseIn(cArkAssetsFilePath, fullPath);
        primaryGameData = PrimaryIlluminateGameData.with(transmogrifiedNode);
    }

    private void illuminateDlcNode() {
        JsonNode dlcArrayNode = inNode.get(cDlc);
        for (JsonNode dlcNode : dlcArrayNode) {
            String filePath = dlcNode.get(cFilePath).asText();
            String fileName = dlcNode.get(cTransmogFile).asText();
            String fullPath = filePath.concat(fileName);

            JsonNode transmogrifiedNode = parseIn(cArkAssetsFilePath, fullPath);
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
            JsonNode prefixNode = mapper.createObjectNode().set(fieldName, resolvedNode);
            addResolvedNodeToGameData(gameData, prefixNode, fieldName);
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