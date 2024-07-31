package gift.dto;

public class ProductOptionResponseDto {
    private Long id;
    private Long productId;
    private String optionName;
    private int quantity;

    public ProductOptionResponseDto() {
    }

    public ProductOptionResponseDto(Long id, Long productId, String optionName, int quantity) {
        this.id = id;
        this.productId = productId;
        this.optionName = optionName;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getOptionName() {
        return optionName;
    }

    public int getQuantity() {
        return quantity;
    }
}
