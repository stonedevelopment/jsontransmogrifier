package app.illuminate;

import app.transmogrify.model.details.TransmogDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static util.Constants.*;
import static util.JSONUtil.parseIn;

/**
 * Converts transmogrified json files into readable files for easy updating
 * <p>
 * Illuminate!
 */
public class IlluminateApp {
    private static final ObjectMapper mapper = new ObjectMapper();

    static TransmogDetails primaryDetails;

    public static void main(String[] args) {
        //  load transmogrification.json
        //  load parent object
        //  convert transmogrified.json file into separate readable json files
        try {
            JsonNode inNode = parseIn(cTransmogrificationFileName);

            //  illuminatePrimary(inNode.get("primary"));
            primaryDetails = mapper.treeToValue(inNode.get(cPrimary), TransmogDetails.class);
            String filePath = primaryDetails.getTransmogFile();

            JsonNode transmogrifiedNode = parseIn(filePath);
            JsonNode detailsNode = transmogrifiedNode.get(cDetails);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
