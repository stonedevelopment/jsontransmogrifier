package illuminate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.data.PrimaryGameData;
import model.details.PrimaryDetails;
import util.JSONUtil;

import java.io.IOException;

import static util.JSONUtil.parseIn;

/**
 * Converts UUID json files into readable files for easy updating
 * <p>
 * Illuminate!
 */
public class IlluminateApp {
    private static final String inFilename = "transmogrification.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        //  load transmogrification.json
        //  load parent object
        //  convert transmogrified.json file into separate readable json files
        try {
            JsonNode inNode = parseIn(inFilename);

            PrimaryDetails details = mapper.treeToValue(inNode.get("primary"), PrimaryDetails.class);
            String filePath = details.getFilePath() + "/transmogrified.json";

            JsonNode transmogrifiedNode = parseIn(filePath);
            JsonNode resourcesNode = convertResources(transmogrifiedNode.get("resources"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JsonNode generateDetailsNode(JsonNode jsonNode) {
        return jsonNode;
    }

    static JsonNode convertResources(JsonNode inNode) {
        return inNode;
    }
}
