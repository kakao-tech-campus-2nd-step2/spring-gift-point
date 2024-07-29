package gift.domain.model.dto;

public class WishResponseDto {

    private Long id;
    private Integer count;
    private Long productId;
    private String productName;
    private Long productPrice;
    private String productImageUrl;

    public Long getId() {
        return id;
    }

    public Integer getCount() {
        return count;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Long getProductPrice() {
        return productPrice;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }


    public WishResponseDto() {
    }

    public WishResponseDto(Long id, Integer count, Long productId, String productName,
        Long productPrice,
        String productImageUrl) {
        this.id = id;
        this.count = count;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
    }
}
