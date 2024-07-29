package gift.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderCreateRequest {

    @NotNull
    private Long optionId;

    @Min(1)
    @NotNull
    private Long quantity;

    private String message = "주문 성공!";

    public Long getOptionId() {
        return optionId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

}
