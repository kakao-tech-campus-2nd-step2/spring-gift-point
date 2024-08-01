package gift.dto;

public class WishAddRequestDto {
    private final Long productId;
    private int quantity = 1;

    public WishAddRequestDto(long productId, int quantity) {
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
