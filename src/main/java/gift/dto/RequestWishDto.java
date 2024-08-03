package gift.dto;

public class RequestWishDto {

    private Long productId;

    public RequestWishDto() {
    }

    public RequestWishDto(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
