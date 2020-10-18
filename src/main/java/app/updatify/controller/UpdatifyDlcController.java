package app.updatify.controller;

import app.updatify.game_data.UpdatifyDlcGameData;
import app.updatify.model.UpdatifyDlcDetails;
import com.fasterxml.jackson.databind.JsonNode;

public class UpdatifyDlcController extends UpdatifyController {
    public UpdatifyDlcController(UpdatifyDlcGameData gameData) {
        super(gameData);
    }

    public static UpdatifyDlcController from(JsonNode tNode, JsonNode iNode, UpdatifyController primaryController) {
        return new UpdatifyDlcController(new UpdatifyDlcGameData(tNode, iNode, primaryController.getGameData()));
    }

    @Override
    public UpdatifyDlcGameData getGameData() {
        return (UpdatifyDlcGameData) super.getGameData();
    }

    @Override
    public UpdatifyDlcDetails getDetails() {
        return (UpdatifyDlcDetails) super.getDetails();
    }
}