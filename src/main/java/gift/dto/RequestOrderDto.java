package gift.dto;

public class RequestOrderDto {

    private Long optionId;
    private int quantity;
    private String message;

    public RequestOrderDto() {
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
