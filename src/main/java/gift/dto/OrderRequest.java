package gift.dto;

import gift.constants.ErrorMessage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderRequest {

    @NotNull
    private Long productId;

    @NotNull
    private Long optionId;

    @Min(value = 1, message = ErrorMessage.OPTION_QUANTITY_INVALID_MSG)
    @Max(value = 99_999_999, message = ErrorMessage.OPTION_QUANTITY_INVALID_MSG)
    private int quantity;

    private String message;

    protected OrderRequest() {
    }

    public Long getProductId() {
        return productId;
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