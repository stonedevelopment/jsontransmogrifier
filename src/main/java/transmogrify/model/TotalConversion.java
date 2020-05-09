package transmogrify.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *     {
 *       "dlc_id": 2,
 *       "resource": [
 *         {
 *           "from": "Metal Ingot",
 *           "to": "Iron Ingot"
 *         },
 *         {
 *           "from": "Metal Ore",
 *           "to": "Iron Ore"
 *         },
 *         {
 *           "from": "Spoiled Meat",
 *           "to": "Rotten Meat"
 *         },
 *         {
 *           "from": "Raw Meat",
 *           "to": "Fresh Meat"
 *         }
 *       ],
 *       "station": [],
 *       "category": [],
 *       "engram": [
 *         "Metal Ingot",
 *         "Canteen"
 *       ]
 *     },
 */
public class TotalConversion {
    //  convert from ? to ?
    public Map<String, String> resourcesToReplace = new TreeMap<>();

    //  remove all in lists
    public List<String> stationsToRemove = new ArrayList<>();
    public List<Long> foldersToRemove = new ArrayList<>();
    public List<String> engramsToRemove = new ArrayList<>();
}
