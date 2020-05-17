package transmogrify.model.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import transmogrify.model.details.Details;
import transmogrify.model.json.*;
import transmogrify.model.primary.*;

import java.util.*;

public abstract class GameData {
    public static final boolean cDebug = false; // TODO: 4/26/2020 Set to FALSE when finalizing

    public List<String> nullifiedResources = new ArrayList<>();

    public long dlcId;
    JsonNode inObject;
    ObjectMapper mapper;

    Map<String, Resource> resourceMap = new TreeMap<>();
    Map<Long, Folder> folderMap = new TreeMap<>();
    Map<String, Engram> engramMap = new TreeMap<>();
    Map<String, Station> stationMap = new TreeMap<>();
    Map<String, Composition> compositionMap = new TreeMap<>();
    Map<String, List<String>> substitutions = new TreeMap<>();

    public GameData(long dlcId, JsonNode inObject, ObjectMapper mapper) {
        this.dlcId = dlcId;
        this.inObject = inObject;
        this.mapper = mapper;
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public abstract Details createDetailsObject(long dlcId);

    public String getResourceUUID(String name) {
        Resource resource = getResourceByName(name);

        return resource != null ? resource.getUuid() : null;
    }

    public String getEngramUUID(String name) {
        Engram engram = getEngramByName(name);

        return engram != null ? engram.getUuid() : null;
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
//        mapSubstitutesFromResourceMap();  // TODO: 5/5/2020 removing substitutes for now, let's update this app already!
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

    List<Composite> convertJsonComposition(List<JsonComposite> oldComposition) {
        List<Composite> newComposition = new ArrayList<>();
        for (JsonComposite oldComposite : oldComposition) {
            String resourceId = getResourceUUID(oldComposite.resource_id);
            int quantity = oldComposite.quantity;
            String engramId = getEngramUUID(oldComposite.resource_id);

            newComposition.add(new Composite(resourceId, quantity, engramId));
        }

        return newComposition;
    }

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
        return this.dlcId == dlcId;
    }

    /**
     * Test if the dlc id provided is valid, used to add stations/folders that belong to primary game data
     *
     * @param dlcId dlc id to test
     * @return if dlc id is valid
     */
    public abstract boolean isValidDlcIdForDirectory(long dlcId);

    /**
     * Creates a hierarchical directory with only UUIDs for app to easily and quickly fill database with parent
     * information.
     *
     * @return JSON object of directory section
     */
    ArrayNode createDirectorySection() {
        ArrayNode outNode = mapper.createArrayNode();

        JsonNode stationArray = inObject.get("station");
        for (JsonNode stationObject : stationArray) {
            JsonStation jsonStation = mapper.convertValue(stationObject, JsonStation.class);

            if (isValidDlcIdForDirectory(jsonStation.dlc_id)) {
                //  get station from map
                Station station = getStationByName(jsonStation.name);

                //  create station object
                ObjectNode stationNode = mapper.createObjectNode();
                stationNode.put("station", station.getUuid());

                //  add engrams from json
                JsonNode engramArray = createJsonArrayForEngrams(station, 0);
                stationNode.set("engrams", engramArray);

                //  add folders from json
                JsonNode folderArray = createJsonArrayForFolders(station, 0);
                stationNode.set("folders", folderArray);

                if (engramArray.size() > 0 || folderArray.size() > 0) {
                    outNode.add(stationNode);
                }
            }
        }

        return outNode;
    }

    private ArrayNode createJsonArrayForFolders(final Station station, final long parentId) {
        ArrayNode outNode = mapper.createArrayNode();

        JsonNode categoryArray = inObject.get("category");
        for (JsonNode categoryObject : categoryArray) {
            JsonCategory jsonCategory = mapper.convertValue(categoryObject, JsonCategory.class);

            if (isValidDlcIdForDirectory(jsonCategory.dlc_id)) {
                //  test if current station exists for category
                if (!jsonCategory.station.contains(station.getName())) continue;

                //  test if both categories share the same parent
                if (jsonCategory.parent_id != parentId) continue;

                //  get folder from folderMap
                Folder folder = getFolderByCategoryId(jsonCategory._id);

                //  create folder json object
                ObjectNode folderNode = mapper.createObjectNode();
                folderNode.put("uuid", folder.getUuid());

                //  add engrams from json
                JsonNode engramArray = createJsonArrayForEngrams(station, jsonCategory._id);
                folderNode.set("engrams", engramArray);

                //  add folders from json
                JsonNode folderArray = createJsonArrayForFolders(station, jsonCategory._id);
                folderNode.set("folders", folderArray);

                if (engramArray.size() > 0 || folderArray.size() > 0) {
                    outNode.add(folderNode);
                }
            }
        }

        return outNode;
    }

    private JsonNode createJsonArrayForEngrams(final Station station, final long parentId) {
        ArrayNode outNode = mapper.createArrayNode();

        JsonNode engramArray = inObject.get("engram");
        for (JsonNode engramObject : engramArray) {
            JsonEngram jsonEngram = mapper.convertValue(engramObject, JsonEngram.class);

            if (isValidDlcId(jsonEngram.dlc_id)) {
                //  test if current station exists for category
                if (!jsonEngram.station.contains(station.getName())) continue;

                //  test if engram shares the same parent
                if (jsonEngram.category_id != parentId) continue;

                //  get engram from engramMap
                Engram engram = getEngramByName(jsonEngram.name);

                //  create engram json object
                ObjectNode engramNode = mapper.createObjectNode();
                engramNode.put("uuid", engram.getUuid());

                //  add engram to array
                outNode.add(engramNode);
            }
        }

        return outNode;
    }

    public JsonNode createSubsSection() {
        ArrayNode outNode = mapper.createArrayNode();

        for (Map.Entry<String, List<String>> entry : substitutions.entrySet()) {
            String resourceName = entry.getKey();
            Resource resource = getResourceByName(resourceName);

            List<String> uuidList = entry.getValue();

            ObjectNode subNode = mapper.createObjectNode();
            subNode.set(resource.getUuid(), mapper.valueToTree(uuidList));

            outNode.add(subNode);
        }

        return outNode;
    }

    public abstract String buildFilePath(String dlcName);

    public abstract String buildFilePathForJSONExport();

    String getNameByDlcId(final long dlcId) {
        if (dlcId == 1) {
            return "ARK:Survival Evolved";
        } else if (dlcId == 2) {
            return "Primitive Plus";
        } else if (dlcId == 3) {
            return "Scorched Earth";
        } else if (dlcId == 4) {
            return "Aberration";
        } else if (dlcId == 5) {
            return "Extinction";
        } else {
            return "UNKNOWN DLC ID: " + dlcId;
        }
    }

    String getDescriptionByDlcId(final long dlcId) {
        if (dlcId == 1) {
            return "As a man or woman stranded, naked, freezing, and starving on the unforgiving shores of a mysterious island called \"ARK\", use your skill and cunning to kill or tame and ride the plethora of leviathan dinosaurs and other primeval creatures roaming the land. Hunt, harvest resources, craft items, grow crops, research technologies, and build shelters to withstand the elements and store valuables, all while teaming up with (or preying upon) hundreds of other players to survive, dominate... and escape!";
        } else if (dlcId == 2) {
            return "Primitive Plus is a free add-on for ARK: Survival Evolved that alters the available tools, weapons and structures in the game to reflect what humans could realistically create using primitive technology and resources. This does not simply remove advanced technology from ARK, but instead replaces it with a multitude of new resources, engrams and systems.";
        } else if (dlcId == 3) {
            return "Stranded naked, dehydrated & starving in a vast desert, even the most seasoned ARK survivors must quickly find water, hunt for food, harvest, craft items, and build shelter to have any chance for survival. Use skills honed on ARK's faraway Island to kill, tame, breed, and ride the fantastical new creatures that have evolved to survive the Desert's ultra harsh conditions, including... DRAGONS! Travel back and forth between the Island and the Desert to team up with hundreds of players across both worlds or play locally!";
        } else if (dlcId == 4) {
            return "Waking up on ‘Aberration’, a derelict, malfunctioning ARK with an elaborate underground biome system, survivors face exotic new challenges unlike anything before: extreme radioactive sunlight and environmental hazards, ziplines, wingsuits, climbing gear, cave dwellings, charge-batteries, and far more, along with a stable of extraordinary new creatures await within the mysterious depths. But beware the ‘Nameless’: unrelenting, Element-infused humanoids which have evolved into vicious light-hating monstrosities! On Aberration, survivors will uncover the ultimate secrets of the ARKs, and discover what the future holds in store for those strong and clever enough to survive!";
        } else if (dlcId == 5) {
            return "Finish your journey through the worlds of ARK in ‘Extinction’, where the story began and ends: on Earth itself! An Element-infested, ravaged planet filled with fantastical creatures both organic & technological, Earth holds both the secrets of the past and the keys to its salvation. As a veteran Survivor who has conquered all previous obstacles, your ultimate challenge awaits: can you defeat the gigantic roaming Titans which dominate the planet, and complete the ARK cycle to save Earth's future?";
        } else {
            return "UNKNOWN DLC ID: " + dlcId;
        }
    }
}
