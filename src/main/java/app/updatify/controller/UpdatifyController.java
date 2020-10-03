package app.updatify.controller;

import app.updatify.game_data.UpdatifyGameData;
import com.fasterxml.jackson.databind.JsonNode;

import static util.Constants.cArkAssetsFilePath;
import static util.JSONUtil.writeOut;

public class UpdatifyController {
    private final UpdatifyGameData gameData;

    protected UpdatifyController(UpdatifyGameData gameData) {
        this.gameData = gameData;
    }

    public static UpdatifyController from(JsonNode tNode, JsonNode iNode) {
        return new UpdatifyController(new UpdatifyGameData(tNode, iNode));
    }

    public void start() {
        getGameData().mapGameDataFromJson();
    }

    public void export() {
        writeGameDataToFile();
    }

    public UpdatifyGameData getGameData() {
        return gameData;
    }

    private void writeGameDataToFile() {
        String filePath = gameData.getDetails().getUpdatifiedFilePath();
        writeJsonToFile(filePath, gameData.resolveToJson());
    }

    private void writeJsonToFile(String filePath, JsonNode outNode) {
        writeOut(cArkAssetsFilePath, filePath, outNode);
    }
}