package gift.dto;

public class WishRequestDto {
    private Long productId;
    private int quantity;

    public WishRequestDto(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
