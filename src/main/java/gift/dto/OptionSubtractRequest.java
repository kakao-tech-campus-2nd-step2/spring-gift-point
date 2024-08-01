package gift.dto;

import gift.constants.ErrorMessage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OptionSubtractRequest {

    @NotNull
    private Long id;

    @Min(value = 1, message = ErrorMessage.OPTION_QUANTITY_INVALID_MSG)
    @Max(value = 99_999_999, message = ErrorMessage.OPTION_QUANTITY_INVALID_MSG)
    private int quantity;

    @NotNull
    private Long productId;

    public OptionSubtractRequest(Long id, int quantity, Long productId) {
        this.id = id;
        this.quantity = quantity;
        this.productId = productId;
    }

    public OptionSubtractRequest(OrderRequest orderRequest) {
        this(orderRequest.getOptionId(), orderRequest.getQuantity(), orderRequest.getProductId());
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }
}
