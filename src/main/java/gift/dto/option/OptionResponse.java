package gift.dto.option;

public class OptionResponse {
    private final Long id;
    private final String name;
    private final int quantity;

    public OptionResponse(Long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
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
}
