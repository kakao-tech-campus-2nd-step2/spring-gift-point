package gift.dto;

public class GiftOrderRequestDto {
    private Long optionId;
    private Long quantity;
    private String message;

    public GiftOrderRequestDto(Long optionId, Long quantity, String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
    }

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
