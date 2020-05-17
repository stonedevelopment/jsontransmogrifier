package transmogrify.model.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import transmogrify.model.*;
import transmogrify.model.json.JsonTotalConversion;
import transmogrify.model.json.JsonTotalConversionResource;
import transmogrify.model.type.DLCDetails;
import transmogrify.model.type.Details;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DLCGameData extends GameData {
    PrimaryGameData primaryGameData;
    TotalConversion totalConversion = new TotalConversion();

    public DLCGameData(long dlcId, JsonNode inObject, ObjectMapper mapper, PrimaryGameData primaryGameData) {
        super(dlcId, inObject, mapper);
        this.primaryGameData = primaryGameData;
        this.details = createDetailsObject(dlcId);
    }

    private boolean isTotalConversion(long dlcId) {
        return dlcId == 2L;
    }

    @Override
    public void removeNullifiedResource(String name) {
        super.removeNullifiedResource(name);
        primaryGameData.removeNullifiedResource(name);
    }

    @Override
    public void mapGameData() {
        mapTotalConversionFromJson();
        super.mapGameData();
    }

    @Override
    public JsonNode generateJson() {
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

        //  add remove section
        gameDataObject.set("remove", createRemoveSection());

        //  add replace section
        gameDataObject.set("replace", createReplaceSection());

        return gameDataObject;
    }

    @Override
    public void mapSubstitutesFromResourceMap() {
        super.mapSubstitutesFromResourceMap();
        mapSubstitutesFromPrimaryResourceMap();
    }

    private void mapSubstitutesFromPrimaryResourceMap() {
        for (Resource resource : primaryGameData.resourceMap.values()) {
            List<String> subsByName = generateSubsForResource(resource.getName());
            if (subsByName.size() > 1) {
                List<String> subsById = convertResourceNamesToId(subsByName);
                if (subsById.size() > 1) {
                    substitutions.put(resource.getName(), subsById);
                }
            }
        }
    }

    @Override
    public boolean isValidDlcIdForDirectory(long dlcId) {
        return this.dlcId == dlcId || primaryGameData.isValidDlcIdForDirectory(dlcId);
    }

    @Override
    public String buildFilePath(String dlcName) {
        return String.format("DLC/%s/", dlcName);
    }

    @Override
    public String buildFilePathForJSONExport() {
        return String.format("src/assets/DLC/%s.json", details.getName());
    }

    @Override
    public Engram getEngramByName(String name) {
        Engram engram = super.getEngramByName(name);
        return engram != null ? engram : primaryGameData.getEngramByName(name);
    }

    @Override
    public Folder getFolderByCategoryId(long id) {
        Folder folder = super.getFolderByCategoryId(id);
        return folder != null ? folder : primaryGameData.getFolderByCategoryId(id);
    }

    @Override
    public Details createDetailsObject(long dlcId) {
        String uuid = UUID.randomUUID().toString();
        String name = getNameByDlcId(dlcId);
        String description = getDescriptionByDlcId(dlcId);
        Boolean totalConversion = isTotalConversion(dlcId);
        String filePath = buildFilePath(name);
        String logoFile = "logo.webp";
        String folderFile = "folder.webp";
        String backFolderFile = "backFolder.webp";
        String gameId = primaryGameData.details.getUuid();
        return new DLCDetails(uuid, name, description, totalConversion, filePath, logoFile, folderFile, backFolderFile, gameId);
    }

    @Override
    public Resource getResourceByName(String name) {
        Resource resource = super.getResourceByName(name);
        return resource != null ? resource : primaryGameData.getResourceByName(name);
    }

    @Override
    public Station getStationByName(String name) {
        Station station = super.getStationByName(name);
        return station != null ? station : primaryGameData.getStationByName(name);
    }

    private JsonNode createRemoveSection() {
        ObjectNode outNode = mapper.createObjectNode();

        //  add uuids of resources to remove
        ArrayNode resourceArray = mapper.createArrayNode();
        outNode.set("resources", resourceArray);

        //  add uuids of stations to remove
        ArrayNode stationArray = mapper.createArrayNode();
        for (String stationName : totalConversion.stationsToRemove) {
            Station stationToRemove = getStationByName(stationName);
            stationArray.add(stationToRemove.getUuid());
        }
        outNode.set("stations", stationArray);

        //  add uuids of folders to remove
        ArrayNode folderArray = mapper.createArrayNode();
        for (long categoryId : totalConversion.foldersToRemove) {
            Folder folderToRemove = getFolderByCategoryId(categoryId);
            folderArray.add(folderToRemove.getUuid());
        }
        outNode.set("folders", folderArray);

        //  add uuids of engrams to remove
        ArrayNode engramArray = mapper.createArrayNode();
        for (String engramName : totalConversion.engramsToRemove) {
            Engram engramToRemove = getEngramByName(engramName);
            engramArray.add(engramToRemove.getUuid());
        }
        outNode.set("engrams", engramArray);

        return outNode;
    }

    private JsonNode createReplaceSection() {
        ObjectNode outNode = mapper.createObjectNode();

        //  add uuids of resources to replace
        ArrayNode resourceArray = mapper.createArrayNode();
        for (Map.Entry<String, String> resourceEntry : totalConversion.resourcesToReplace.entrySet()) {
            String from = resourceEntry.getKey();
            String to = resourceEntry.getValue();

            ObjectNode resourceNode = mapper.createObjectNode();
            resourceNode.put(from, to);

            resourceArray.add(resourceNode);
        }
        outNode.set("resources", resourceArray);

        //  add uuids of stations to replace
        ArrayNode stationArray = mapper.createArrayNode();
        outNode.set("stations", stationArray);

        //  add uuids of folders to replace
        ArrayNode folderArray = mapper.createArrayNode();
        outNode.set("folders", folderArray);

        //  add uuids of engrams to replace
        ArrayNode engramArray = mapper.createArrayNode();
        outNode.set("engrams", engramArray);

        //  add uuids of engrams to replace
        ArrayNode compositionArray = mapper.createArrayNode();
        outNode.set("composition", compositionArray);

        return outNode;
    }

    private void mapTotalConversionFromJson() {
        JsonNode totalConversionArray = inObject.get("total_conversion");
        for (JsonNode totalConversionObject : totalConversionArray) {
            JsonTotalConversion jsonTotalConversion =
                    mapper.convertValue(totalConversionObject, JsonTotalConversion.class);

            if (isValidDlcId(jsonTotalConversion.dlc_id)) {
                //  add list of resources to replace
                for (JsonTotalConversionResource resourceConversion : jsonTotalConversion.resource) {
                    totalConversion.resourcesToReplace.put(resourceConversion.from, resourceConversion.to);
                }

                //  add lists of removals
                totalConversion.stationsToRemove.addAll(jsonTotalConversion.station);
                totalConversion.foldersToRemove.addAll(jsonTotalConversion.category);
                totalConversion.engramsToRemove.addAll(jsonTotalConversion.engram);
            }
        }
    }
}