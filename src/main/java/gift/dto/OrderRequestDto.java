package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderRequestDto {
    private Long productOptionId;
    private int quantity;
    private String message;

    public OrderRequestDto(@JsonProperty("OptionId") Long productOptionId, int quantity, String message) {
        this.productOptionId = productOptionId;
        this.quantity = quantity;
        this.message = message;
    }

    public Long getProductOptionId() {
        return productOptionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }
}