package app.updatify.controller;

import app.updatify.game_data.UpdatifyGameData;
import app.updatify.model.UpdatifyDetails;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Date;

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

    public UpdatifyDetails getDetails() {
        return gameData.getDetails();
    }

    public String getUuid() {
        return getDetails().getUuid();
    }

    public String getName() {
        return getDetails().getName();
    }

    public String getUpdatifiedFilePath() {
        return getDetails().getUpdatifiedFilePath();
    }

    /**
     * Returns a Date value determined by if the Updatify process found new data; be it updates, additions, or
     * deletions. If the process found new data, return a new Date; if not, return the previously saved Date.
     *
     * @return Date value of the last time data was updated.
     */
    public Date getLastUpdated() {
        if (gameData.hasUpdate()) {
            return new Date();
        } else {
            return getDetails().getLastUpdated();
        }
    }

    private void writeGameDataToFile() {
        String filePath = gameData.getDetails().getUpdatifiedFilePath();
        writeJsonToFile(filePath, gameData.resolveToJson());
    }

    private void writeJsonToFile(String filePath, JsonNode outNode) {
        writeOut(cArkAssetsFilePath, filePath, outNode);
    }
}