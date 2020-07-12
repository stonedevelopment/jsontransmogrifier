package app;

import app.transmogrify.TransmogrifyApp;
import com.fasterxml.jackson.databind.JsonNode;
import util.JSONUtil;

import java.io.IOException;

import static util.Constants.cArkAssetsFilePath;
import static util.Constants.cArkDataEditableFileName;

public class App {
    private static TransmogrifyApp transmogrifyApp;

    public static void main(String[] args) {
        try {
            setupTransmogrifyApp();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setupTransmogrifyApp() throws IOException {
        JsonNode inNode = JSONUtil.parseIn(cArkAssetsFilePath, cArkDataEditableFileName);
        transmogrifyApp = new TransmogrifyApp(inNode);
    }
}