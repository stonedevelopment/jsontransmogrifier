package app;

import app.transmogrify.TransmogrifyApp;
import com.fasterxml.jackson.databind.JsonNode;
import util.JSONUtil;

import java.io.IOException;

import static util.Constants.cArkAssetsFilePath;
import static util.Constants.cArkDataEditableFileName;
import static util.JSONUtil.writeOut;

public class App {
    private static TransmogrifyApp transmogrifyApp;

    public static void main(String[] args) {
        transmogrify();
    }

    private static void transmogrify() {
        try {
            JsonNode inNode = JSONUtil.parseIn(cArkAssetsFilePath, cArkDataEditableFileName);
            transmogrifyApp = new TransmogrifyApp(inNode);
            transmogrifyApp.transmogrify();
            transmogrifyApp.export();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}