package gift.dto;

public class OrderRequestDTO {
    private Long optionId;
    private int quantity;
    private int points;
    private String message;

    public OrderRequestDTO() {}

    public OrderRequestDTO(Long optionId, int quantity, int points, String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.points = points;
        this.message = message;
    }

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPoints() {
        return points;
    }

    public String getMessage() {
        return message;
    }
}
