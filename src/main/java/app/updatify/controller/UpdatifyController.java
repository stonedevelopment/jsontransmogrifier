package app.updatify.controller;

import app.transmogrify.model.game_data.DlcTransmogGameData;
import app.updatify.game_data.UpdatifyGameData;
import com.fasterxml.jackson.databind.JsonNode;

import static util.Constants.cArkAssetsFilePath;
import static util.JSONUtil.writeOut;

public class UpdatifyController {
    private final UpdatifyGameData gameData;

    public UpdatifyController(JsonNode transmogrificationNode, JsonNode illuminationNode) {
        this.gameData = new UpdatifyGameData(transmogrificationNode, illuminationNode);
    }

    public void start() {
        gameData.mapGameDataFromJson();
    }

    public void export() {
        writeGameDataToFile();
    }

    private void writeGameDataToFile() {
        String filePath = gameData.getDetails().getUpdatifiedFilePath();
        writeJsonToFile(filePath, gameData.resolveToJson());
    }

    private void writeJsonToFile(String filePath, JsonNode outNode) {
        writeOut(cArkAssetsFilePath, filePath, outNode);
    }
}