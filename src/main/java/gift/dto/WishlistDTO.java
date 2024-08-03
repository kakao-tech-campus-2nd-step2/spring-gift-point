package gift.dto;

public class WishlistDTO {
    private String userId;
    private Long productId;
    private Long optionId;  // 새로운 필드 추가

    public WishlistDTO(String userId, Long productId, Long optionId) {
        this.userId = userId;
        this.productId = productId;
        this.optionId = optionId;
    }

    public String getUserId() {
        return userId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOptionId() {
        return optionId;
    }
}