package gift.dto;

public class OptionResponse {
    private final Long id;
    private final String name;
    private final Long quantity;

    public OptionResponse(Long id, String name, Long quantity) {
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
    public Long getQuantity() {
        return quantity;
    }
}
