package transmogrify.model.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import transmogrify.model.details.Details;
import transmogrify.model.json.*;
import transmogrify.model.primary.*;

import java.util.*;

public abstract class GameData {
    public static final boolean cDebug = false; // TODO: 4/26/2020 Set to FALSE when finalizing
    public static final String cUuid = "uuid";
    public static final String cName = "name";
    public static final String cImageFile = "imageFile";
    public static final String cStationId = "stationId";

    public List<String> nullifiedResources = new ArrayList<>();

    public JsonDlc jsonDlc;
    JsonNode inObject;
    ObjectMapper mapper;

    Map<String, Resource> resourceMap = new TreeMap<>();
    Map<Long, Folder> folderMap = new TreeMap<>();
    Map<String, Engram> engramMap = new TreeMap<>();
    Map<String, Station> stationMap = new TreeMap<>();
    Map<String, Composition> compositionMap = new TreeMap<>();
    Map<String, List<Composite>> compositeMap = new TreeMap<>();
    Map<String, List<String>> substitutions = new TreeMap<>();
    Map<String, DirectoryItem> directory = new TreeMap<>();

    public GameData(JsonDlc jsonDlc, JsonNode inObject, ObjectMapper mapper) {
        this.jsonDlc = jsonDlc;
        this.inObject = inObject;
        this.mapper = mapper;
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public abstract Details createDetailsObject(JsonDlc jsonDlc);

    public abstract Details getDetailsObject();

    public long getDlcId() {
        return jsonDlc._id;
    }

    public String getResourceUUID(String name) {
        Resource resource = getResourceByName(name);

        return resource != null ? resource.getUuid() : null;
    }

    public String getResourceImageFile(String name) {
        Resource resource = getResourceByName(name);

        return resource != null ? resource.getImageFile() : null;
    }

    public String getEngramUUID(String name) {
        Engram engram = getEngramByName(name);

        return engram != null ? engram.getUuid() : null;
    }

    public String getEngramImageFile(String name) {
        Engram engram = getEngramByName(name);

        return engram != null ? engram.getImageFile() : null;
    }

    public Resource getResourceByName(String name) {
        return resourceMap.get(name);
    }

    public Resource getSubstituteResourceByName(String name) {
        return resourceMap.get(name);
    }

    public Engram getEngramByName(String name) {
        return engramMap.get(name);
    }

    public Station getStationByName(String name) {
        return stationMap.get(name);
    }

    public Folder getFolderByCategoryId(long id) {
        return folderMap.get(id);
    }

    public void addNullifiedResource(String name) {
        if (!nullifiedResources.contains(name)) {
            nullifiedResources.add(name);
        }
    }

    public void removeNullifiedResource(String name) {
        nullifiedResources.remove(name);
    }

    public void mapGameData() {
        mapFoldersFromJson();
        mapResourcesFromJson();
        mapEngramsFromJson();
        mapStationsFromJson();
        mapCompositionFromJson();
        mapDirectoryFromJson();
    }

    public abstract JsonNode generateJson();

    void mapFoldersFromJson() {
        JsonNode categoryArray = inObject.get("category");
        for (JsonNode categoryObject : categoryArray) {
            JsonCategory jsonCategory = mapper.convertValue(categoryObject, JsonCategory.class);

            if (isValidDlcId(jsonCategory.dlc_id)) {
                folderMap.put(jsonCategory._id, buildFolder(jsonCategory));
            }
        }
    }

    public abstract Folder buildFolder(JsonCategory jsonCategory);

    void mapResourcesFromJson() {
        JsonNode resourceArray = inObject.get("resource");
        for (JsonNode resourceObject : resourceArray) {
            JsonResource jsonResource = mapper.convertValue(resourceObject, JsonResource.class);

            if (isValidDlcId(jsonResource.dlc_id)) {
                //  test if is complex resource, skip if true
                if (jsonResource.complex_resource) continue;

                resourceMap.put(jsonResource.name, buildResource(jsonResource));
            }
        }
    }

    public abstract Resource buildResource(JsonResource jsonResource);

    void mapEngramsFromJson() {
        JsonNode engramArray = inObject.get("engram");
        for (JsonNode engramObject : engramArray) {
            JsonEngram jsonEngram = mapper.convertValue(engramObject, JsonEngram.class);

            if (isValidDlcId(jsonEngram.dlc_id)) {
                engramMap.put(jsonEngram.name, buildEngram(jsonEngram));
            }
        }
    }

    public abstract Engram buildEngram(JsonEngram jsonEngram);

    void mapStationsFromJson() {
        JsonNode stationArray = inObject.get("station");
        for (JsonNode stationObject : stationArray) {
            JsonStation jsonStation = mapper.convertValue(stationObject, JsonStation.class);

            if (isValidDlcId(jsonStation.dlc_id)) {
                stationMap.put(jsonStation.name, buildStation(jsonStation));
            }
        }
    }

    public abstract Station buildStation(JsonStation jsonStation);

    void mapCompositionFromJson() {
        JsonNode engramArray = inObject.get("engram");
        for (JsonNode engramObject : engramArray) {
            JsonEngram jsonEngram = mapper.convertValue(engramObject, JsonEngram.class);

            if (isValidDlcId(jsonEngram.dlc_id)) {
                compositionMap.put(jsonEngram.name, buildComposition(jsonEngram));
            }
        }
    }

    public abstract Composition buildComposition(JsonEngram jsonEngram);

    void mapCompositesFromJson(String compositionId, JsonEngram jsonEngram) {
        for (JsonComposite jsonComposite : jsonEngram.composition) {
            List<Composite> compositeList = compositeMap.get(jsonComposite.resource_id);
            if (compositeList == null) compositeList = new ArrayList<>();
            compositeList.add(buildComposite(compositionId, jsonComposite));
            compositeMap.put(jsonComposite.resource_id, compositeList);
        }
    }

    public abstract Composite buildComposite(String compositionId, JsonComposite jsonComposite);

    List<Composite> flattenCompositeMapToList() {
        List<Composite> composites = new ArrayList<>();
        compositeMap.values().forEach(composites::addAll);
        return composites;
    }

    public void mapSubstitutesFromResourceMap() {
        for (Resource resource : resourceMap.values()) {
            List<String> subsByName = generateSubsForResource(resource.getName());
            if (subsByName.size() > 1) {
                List<String> subsById = convertResourceNamesToId(subsByName);
                if (subsById.size() > 1) {
                    substitutions.put(resource.getName(), subsById);
                }
            }
        }
    }

    public abstract int mapStationDirectoryItem(Station station);

    public abstract void mapEngramDirectoryItem(Engram engram, String parentId);

    public abstract int mapFolderDirectoryItem(Station station, Folder folder, long categoryId, String parentId);

    List<String> convertResourceNamesToId(List<String> in) {
        List<String> out = new ArrayList<>();

        for (String resourceName : in) {
            String uuid = getResourceUUID(resourceName);
            if (uuid == null) {
                uuid = getEngramUUID(resourceName);
                if (uuid == null) {
                    addNullifiedResource(resourceName);
                    continue;
                }
            }

            removeNullifiedResource(resourceName);

            out.add(uuid);
        }

        return out;
    }

    List<String> generateSubsForResource(String in) {
        //  add in substitutions
        String[] substitutes = new String[0];
        if (in.equals("Leech Blood or Horns")) {
            //  "Because of the current crafting system, some recipes can use either Leech Blood or Woolly Rhino Horn or Deathworm Horn."
            substitutes = new String[]{"Leech Blood", "Deathworm Horn", "Woolly Rhino Horn"};
        } else if (in.equals("Cooked Meat or Fish Meat")) {
            substitutes = new String[]{"Cooked Meat", "Cooked Fish Meat"};
        } else if (in.equals("Cooked Meat or Meat Jerky")) {
            substitutes = new String[]{"Cooked Meat", "Cooked Meat Jerky"};
        } else if (in.equals("Pelt, Hair, or Wool")) {
            substitutes = new String[]{"Pelt", "Human Hair", "Wool"};
        } else if (in.equals("Berries (Any Kind)")) {
            //  Berries
            substitutes = new String[]{"Amarberry", "Azulberry", "Mejoberry", "Narcoberry", "Stimberry", "Tintoberry"};
        } else if (in.equals("Feces (Any Size)")) {
            //  Poops
            substitutes = new String[]{"Human Feces", "Small Animal Feces", "Medium Animal Feces", "Large Animal Feces", "Massive Animal Feces"};
        } else if (in.equals("Extra Small Egg")) {
            substitutes = new String[]{"Dilo Egg", "Dodo Egg", "Featherlight Egg", "Kairuku Egg", "Lystrosaur Egg", "Parasaur Egg", "Tek Parasaur Egg", "Vulture Egg"};
        } else if (in.contains(",")) {
            //  remove "or", convert to list for each ","
            String newName = in.replace(", or ", ",");
            newName = newName.replace(", ", ",");
            substitutes = newName.split(",");
        } else if (in.contains(" or ")) {
            //  replace " or " with ","
            String newName = in.replace(" or ", ",");
            substitutes = newName.split(",");
        }

        return new ArrayList<>(Arrays.asList(substitutes));
    }

    /**
     * Tests if the dlc id provided matches
     *
     * @param dlcId dlc id to test
     * @return if both ids match
     */
    boolean isValidDlcId(long dlcId) {
        return jsonDlc._id == dlcId;
    }

    /**
     * Test if the dlc id provided is valid, used to add stations/folders that belong to primary game data
     *
     * @param dlcId dlc id to test
     * @return if dlc id is valid
     */
    public abstract boolean isValidDlcIdForDirectory(long dlcId);

    /**
     * directory [
     * {
     * uuid
     * name
     * imageFile
     * parentId
     * sourceId
     * gameId
     * }
     * ]
     */
    void mapDirectoryFromJson() {
        JsonNode stationArray = inObject.get("station");
        for (JsonNode stationObject : stationArray) {
            JsonStation jsonStation = mapper.convertValue(stationObject, JsonStation.class);

            if (isValidDlcIdForDirectory(jsonStation.dlc_id)) {
                //  get station from map
                Station station = getStationByName(jsonStation.name);

                //  map station object
                mapStationDirectoryItem(station);
            }
        }
    }

    int mapFolderDirectory(final Station station, final long categoryId, final String parentId) {
        int count = 0;

        JsonNode categoryArray = inObject.get("category");
        for (JsonNode categoryObject : categoryArray) {
            JsonCategory jsonCategory = mapper.convertValue(categoryObject, JsonCategory.class);

            if (isValidDlcIdForDirectory(jsonCategory.dlc_id)) {
                //  test if current station exists for category
                if (!jsonCategory.station.contains(station.getName())) continue;

                //  test if both categories share the same parent
                if (jsonCategory.parent_id != categoryId) continue;

                //  get folder from folderMap
                Folder folder = getFolderByCategoryId(jsonCategory._id);

                //  map folder object
                int itemCount = mapFolderDirectoryItem(station, folder, jsonCategory._id, parentId);

                count += itemCount;
            }
        }

        return count;
    }

    int mapEngramDirectory(final Station station, final long categoryId, final String parentId) {
        int count = 0;

        JsonNode engramArray = inObject.get("engram");
        for (JsonNode engramObject : engramArray) {
            JsonEngram jsonEngram = mapper.convertValue(engramObject, JsonEngram.class);

            if (isValidDlcId(jsonEngram.dlc_id)) {
                //  test if current station exists for category
                if (!jsonEngram.station.contains(station.getName())) continue;

                //  test if engram shares the same parent
                if (jsonEngram.category_id != categoryId) continue;

                //  get engram from engramMap
                Engram engram = getEngramByName(jsonEngram.name);

                //  create engram json object
                mapEngramDirectoryItem(engram, parentId);

                //  increase count for length validity
                count++;
            }
        }

        return count;
    }

    public abstract String buildFilePathForJSONExport();
}