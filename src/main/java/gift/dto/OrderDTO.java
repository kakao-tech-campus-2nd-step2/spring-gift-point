package gift.dto;


public class OrderDTO {
    private Long orderId;
    private Long optionId;
    private int quantity;
    private String message;

    public OrderDTO() {}


    public OrderDTO(Long orderId, Long optionId, int quantity, String message) {
        this.orderId = orderId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
    }

    // Getters and setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}