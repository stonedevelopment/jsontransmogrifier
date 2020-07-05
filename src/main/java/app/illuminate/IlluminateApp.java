package app.illuminate;

import app.illuminate.model.DlcIlluminateGameData;
import app.illuminate.model.PrimaryIlluminateGameData;
import app.transmogrify.model.details.DlcTransmogDetails;
import app.transmogrify.model.details.TransmogDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static util.Constants.*;
import static util.JSONUtil.parseIn;

/**
 * Converts transmogrified json files into, separate, readable files for easy editing
 * <p>
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
            JsonNode transmogNode = parseIn(cTransmogrificationFileName);
            illuminatePrimaryNode(transmogNode);
            illuminateDlcNode(transmogNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void illuminatePrimaryNode(JsonNode transmogNode) throws IOException {
        TransmogDetails details = mapper.treeToValue(transmogNode.get(cPrimary), TransmogDetails.class);
        String filePath = details.getTransmogFile();

        JsonNode transmogrifiedNode = parseIn(filePath);
        primaryGameData = new PrimaryIlluminateGameData(transmogrifiedNode);
        //  writeGameDataToFile()
    }

    private static void illuminateDlcNode(JsonNode transmogNode) throws IOException {
        JsonNode dlcArrayNode = transmogNode.get(cDlc);
        for (JsonNode dlcNode : dlcArrayNode) {
            DlcTransmogDetails details = mapper.treeToValue(dlcNode, DlcTransmogDetails.class);
            String filePath = details.getTransmogFile();

            JsonNode transmogrifiedNode = parseIn(filePath);
            DlcIlluminateGameData gameData = new DlcIlluminateGameData(transmogrifiedNode);
            //  writeGameDataToFile()
        }
    }
}
