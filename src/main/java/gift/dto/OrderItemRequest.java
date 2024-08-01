package gift.dto;

public class OrderItemRequest {
    private Long productId;
    private Long optionId;
    private int quantity;

    public OrderItemRequest() {
    }

    public OrderItemRequest(Long productId, Long optionId, int quantity) {
        this.productId = productId;
        this.optionId = optionId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

}
