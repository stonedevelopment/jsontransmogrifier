package app.illuminate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.details.PrimaryDetails;
import model.Resource;

import java.io.IOException;
import java.util.Map;

import static util.Constants.*;
import static util.JSONUtil.parseIn;

/**
 * Converts transmogrified json files into readable files for easy updating
 * <p>
 * Illuminate!
 */
public class IlluminateApp {
    private static final ObjectMapper mapper = new ObjectMapper();

    static PrimaryDetails primaryDetails;

    public static void main(String[] args) {
        //  load transmogrification.json
        //  load parent object
        //  convert transmogrified.json file into separate readable json files
        try {
            JsonNode inNode = parseIn(cTransmogDataFileName);

            //  illuminatePrimary(inNode.get("primary"));
            primaryDetails = mapper.treeToValue(inNode.get(cPrimary), PrimaryDetails.class);
            String filePath = primaryDetails.getTransmogFile();

            JsonNode transmogrifiedNode = parseIn(filePath);
            JsonNode detailsNode = transmogrifiedNode.get(cDetails);
            Map<String, Resource> resourceMap = mapResources(transmogrifiedNode.get(cResources));
            JsonNode convertedStations = convertStations(transmogrifiedNode.get(cStations));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Map<String, Resource> mapResources(JsonNode inNode) {
        return inNode;
    }

    static JsonNode convertStations(JsonNode inNode) {
        return inNode;
    }
}
