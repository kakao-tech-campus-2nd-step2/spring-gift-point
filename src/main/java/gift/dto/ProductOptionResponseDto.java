package gift.dto;

public class ProductOptionResponseDto {
    private Long id;
    private String productName;
    private String optionName;
    private int quantity;

    public ProductOptionResponseDto() {
    }

    public ProductOptionResponseDto(Long id, String productName, String optionName, int quantity) {
        this.id = id;
        this.productName = productName;
        this.optionName = optionName;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getOptionName() {
        return optionName;
    }

    public int getQuantity() {
        return quantity;
    }
}
