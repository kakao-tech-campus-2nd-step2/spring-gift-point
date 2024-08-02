package gift.dto;

public class OrderDTO {
    private Long optionId;
    private int quantity;
    private String message;
    private Boolean usePoint;

    public OrderDTO(Long optionId, int quantity, String message, boolean usePoint) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.usePoint = usePoint;
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

    public boolean getPoint() {
        return usePoint;
    }
}
