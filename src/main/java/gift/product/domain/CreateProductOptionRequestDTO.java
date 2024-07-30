package gift.product.domain;

public class CreateProductOptionRequestDTO {

    private static final int MAX_INPUT_LENGTH = 255;

    private String name;
    private Long quantity;

    public String getName() {
        return name;
    }

    public Long getQuantity() {
        return quantity;
    }
}
