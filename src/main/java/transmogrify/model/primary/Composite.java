package transmogrify.model.primary;

public class Composite {
    private final String resourceId;
    private final int quantity;
    private final String engramId;

    public Composite(String resourceId, int quantity, String engramId) {
        this.resourceId = resourceId;
        this.quantity = quantity;
        this.engramId = engramId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getEngramId() {
        return engramId;
    }
}