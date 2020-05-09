package transmogrify.model;

import java.util.Date;
import java.util.List;

public class Composition {
    //  unique id for this composition
    public String uuid;

    //  uuid of Engram tied to this composition
    public String engramId;

    //  list of composites for this composition
    public List<Composite> compositeList;

    //  date of when this composition was last updated
    public Date lastUpdated;
}
