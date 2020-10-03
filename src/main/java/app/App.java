package app;

import app.illuminate.IlluminateApp;
import app.transmogrify.TransmogrifyApp;
import app.updatify.UpdatifyApp;
import com.fasterxml.jackson.databind.JsonNode;
import util.JSONUtil;

import static util.Constants.*;

public class App {
    /**
     * Data conversion steps: transmogrify -> illuminate -> updatify
     * Comment out a step to skip
     */
    public static void main(String[] args) {
        //  convert old data_editable file to static UUIDs
//        transmogrify();

        //  convert transmogrified static UUIDs to separate readable/editable files
//        illuminate();

        //  merge illuminated files into database-ready UUIDs pre-generated from Transmog
        //  nullified illuminated ids will be created on the fly
        updatify();
    }

    private static void transmogrify() {
        JsonNode inNode = JSONUtil.parseIn(cArkAssetsFilePath, cArkDataEditableFileName);
        TransmogrifyApp transmogrifyApp = new TransmogrifyApp(inNode);
        transmogrifyApp.start();
        transmogrifyApp.export();
    }

    private static void illuminate() {
        JsonNode inNode = JSONUtil.parseIn(cArkAssetsFilePath, cTransmogrificationFileName);
        IlluminateApp illuminateApp = new IlluminateApp(inNode);
        illuminateApp.start();
        illuminateApp.export();
    }

    private static void updatify() {
        //  parse Transmogrification file
        JsonNode transmogrificationNode = JSONUtil.parseIn(cArkAssetsFilePath, cTransmogrificationFileName);
        //  parse Illumination file
        JsonNode illuminationNode = JSONUtil.parseIn(cArkAssetsFilePath, cIlluminationFileName);
        //  instantiate app with both files
        UpdatifyApp updatifyApp = new UpdatifyApp(transmogrificationNode, illuminationNode);
        //  start app
        updatifyApp.start();
    }
}