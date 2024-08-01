package gift.dto;

public class WishRequestDto {
    private Long productId;

    public WishRequestDto() {
    }

    public WishRequestDto(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
