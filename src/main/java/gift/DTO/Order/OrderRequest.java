package gift.DTO.Order;

public class OrderRequest {
    private Long optionId;
    private int quantity;
    private String message;
    private Long productId;

    public OrderRequest() {
    }

    public OrderRequest(Long optionId, int quantity, String message, Long productId) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.productId = productId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public Long getProductId() {
        return productId;
    }
}
