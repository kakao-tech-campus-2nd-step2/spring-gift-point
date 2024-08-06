package gift.DTO.Order;

public class OrderRequest {
    private Long optionId;
    private int quantity;
    private String message;
    private Long productId;
    private int point;

    public OrderRequest() {
    }

    public OrderRequest(Long optionId, int quantity, String message, Long productId, int point) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.productId = productId;
        this.point = point;
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

    public int getPoint() {
        return point;
    }
}
