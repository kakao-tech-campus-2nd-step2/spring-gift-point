package gift.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
    @NotNull(message = "product_id를 입력해주세요.")
    @JsonProperty(value = "product_id")
    Long productId,
    @NotNull(message = "option_id를 입력해주세요.")
    @JsonProperty(value = "option_id")
    Long optionId,
    @NotNull(message = "주문할 수량을 입력해주세요.")
    Integer quantity,
    String message,
    @NotNull(message = "사용할 포인트를 입력해주세요.")
    Integer point
) {

}
