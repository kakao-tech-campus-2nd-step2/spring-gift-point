package gift.dto;

public class OrderDTO {
    private Long productId;
    private int quantity;

    public OrderDTO(Long productId, int quantity) {
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
