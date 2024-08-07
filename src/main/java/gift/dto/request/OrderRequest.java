package gift.dto.request;

public class OrderRequest {

    private final Long optionId;
    private final int quantity;
    private final String message;
    private final int point;

    public OrderRequest(Long id, Long optionId, int quantity, String message, int point) {

        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.point = point;

    }

    public int getPoint() {
        return point;
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