package gift.dto;

public class OptionResponseDto {
    private final Long id;
    private final String name;
    private final Long quantity;
    private final Long product_id;

    public OptionResponseDto(Long id, String name, Long quantity, Long product_id) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.product_id = product_id;
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

    public Long getProduct_id() {
        return product_id;
    }
}