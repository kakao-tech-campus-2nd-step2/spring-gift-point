package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderRequestDto {
    private Long productOptionId;
    private int quantity;
    private String message;
    private boolean usePoint;

    public OrderRequestDto(@JsonProperty("OptionId") Long productOptionId, int quantity, String message, boolean usePoint) {
        this.productOptionId = productOptionId;
        this.quantity = quantity;
        this.message = message;
        this.usePoint = usePoint;
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

    public boolean isUsePoint() {
        return usePoint;
    }
}
