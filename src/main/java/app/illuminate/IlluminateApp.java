package app.illuminate;

import app.illuminate.model.PrimaryIlluminateGameData;
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
            illuminatePrimary(transmogNode);
            illuminateDlcs(transmogNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void illuminatePrimary(JsonNode transmogNode) throws IOException {
        TransmogDetails details = mapper.treeToValue(transmogNode.get(cPrimary), TransmogDetails.class);
        String filePath = details.getTransmogFile();

        JsonNode transmogrifiedNode = parseIn(filePath);
        primaryGameData = new PrimaryIlluminateGameData(transmogrifiedNode);
    }

    private static void illuminateDlcs(JsonNode transmogNode) {
        JsonNode dlcNode = transmogNode.get(cDlc);

    }
}
