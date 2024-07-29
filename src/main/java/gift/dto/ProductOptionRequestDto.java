package gift.dto;

public class ProductOptionRequestDto {
    private Long productId;
    private Long optionId;
    private int quantity;

    public ProductOptionRequestDto() {
    }

    public ProductOptionRequestDto(Long productId, Long optionId, int quantity) {
        this.productId = productId;
        this.optionId = optionId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }
}
