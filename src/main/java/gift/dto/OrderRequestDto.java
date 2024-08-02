package gift.dto;

public class OrderRequestDto {

    private Long optionId;
    private int quantity;
    private String message;

    public OrderRequestDto() {
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
}
