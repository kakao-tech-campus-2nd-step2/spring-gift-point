package gift.dto;

public class OptionDto {
    private final Long id;
    private final String name;
    private final int quantity;
    private final Long productId;

    public OptionDto(Long id, String name, int quantity, Long productId) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.productId = productId;
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

    public Long getProductId() {
        return productId;
    }
}