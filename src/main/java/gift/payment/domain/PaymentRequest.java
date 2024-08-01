package gift.payment.domain;

public class PaymentRequest {
    private Long optionId;
    private String message;
    private Long quantity;
    private Long productId;
    private Long point;
    private String phone;
    private boolean receipt;

    public PaymentRequest(Long optionId, String message, Long quantity, Long productId, Long point, String phone, boolean receipt) {
        this.optionId = optionId;
        this.message = message;
        this.quantity = quantity;
        this.productId = productId;
        this.point = point;
        this.phone = phone;
        this.receipt = receipt;
    }

    public Long getOptionId() {
        return optionId;
    }

    public String getMessage() {
        return message;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getPoint() {
        return point;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isReceipt() {
        return receipt;
    }


}
