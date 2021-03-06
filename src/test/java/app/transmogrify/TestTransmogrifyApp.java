package app.transmogrify;

import app.transmogrify.model.game_data.PrimaryTransmogGameData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import util.JSONUtil;

import java.io.IOException;

import static util.Constants.cArkAssetsFilePath;
import static util.Constants.cArkDataEditableFileName;

public class TestTransmogrifyApp {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static JsonNode inNode;
    private static PrimaryTransmogGameData primaryGameData;

    static void init() throws IOException {
        inNode = JSONUtil.parseIn(cArkAssetsFilePath, cArkDataEditableFileName);
    }

    static void initEach() {

    }
}
