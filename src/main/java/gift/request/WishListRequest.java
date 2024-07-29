package gift.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record WishListRequest(
    @NotNull(message = "상품 id를 입력해주세요.")
    @JsonProperty("product_id")
    Long productId
) {

}
