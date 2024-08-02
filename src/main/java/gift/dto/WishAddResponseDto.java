package gift.dto;

public class WishAddResponseDto {
    private Long id;
    private Long productId;

    public WishAddResponseDto(Long id, Long productId) {
        this.id = id;
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }
}
