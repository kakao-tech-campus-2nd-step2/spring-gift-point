package gift.order;

import jakarta.validation.constraints.NotNull;

public class OrderRequest {
    @NotNull
    long optionId;
    @NotNull
    long quantity;
    String message;

    public OrderRequest(long optionId, long quantity, String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
    }

    public long getOptionId() {
        return optionId;
    }

    public long getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public void setOptionId(long optionId) {
        this.optionId = optionId;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
