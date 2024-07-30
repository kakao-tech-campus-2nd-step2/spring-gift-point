package gift.dto;

import jakarta.validation.constraints.NotNull;

public class OrderRequest {

    @NotNull
    private Long optionId;

    @NotNull
    private int quantity;

    private String message;

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
