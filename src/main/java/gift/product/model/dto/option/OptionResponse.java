package gift.product.model.dto.option;

public class OptionResponse {
    private final Long id;
    private final String name;
    private final int quantity;
    private final int additionalCost;

    public OptionResponse(Long id, String name, int quantity, int additionalCost) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.additionalCost = additionalCost;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getAdditionalCost() {
        return additionalCost;
    }
}
