package gift.dto;

public class RequestOrderDto {

    private Long optionId;
    private int quantity;
    private String message;
    private int point;

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

    public int getPoint() {
        return point;
    }
}
