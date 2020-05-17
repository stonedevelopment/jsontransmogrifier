package transmogrify.model.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import transmogrify.model.type.Details;
import transmogrify.model.type.PrimaryDetails;

import java.util.UUID;

public class PrimaryGameData extends GameData {
    public PrimaryGameData(JsonNode inObject, ObjectMapper mapper) {
        super(1, inObject, mapper);
        this.details = createDetailsObject(1);
    }

    @Override
    public boolean isValidDlcIdForDirectory(long dlcId) {
        return this.dlcId == dlcId;
    }

    @Override
    String getNameByDlcId(long dlcId) {
        return "ARK:Survival Evolved";
    }

    @Override
    public String buildFilePath(String dlcName) {
        return "Primary/";
    }

    @Override
    public String buildFilePathForJSONExport() {
        return "src/assets/Primary/Primary.json";
    }

    @Override
    public Details createDetailsObject(long dlcId) {
        String uuid = UUID.randomUUID().toString();
        String name = getNameByDlcId(dlcId);
        String description = getDescriptionByDlcId(dlcId);
        String filePath = buildFilePath(name);
        String logoFile = "logo.webp";
        String folderFile = "folder.webp";
        String backFolderFile = "backFolder.webp";
        return new PrimaryDetails(uuid, name, description, filePath, logoFile, folderFile, backFolderFile);
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

        //  add substitutions
//        gameDataObject.set("substitutions", createSubsSection());

        //  add directory, traverse through tree, fill with uuids
        gameDataObject.set("directory", createDirectorySection());

        return gameDataObject;
    }
}
