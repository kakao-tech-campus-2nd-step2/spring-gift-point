package gift.dto;

public class WishResponseDto {
    private final Long id;
    private final Long productId;
    private final String productName;
    private final int productPrice;
    private final String productImageUrl;

    public WishResponseDto(Long id, Long productId, String productName, int productPrice, String productImageUrl) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }
}
