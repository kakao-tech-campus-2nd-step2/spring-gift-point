package gift.dto;

public class OrderRequest {
    private Long optionId;
    private int quantity;
    private String message;
    private int pointsToUse;

    public OrderRequest() {
    }

    public OrderRequest(Long optionId, int quantity, String message, int pointsToUse) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.pointsToUse = pointsToUse;
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

    public int getPointsToUse() {
        return pointsToUse;
    }

    public void setPointsToUse(int pointsToUse) {
        this.pointsToUse = pointsToUse;
    }
}