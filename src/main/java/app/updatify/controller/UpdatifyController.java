package app.updatify.controller;

import app.updatify.game_data.IlluminateUpdatifyGameData;
import app.updatify.game_data.TransmogUpdatifyGameData;
import com.fasterxml.jackson.databind.JsonNode;

public class UpdatifyController {
    private final TransmogUpdatifyGameData transmogGameData;
    private final IlluminateUpdatifyGameData illuminateGameData;

    public UpdatifyController(JsonNode transmogrifiedNode, JsonNode illuminatedNode) {
        this.transmogGameData = new TransmogUpdatifyGameData(transmogrifiedNode);
        this.illuminateGameData = new IlluminateUpdatifyGameData(illuminatedNode);
    }

    public void start() {
        transmogGameData.mapGameDataFromJson();
        illuminateGameData.mapGameDataFromJson();
    }
}