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
        JsonNode resolvedNode = gameData.resolveToJson();



        JsonNode resourcesNode = resolvedNode.get(cResources);
        //  generate file path for illuminated resources
        String illuminatedResourcesFilePath = gameData.generateIlluminatedResourcesFilePath();
        //  write illuminated resources to json
        writeJsonToFile(illuminatedResourcesFilePath, resourcesNode);
        //  add illuminated resources json file name for illumination write out
        gameData.addIlluminatedFile(illuminatedResourcesFilePath);

        /**
         * Consider removing uuids from all illuminated files
         * Updatify should compare Transmog with Illuminated, if exists use UUID; else create one
         *
         * Consider moving engrams to own file
         *  Include composition, using only the composites
         *
         * Consider creating directory file in place of separate files
         * Use names only for stations, folders and engrams
         */

        ArrayNode resolvedDirectory = mapper.createArrayNode();

        //  iterate through resolved directory, separate each directory parent (crafting stations)
        JsonNode directoryNode = resolvedNode.get(cDirectory);
        for (JsonNode illuminatedStation : directoryNode) {
            //  generate file path for illuminated node
            String illuminatedStationFilePath = gameData.generateIlluminatedStationFilePath(illuminatedStation);
            //  write illuminated node's data to json
            writeJsonToFile(illuminatedStationFilePath, illuminatedStation);
            //  add illuminate node's json file name for illumination write out
            gameData.addIlluminatedFile(illuminatedStationFilePath);
        }
    }

    private void writeIllumination() {
        ObjectNode outNode = mapper.createObjectNode();
        outNode.set(cPrimary, mapper.valueToTree(primaryGameData.getDetailsObject()));

        ArrayNode outDlcArrayNode = mapper.createArrayNode();
        for (DlcIlluminateGameData dlcGameData : dlcGameDataList) {
            outDlcArrayNode.add(mapper.valueToTree(dlcGameData.getDetailsObject()));
        }

        outNode.set(cDlc, outDlcArrayNode);

        writeJsonToFile(cIlluminationFileName, outNode);
    }

    private void writeJsonToFile(String fileName, JsonNode outNode) {
        writeOut(cArkAssetsFilePath, fileName, outNode);
    }
}