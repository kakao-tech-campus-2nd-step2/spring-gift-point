package gift.dto;

public class WishCUDResponseDto {
    private Long productId;

    public WishCUDResponseDto(Long productId) {
        this.productId = productId;
    }



    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
