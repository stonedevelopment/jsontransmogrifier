package app.transmogrify.model.json;

import java.util.List;

/**
 *       "_id": 100,
 *       "name": "Misc",
 *       "parent_id": 0,
 *       "dlc_id": 1,
 *       "station": [
 *         "Self",
 *         "Fabricator",
 *         "Smithy",
 *         "Construction Table",
 *         "Cooking Station"
 *       ]
 */
public class JsonCategory {
    public long _id;
    public String name;
    public long parent_id;
    public long dlc_id;
    public List<String> station;
}
