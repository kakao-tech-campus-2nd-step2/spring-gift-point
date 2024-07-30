package gift.dto;

import gift.model.Option;
import gift.model.Product;

public class OptionRequestDTO {

    private String name;
    private int quantity;
    private Long productId;

    protected OptionRequestDTO() {}

    public OptionRequestDTO(String name, int quantity, Long productId) {
        this.name = name;
        this.quantity = quantity;
        this.productId = productId;
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

    public Option toEntity(Product product) {
        return new Option(this.name, this.quantity, product);
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
