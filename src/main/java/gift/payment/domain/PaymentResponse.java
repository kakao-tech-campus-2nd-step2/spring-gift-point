package gift.payment.domain;

public class PaymentResponse {
    private Long id;
    private Long optionId;
    private Long quantity;
    private String orderDateTime;
    private String message;
    private boolean success;

    public PaymentResponse(Long id, Long optionId, Long quantity, String orderDateTime, String message, boolean success) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
        this.success = success;
    }

    public Long getId() {
        return id;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
