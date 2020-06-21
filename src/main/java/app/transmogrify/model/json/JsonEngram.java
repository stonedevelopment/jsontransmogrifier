package app.transmogrify.model.json;

import java.util.List;

/**
 *       "name": "APS Mailbox",
 *       "description": "Mailbox for the Federated Postal Service of ARK.",
 *       "yield": 1,
 *       "level": 20,
 *       "category_id": 2022,
 *       "image_folder": "Primitive Plus\/Engrams\/Structures\/Wood\/Storage\/",
 *       "image_file": "aps_mailbox.webp",
 *       "points": 0,
 *       "xp": 0,
 *       "steam_id": 0,
 *       "console_id": 0,
 *       "dlc_id": 2,
 *       "composition": [
 *         {
 *           "resource_id": "Wood",
 *           "quantity": 20
 *         },
 *         {
 *           "resource_id": "Iron Ingot",
 *           "quantity": 5
 *         }
 *       ],
 *       "station": [
 *         "Construction Table"
 *       ]
 */
public class JsonEngram {
    public String name;
    public String description;
    public int yield;
    public int level;
    public long category_id;
    public String image_folder;
    public String image_file;
    public int points;
    public int xp;
    public int steam_id;
    public int console_id;
    public long dlc_id;
    public List<JsonComposite> composition;
    public List<String> station;
}
