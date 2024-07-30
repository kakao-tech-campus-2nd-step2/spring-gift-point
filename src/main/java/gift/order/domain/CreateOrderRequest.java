package gift.order.domain;

public class CreateOrderRequest {
    private Long optionId;
    private Long quantity;
    private String message;

    public Long getOptionId() {
        return optionId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }
}
