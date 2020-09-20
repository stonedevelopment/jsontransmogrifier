package app.updatify.controller;

import app.updatify.game_data.UpdatifyGameData;
import com.fasterxml.jackson.databind.JsonNode;

public class UpdatifyController {
    private final UpdatifyGameData gameData;

    public UpdatifyController(JsonNode transmogrificationNode, JsonNode illuminationNode) {
        this.gameData = new UpdatifyGameData(transmogrificationNode, illuminationNode);
    }

    public void start() {
        gameData.mapGameDataFromJson();
    }
}