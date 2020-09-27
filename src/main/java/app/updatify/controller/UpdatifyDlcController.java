package app.updatify.controller;

import com.fasterxml.jackson.databind.JsonNode;

public class UpdatifyDlcController extends UpdatifyController {
    private final UpdatifyController primaryController;

    public UpdatifyDlcController(JsonNode transmogrificationNode, JsonNode illuminationNode, UpdatifyController primaryController) {
        super(transmogrificationNode, illuminationNode);
        this.primaryController = primaryController;
    }
}