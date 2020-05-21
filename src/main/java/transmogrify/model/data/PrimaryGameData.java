package transmogrify.model.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import transmogrify.model.details.Details;
import transmogrify.model.details.PrimaryDetails;
import transmogrify.model.json.*;
import transmogrify.model.primary.*;

import java.util.Date;
import java.util.UUID;

public class PrimaryGameData extends GameData {
    PrimaryDetails details;

    public PrimaryGameData(JsonDlc jsonDlc, JsonNode inObject, ObjectMapper mapper) {
        super(jsonDlc, inObject, mapper);
        this.details = createDetailsObject(jsonDlc);
    }

    @Override
    public boolean isValidDlcIdForDirectory(long dlcId) {
        return getDlcId() == dlcId;
    }

    @Override
    public String buildFilePathForJSONExport() {
        return "src/assets/Primary/Primary.json";
    }

    @Override
    public PrimaryDetails createDetailsObject(JsonDlc jsonDlc) {
        String uuid = !cDebug ? UUID.randomUUID().toString() : jsonDlc.name;
        String name = jsonDlc.name;
        String description = jsonDlc.description;
        String filePath = jsonDlc.filePath;
        String logoFile = "logo.webp";
        String folderFile = "folder.webp";
        String backFolderFile = "backFolder.webp";
        return new PrimaryDetails(uuid, name, description, filePath, logoFile, folderFile, backFolderFile);
    }

    @Override
    public PrimaryDetails getDetailsObject() {
        return details;
    }

    @Override
    public Folder buildFolder(JsonCategory jsonCategory) {
        String uuid = !cDebug ? generateUUID() : jsonCategory.name;
        String name = jsonCategory.name;
        String gameId = details.getUuid();
        return new Folder(uuid, name, gameId);
    }

    @Override
    public Resource buildResource(JsonResource jsonResource) {
        String uuid = !cDebug ? generateUUID() : jsonResource.name;
        String name = jsonResource.name;
        String description = "";
        String imageFile = jsonResource.image_file;
        Date lastUpdated = new Date();
        String gameId = details.getUuid();

        return new Resource(uuid, name, description, imageFile, lastUpdated, gameId);
    }

    @Override
    public Engram buildEngram(JsonEngram jsonEngram) {
        String uuid = !cDebug ? generateUUID() : jsonEngram.name;
        String name = jsonEngram.name;
        String description = jsonEngram.description;
        String imageFile = jsonEngram.image_file;
        int level = jsonEngram.level;
        int yield = jsonEngram.yield;
        int points = jsonEngram.points;
        int xp = jsonEngram.xp;
        int craftingTime = 0;
        Date lastUpdated = new Date();
        String gameId = details.getUuid();

        return new Engram(uuid, name, description, imageFile, level, yield, points, xp, craftingTime, lastUpdated,
                gameId);
    }

    @Override
    public Station buildStation(JsonStation jsonStation) {
        String uuid = !cDebug ? generateUUID() : jsonStation.name;
        String name = jsonStation.name;
        String imageFile = jsonStation.image_file;
        String engramId = getEngramUUID(jsonStation.name);
        Date lastUpdated = new Date();
        String gameId = details.getUuid();
        return new Station(uuid, name, imageFile, engramId, lastUpdated, gameId);
    }

    @Override
    public Composition buildComposition(JsonEngram jsonEngram) {
        String uuid = !cDebug ? generateUUID() : jsonEngram.name;
        String engramId = getEngramUUID(jsonEngram.name);
        Date lastUpdated = new Date();
        String gameId = details.getUuid();

        mapCompositesFromJson(uuid, jsonEngram);

        return new Composition(uuid, engramId, lastUpdated, gameId);
    }

    @Override
    public JsonNode generateJson() {
        //  create dlc json object
        ObjectNode gameDataObject = mapper.createObjectNode();

        gameDataObject.set("details", mapper.valueToTree(details));

        //  add resources, without complex resources
        gameDataObject.set("resources", mapper.valueToTree(resourceMap.values()));

        //  add stations
        gameDataObject.set("stations", mapper.valueToTree(stationMap.values()));

        //  add folders
        gameDataObject.set("folders", mapper.valueToTree(folderMap.values()));

        //  add engrams
        gameDataObject.set("engrams", mapper.valueToTree(engramMap.values()));

        //  add composition
        gameDataObject.set("composition", mapper.valueToTree(compositionMap.values()));

        //  add composites
        gameDataObject.set("composites", mapper.valueToTree(compositeMap.values()));

        //  add substitutions
//        gameDataObject.set("substitutions", createSubsSection());

        //  add directory, traverse through tree, fill with uuids
        gameDataObject.set("directory", createDirectorySection());

        return gameDataObject;
    }
}
